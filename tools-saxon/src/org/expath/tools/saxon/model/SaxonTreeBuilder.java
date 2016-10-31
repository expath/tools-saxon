/****************************************************************************/
/*  File:       TreeBuilderHelper.java                                      */
/*  Author:     F. Georges - fgeorges.org                                   */
/*  Date:       2009-02-02                                                  */
/*  Tags:                                                                   */
/*      Copyright (c) 2009 Florent Georges (see end of file.)               */
/* ------------------------------------------------------------------------ */


package org.expath.tools.saxon.model;

import net.sf.saxon.event.Builder;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.om.*;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.type.BuiltInAtomicType;
import net.sf.saxon.type.Untyped;
import org.expath.tools.ToolsException;
import org.expath.tools.model.TreeBuilder;
import org.expath.tools.saxon.misc.VoidLocation;


/**
 * Implementation of {@link TreeBuilder} for Saxon.
 *
 * @author Florent Georges
 */
public class SaxonTreeBuilder
        implements TreeBuilder
{
    public SaxonTreeBuilder(XPathContext ctxt, String prefix, String ns)
            throws ToolsException
    {
        myBuilder = ctxt.getController().makeBuilder();
        myBuilder.open();
        myNs = ns;
        myPrefix = prefix;
    }

    /**
     * Provide the result in Saxon's object tools.
     */
    public NodeInfo getCurrentRoot()
            throws ToolsException
    {
        try {
            myBuilder.close();
        }
        catch ( XPathException ex ) {
            throw new ToolsException("Error closing the Saxon tree builder", ex);
        }
        return myBuilder.getCurrentRoot();
    }

    @Override
    public void startElem(String localname)
            throws ToolsException
    {
        NodeName name = new FingerprintedQName(myPrefix, myNs, localname);
        try {
            myBuilder.startElement(name, Untyped.getInstance(), VoidLocation.instance(), 0);
        }
        catch ( XPathException ex ) {
            throw new ToolsException("Error starting element on the Saxon tree builder", ex);
        }
    }

    @Override
    public void attribute(String localname, CharSequence value)
            throws ToolsException
    {
        if ( value != null ) {
            NodeName name = new NoNamespaceName(localname);
            try {
                myBuilder.attribute(name, BuiltInAtomicType.UNTYPED_ATOMIC, value, VoidLocation.instance(), 0);
            }
            catch ( XPathException ex ) {
                throw new ToolsException("Error creating attribute on the Saxon tree builder", ex);
            }
        }
    }

    @Override
    public void startContent()
            throws ToolsException
    {
        try {
            myBuilder.startContent();
        }
        catch ( XPathException ex ) {
            throw new ToolsException("Error starting content on the Saxon tree builder", ex);
        }
    }

    @Override
    public void endElem()
            throws ToolsException
    {
        try {
            myBuilder.endElement();
        }
        catch ( XPathException ex ) {
            throw new ToolsException("Error ending element on the Saxon tree builder", ex);
        }
    }

    private final Builder myBuilder;
    /** The namespace used for the elements. */
    private final String myNs;
    /** The prefix used for the elements. */
    private final String myPrefix;
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
