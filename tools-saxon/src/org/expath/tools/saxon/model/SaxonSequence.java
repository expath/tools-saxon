/****************************************************************************/
/*  File:       SaxonSequence.java                                          */
/*  Author:     F. Georges - H2O Consulting                                 */
/*  Date:       2011-03-10                                                  */
/*  Tags:                                                                   */
/*      Copyright (c) 2011 Florent Georges (see end of file.)               */
/* ------------------------------------------------------------------------ */


package org.expath.tools.saxon.model;

import java.io.OutputStream;
import java.util.Properties;
import javax.xml.namespace.QName;
import javax.xml.transform.OutputKeys;
import net.sf.saxon.Configuration;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.SaxonOutputKeys;
import net.sf.saxon.om.Item;
import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.query.QueryResult;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.tree.iter.SingletonIterator;
import org.expath.tools.ToolsException;
import org.expath.tools.model.Sequence;
import org.expath.tools.serial.SerialParameters;

/**
 * Saxon implementation of {@link Sequence}, relying on {@link SequenceIterator}.
 *
 * @author Florent Georges
 */
public class SaxonSequence
        implements Sequence
{
    public SaxonSequence(SequenceIterator it, XPathContext ctxt)
    {
        myIt = it;
        myCtxt = ctxt;
    }

    @Override
    public boolean isEmpty()
            throws ToolsException
    {
        try {
            return myIt == null || myIt.getAnother().next() == null;
        }
        catch ( XPathException ex ) {
            throw new ToolsException("Error getting another iterator", ex);
        }
    }

    @Override
    public Sequence next()
            throws ToolsException
    {
        Item item;
        try {
            item = myIt == null ? null : myIt.next();
        }
        catch ( XPathException ex ) {
            throw new ToolsException("Error getting the next item in the sequence", ex);
        }
        SequenceIterator it = SingletonIterator.makeIterator(item);
        return new SaxonSequence(it, myCtxt);
    }

    @Override
    public void serialize(OutputStream out, SerialParameters params)
            throws ToolsException
    {
        Properties props = params == null ? null : makeOutputProperties(params);
        Configuration config = myCtxt.getConfiguration();
        try {
            QueryResult.serializeSequence(myIt, config, out, props);
        }
        catch ( XPathException ex ) {
            throw new ToolsException("Error serializing the sequence", ex);
        }
    }

    // TODO: This is really an old way to do this!  Using strings...  Should
    // really switch to S9API...
    private Properties makeOutputProperties(SerialParameters params)
            throws ToolsException
    {
        Properties props = new Properties();

        setOutputKey(props, OutputKeys.METHOD,                 params.getMethod());
        setOutputKey(props, OutputKeys.MEDIA_TYPE,             params.getMediaType());
        setOutputKey(props, OutputKeys.ENCODING,               params.getEncoding());
        setOutputKey(props, OutputKeys.CDATA_SECTION_ELEMENTS, params.getCdataSectionElements());
        setOutputKey(props, OutputKeys.DOCTYPE_PUBLIC,         params.getDoctypePublic());
        setOutputKey(props, OutputKeys.DOCTYPE_SYSTEM,         params.getDoctypeSystem());
        setOutputKey(props, OutputKeys.INDENT,                 params.getIndent());
        setOutputKey(props, OutputKeys.OMIT_XML_DECLARATION,   params.getOmitXmlDeclaration());
        setOutputKey(props, OutputKeys.STANDALONE,             params.getStandalone());
        setOutputKey(props, OutputKeys.VERSION,                params.getVersion());

        setOutputKey(props, SaxonOutputKeys.BYTE_ORDER_MARK,       params.getByteOrderMark());
        setOutputKey(props, SaxonOutputKeys.ESCAPE_URI_ATTRIBUTES, params.getEscapeUriAttributes());
        setOutputKey(props, SaxonOutputKeys.INCLUDE_CONTENT_TYPE,  params.getIncludeContentType());
        setOutputKey(props, SaxonOutputKeys.NORMALIZATION_FORM,    params.getNormalizationForm());
        setOutputKey(props, SaxonOutputKeys.UNDECLARE_PREFIXES,    params.getUndeclarePrefixes());
        Iterable<SerialParameters.UseChar> maps = params.getUseCharacterMaps();
        if ( maps != null ) {
            if ( maps.iterator().hasNext() ) {
                // TODO: How are we supposed to pass this param through a string property?
                throw new ToolsException("Use character map serialization parameter is not supported");
            }
        }

        setExtensionKey(props, params, SaxonOutputKeys.CHARACTER_REPRESENTATION);
        setExtensionKey(props, params, SaxonOutputKeys.DOUBLE_SPACE);
        setExtensionKey(props, params, SaxonOutputKeys.INDENT_SPACES);
        setExtensionKey(props, params, SaxonOutputKeys.LINE_LENGTH);
        setExtensionKey(props, params, SaxonOutputKeys.RECOGNIZE_BINARY);
        setExtensionKey(props, params, SaxonOutputKeys.REQUIRE_WELL_FORMED);
        setExtensionKey(props, params, SaxonOutputKeys.STYLESHEET_VERSION);
        setExtensionKey(props, params, SaxonOutputKeys.SUPPRESS_INDENTATION);
        setExtensionKey(props, params, SaxonOutputKeys.WRAP);

        return props;
    }

    private QName parseClarkNotation(String clark)
    {
        if ( clark.startsWith("{") ) {
            int idx = clark.indexOf('}');
            String uri = clark.substring(1, idx);
            String local = clark.substring(idx + 1);
            return new QName(uri, local);
        }
        else {
            return new QName(clark);
        }
    }

    private void setExtensionKey(Properties props, SerialParameters params, String name)
            throws ToolsException
    {
        QName qname = parseClarkNotation(name);
        String value = params.getExtension(qname);
        if ( value != null ) {
            props.setProperty(name, value);
        }
    }

    private void setOutputKey(Properties props, String name, String value)
            throws ToolsException
    {
        if ( value != null ) {
            props.setProperty(name, value);
        }
    }

    private void setOutputKey(Properties props, String name, Boolean value)
            throws ToolsException
    {
        if ( value != null ) {
            props.setProperty(name, value ? "yes" : "no");
        }
    }

    private void setOutputKey(Properties props, String name, SerialParameters.Standalone value)
            throws ToolsException
    {
        if ( value != null ) {
            switch ( value ) {
                case YES:
                    props.setProperty(name, "yes");
                    break;
                case NO:
                    props.setProperty(name, "no");
                    break;
                case OMIT:
                    props.setProperty(name, "omit");
                    break;
                default:
                    throw new ToolsException("Invalid Standalone value: " + value);
            }
        }
    }

    private void setOutputKey(Properties props, String name, QName value)
            throws ToolsException
    {
        if ( value != null ) {
            if ( value.getNamespaceURI() != null ) {
                throw new ToolsException(
                        "A QName with a non-null namespace not supported as a serialization param: {"
                                + value.getNamespaceURI() + "}" + value.getLocalPart());
            }
            props.setProperty(name, value.getLocalPart());
        }
    }

    private void setOutputKey(Properties props, String name, Iterable<QName> value)
            throws ToolsException
    {
        if ( value != null ) {
            StringBuilder buf = new StringBuilder();
            for ( QName qname : value ) {
                if ( qname.getNamespaceURI() != null ) {
                    throw new ToolsException(
                            "A QName with a non-null namespace not supported as a serialization param: {"
                                    + qname.getNamespaceURI() + "}" + qname.getLocalPart());
                }
                buf.append(qname.getLocalPart());
                buf.append(" ");
            }
            props.setProperty(name, buf.toString());
        }
    }

    private final SequenceIterator myIt;
    private final XPathContext myCtxt;
}


/* ------------------------------------------------------------------------ */
/*  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS COMMENT.               */
/*                                                                          */
/*  The contents of this file are subject to the Mozilla Public License     */
/*  Version 1.0 (the "License"); you may not use this file except in        */
/*  compliance with the License. You may obtain a copy of the License at    */
/*  http://www.mozilla.org/MPL/.                                            */
/*                                                                          */
/*  Software distributed under the License is distributed on an "AS IS"     */
/*  basis, WITHOUT WARRANTY OF ANY KIND, either express or implied.  See    */
/*  the License for the specific language governing rights and limitations  */
/*  under the License.                                                      */
/*                                                                          */
/*  The Original Code is: all this file.                                    */
/*                                                                          */
/*  The Initial Developer of the Original Code is Florent Georges.          */
/*                                                                          */
/*  Contributor(s): none.                                                   */
/* ------------------------------------------------------------------------ */
