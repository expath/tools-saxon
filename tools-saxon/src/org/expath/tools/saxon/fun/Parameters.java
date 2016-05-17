/****************************************************************************/
/*  File:       Parameters.java                                             */
/*  Author:     F. Georges - H2O Consulting                                 */
/*  Date:       2013-08-22                                                  */
/*  Tags:                                                                   */
/*      Copyright (c) 2013 Florent Georges (see end of file.)               */
/* ------------------------------------------------------------------------ */


package org.expath.tools.saxon.fun;

import java.util.ArrayList;
import java.util.List;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.om.Item;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.type.Type;
import net.sf.saxon.value.Base64BinaryValue;
import net.sf.saxon.value.BooleanValue;
import net.sf.saxon.value.IntegerValue;
import net.sf.saxon.value.StringValue;
import org.expath.tools.ToolsException;
import org.expath.tools.model.Element;
import org.expath.tools.saxon.model.SaxonElement;
import org.expath.tools.saxon.model.SaxonSequence;

/**
 * Utilities for extension functions parameters for Saxon.
 *
 * @author Florent Georges
 */
public class Parameters
{
    /**
     * Check the number of parameters in params, and throw an error if not OK.
     * 
     * @param lib The extension library.
     * @param params The parameter list.
     * @param min The minimal number of parameters.
     * @param formals  The formal parameters.
     * @throws XPathException If there is not exactly {@code num} parameters.
     */
    public Parameters(Library lib, Sequence[] params, int min, Param[] formals)
            throws XPathException
    {
        int max = formals.length;
        if ( params.length < min || params.length > max ) {
            if ( min == max ) {
                throw new XPathException("There is not exactly " + min + " params: " + params.length);
            }
            else {
                throw new XPathException("There is not between " + min + " and " + max + " params: " + params.length);
            }
        }
        myParams = params;
        myFormals = formals;
        myLib = lib;
    }

    /**
     * Return the number of parameters.
     * 
     * @return The number of parameters.
     */
    public int number()
    {
        return myParams.length;
    }

    /**
     * Return the pos-th parameter, checking it is a string.
     * 
     * If optional is false and the parameter is the empty sequence, an
     * {@code XPathException} is thrown.  As well as if there is more than
     * one item.
     * 
     * @param pos The position of the parameter to analyze, 0-based.
     * @param optional Can the parameter be the empty sequence?
     * @throws XPathException If there is not such parameter or if it is not a string.
     * @return The parameter as a string.
     */
    public String asString(int pos, boolean optional)
            throws XPathException
    {
        Item item = asItem(pos, optional);
        if ( item == null ) {
            return null;
        }
        if ( ! ( item instanceof StringValue ) ) {
            throw new XPathException("The param $" + myFormals[pos].name() + " is not a string");
        }
        return item.getStringValue();
    }

    /**
     * Return the pos-th parameter, checking it is a boolean.
     * 
     * If optional is false and the parameter is the empty sequence, an
     * {@code XPathException} is thrown.  As well as if there is more than
     * one item.
     * 
     * @param pos The position of the parameter to analyze, 0-based.
     * @param optional Can the parameter be the empty sequence?
     * @throws XPathException If there is not such parameter or if it is not a boolean.
     * @return The parameter as a boolean.
     */
    public Boolean asBoolean(int pos, boolean optional)
            throws XPathException
    {
        Item item = asItem(pos, optional);
        if ( item == null ) {
            return null;
        }
        if ( ! ( item instanceof BooleanValue ) ) {
            throw new XPathException("The param $" + myFormals[pos].name() + " is not a boolean");
        }
        BooleanValue v = (BooleanValue) item;
        return v.getBooleanValue();
    }

    /**
     * Return the pos-th parameter, checking it is a string.
     * 
     * If optional is false and the parameter is the empty sequence, an
     * {@code XPathException} is thrown.  As well as if there is more than
     * one item.
     * 
     * @param pos The position of the parameter to analyze, 0-based.
     * @param optional Can the parameter be the empty sequence?
     * @throws XPathException If there is not such parameter or if it is not a long.
     * @return The parameter as a long.
     */
    public Long asLong(int pos, boolean optional)
            throws XPathException
    {
        Item item = asItem(pos, optional);
        if ( item == null ) {
            return null;
        }
        if ( ! ( item instanceof IntegerValue ) ) {
            throw new XPathException("The param $" + myFormals[pos].name() + " is not an integer");
        }
        IntegerValue v = (IntegerValue) item;
        return v.longValue();
    }

    /**
     * Return the pos-th parameter, checking it is a list of strings.
     * 
     * If optional is false and the parameter is the empty sequence, an
     * {@code XPathException} is thrown.
     * 
     * @param pos The position of the parameter to analyze, 0-based.
     * @param optional Can the parameter be the empty sequence?
     * @throws XPathException If there is not such parameter or if it is not a
     *      string sequence.
     * @return The parameter as a string sequence.
     */
    public List<String> asStringList(int pos, boolean optional)
            throws XPathException
    {
        SequenceIterator it = initiate(pos, optional);
        if ( null == it ) {
            return null;
        }
        List<String> result = new ArrayList<>();
        Item item = it.next();
        while ( item != null ) {
            if ( ! ( item instanceof StringValue ) ) {
                throw new XPathException("Some value in the param $" + myFormals[pos].name() + " is not a string");
            }
            String value = item.getStringValue();
            result.add(value);
            item = it.next();
        }
        return result;
    }

    /**
     * Return the pos-th parameter, checking it is a base64 binary.
     * 
     * If optional is false and the parameter is the empty sequence, an
     * {@code XPathException} is thrown.  As well as if there is more than
     * one item.
     * 
     * @param pos The position of the parameter to analyze, 0-based.
     * @param optional Can the parameter be the empty sequence?
     * @throws XPathException If there is not such parameter or if it is not a binary.
     * @return The parameter as a binary.
     */
    public byte[] asBinary(int pos, boolean optional)
            throws XPathException
    {
        Item item = asItem(pos, optional);
        if ( item == null ) {
            return null;
        }
        if ( ! ( item instanceof Base64BinaryValue ) ) {
            throw new XPathException("The param $" + myFormals[pos].name() + " is not a base64 binary");
        }
        Base64BinaryValue bin = (Base64BinaryValue) item;
        return bin.getBinaryValue();
    }

    /**
     * Return the pos-th parameter, checking it is an element node.
     * 
     * If optional is false and the parameter is the empty sequence, an
     * {@code XPathException} is thrown.  As well as if there is more than
     * one item, or if the param is not an element node.
     * 
     * @param pos The position of the parameter to analyze, 0-based.
     * @param optional Can the parameter be the empty sequence?
     * @param ctxt The context in which this extension call occurs.
     * @throws XPathException If there is not such parameter or if it is not an element.
     * @return The parameter as an element.
     */
    public Element asElement(int pos, boolean optional, XPathContext ctxt)
            throws XPathException
    {
        Item item = asItem(pos, optional);
        if ( item == null ) {
            return null;
        }
        if ( ! ( item instanceof NodeInfo ) ) {
            throw new XPathException("The param $" + myFormals[pos].name() + " is not a node");
        }
        NodeInfo node = (NodeInfo) item;
        int kind = node.getNodeKind();
        if ( kind != Type.ELEMENT ) {
            throw new XPathException("The param $" + myFormals[pos].name() + " is not an element (kind: " + kind + ")");
        }
        try {
            return new SaxonElement(node, ctxt);
        }
        catch ( ToolsException ex ) {
            throw new XPathException("Error creating an EXPath Saxon element", ex);
        }
    }

    /**
     * Return the pos-th parameter, checking it is an element node, as well as its name.
     * 
     * If optional is false and the parameter is the empty sequence, an
     * {@code XPathException} is thrown.  As well as if there is more than
     * one item, or if the param is not an element node, or if the element
     * name is not equal to {@code name} (in the webapp namespace).
     * 
     * @param pos The position of the parameter to analyze, 0-based.
     * @param optional Can the parameter be the empty sequence?
     * @param ctxt The context in which this extension call occurs.
     * @param name The required local name of the element.
     * @throws XPathException If there is not such parameter or if it is not an element.
     * @return The parameter as an element.
     */
    public Element asElement(int pos, boolean optional, XPathContext ctxt, String name)
            throws XPathException
    {
        Element elem = asElement(pos, optional, ctxt);
        String actual_name = elem.getLocalName();
        if ( ! actual_name.equals(name) ) {
            String msg = "The param $" + myFormals[pos].name() + " element local name is: "
                    + actual_name + ", instead of: " + name;
            throw new XPathException(msg);
        }
        String actual_ns = elem.getNamespaceUri();
        if ( ! actual_ns.equals(myLib.getNamespace()) ) {
            String msg = "The param $" + myFormals[pos].name() + " element namespace is: "
                    + actual_ns + ", instead of: " + myLib.getNamespace();
            throw new XPathException(msg);
        }
        return elem;
    }

    /**
     * Return the pos-th parameter, as a sequence.
     * 
     * If optional is false and the parameter is the empty sequence, an
     * {@code XPathException} is thrown.
     * 
     * @param pos The position of the parameter to analyze, 0-based.
     * @param optional Can the parameter be the empty sequence?
     * @param ctxt The context in which this extension call occurs.
     * @throws XPathException If there is not such parameter or if it is not an element.
     * @return The parameter as a sequence (never return null).
     */
    public org.expath.tools.model.Sequence asSequence(int pos, boolean optional, XPathContext ctxt)
            throws XPathException
    {
        SequenceIterator it = initiate(pos, optional);
        return new SaxonSequence(it, ctxt);
    }

    /**
     * Return the pos-th parameter, checking its arity.
     * 
     * If optional is false and the parameter is the empty sequence, an
     * {@code XPathException} is thrown.  As well as if there is more than
     * one item.
     * 
     * @param pos The position of the parameter to analyze, 0-based.
     * @param optional Can the parameter be the empty sequence?
     */
    private Item asItem(int pos, boolean optional)
            throws XPathException
    {
        SequenceIterator it = initiate(pos, optional);
        if ( null == it ) {
            return null;
        }
        Item item = it.next();
        if ( it.next() != null ) {
            throw new XPathException("The param $" + myFormals[pos].name() + " sequence has more than one item");
        }
        return item;
    }

    /**
     * Initiate retrieving the pos-th parameter, checking its arity.
     * 
     * If optional is false and the parameter is the empty sequence, an
     * {@code XPathException} is thrown.
     * 
     * @param pos The position of the parameter to analyze, 0-based.
     * @param optional Can the parameter be the empty sequence?
     * @throws XPathException If any error occurs.
     * @return An iterator pointing at the first item in the pos-th parameter,
     *      or null in case of empty sequence (unless {@code optional is false,
     *      in which case an exception is thrown instead).
     */
    private SequenceIterator initiate(int pos, boolean optional)
            throws XPathException
    {
        if ( pos < 0 || pos >= number() ) {
            throw new XPathException("Asked for the " + ordinal(pos) + " param of " + number());
        }
        Sequence param = myParams[pos];
        SequenceIterator it = param.iterate();
        SequenceIterator res = it.getAnother();
        if ( it.next() == null ) {
            if ( optional ) {
                return null;
            }
            throw new XPathException("The param $" + myFormals[pos].name() + " is an empty sequence");
        }
        return res;
    }

    private String ordinal(int pos)
            throws XPathException
    {
        if ( pos == 0 ) {
            return "1st";
        }
        else if ( pos == 1 ) {
            return "2d";
        }
        else if ( pos == 2 ) {
            return "3d";
        }
        else if ( pos > 2 ) {
            return (pos + 1) + "th";
        }
        else {
            throw new XPathException("pos must be 0 or above, and is: " + pos);
        }
    }

    public Formatter format(String name)
    {
        return new Formatter(name, myParams.length, myFormals.length);
    }

    public class Formatter
    {
        public Formatter(String name, int num, int max)
        {
            myNum = num;
            myMax = max;
            myI   = 0;
            myBuf = new StringBuilder("Calling ");
            myBuf.append(myLib.getPrefix());
            myBuf.append(":");
            myBuf.append(name);
            myBuf.append("(");
        }

        public Formatter param(String value)
            throws XPathException
        {
            if ( checkPos() ) {
                if ( value == null ) {
                    myBuf.append("()");
                }
                else {
                    myBuf.append("'");
                    myBuf.append(value.replace("'", "''"));
                    myBuf.append("'");
                }
            }
            return this;
        }

        // TODO: Adapt specifically for element nodes?
        public Formatter param(Item item)
            throws XPathException
        {
            if ( checkPos() ) {
                if ( item == null ) {
                    myBuf.append("()");
                }
                else {
                    myBuf.append(item.toString());
                }
            }
            return this;
        }

        public Formatter param(byte[] value)
            throws XPathException
        {
            if ( checkPos() ) {
                if ( value == null ) {
                    myBuf.append("()");
                }
                else {
                    myBuf.append("#<TODO: binary: ");
                    myBuf.append(value);
                    myBuf.append(">");
                }
            }
            return this;
        }

        public Formatter param(Sequence value)
            throws XPathException
        {
            if ( checkPos() ) {
                if ( value == null ) {
                    myBuf.append("()");
                }
                else {
                    myBuf.append("#<TODO: sequence: ");
                    myBuf.append(value);
                    myBuf.append(">");
                }
            }
            return this;
        }

        public Formatter param(List<String> value)
            throws XPathException
        {
            if ( checkPos() ) {
                myBuf.append("(");
                if ( value != null ) {
                    boolean first = true;
                    for ( String v : value ) {
                        if ( ! first ) {
                            myBuf.append(",");
                        }
                        myBuf.append("'");
                        myBuf.append(v.replace("'", "''"));
                        myBuf.append("'");
                        first = false;
                    }
                }
                myBuf.append(")");
            }
            return this;
        }

        public String value()
        {
            myBuf.append(")");
            return myBuf.toString();
        }

        /**
         * Return true if the value must be output.
         */
        private boolean checkPos()
            throws XPathException
        {
            ++myI;
            if ( myI > myMax ) {
                throw new XPathException("too much params: " + ordinal(myI) + ", max: " + myMax);
            }
            boolean doit = myI <= myNum;
            if ( doit && myI > 1 ) {
                myBuf.append(", ");
            }
            return doit;
        }

        private final StringBuilder myBuf;
        private final int myNum;
        private final int myMax;
        private int myI;
    }

    private Sequence[] myParams;
    private Param[] myFormals;
    private final Library myLib;
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
