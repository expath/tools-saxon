/****************************************************************************/
/*  File:       Types.java                                                  */
/*  Author:     F. Georges - H2O Consulting                                 */
/*  Date:       2013-09-11                                                  */
/*  Tags:                                                                   */
/*      Copyright (c) 2013 Florent Georges (see end of file.)               */
/* ------------------------------------------------------------------------ */


package org.expath.tools.saxon.fun;

import net.sf.saxon.expr.StaticProperty;
import net.sf.saxon.om.NamePool;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.pattern.AnyNodeTest;
import net.sf.saxon.pattern.NameTest;
import net.sf.saxon.pattern.NodeKindTest;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.type.AnyItemType;
import net.sf.saxon.type.BuiltInAtomicType;
import net.sf.saxon.type.ItemType;
import net.sf.saxon.type.Type;
import net.sf.saxon.value.SequenceType;

/**
 * Utilities for extension functions types for Saxon.
 *
 * @author Florent Georges
 * @date   2013-09-11
 */
public class Types
{
    // exactly one (no type podifier in XPath)
    public static final int SINGLE   = StaticProperty.EXACTLY_ONE;
    // zero or one ("?" in XPath)
    public static final int OPTIONAL = StaticProperty.ALLOWS_ZERO_OR_ONE;
    // zero or more ("*" in XPath)
    public static final int ANY      = StaticProperty.ALLOWS_ZERO_OR_MORE;
    // one or more ("+" in XPath)
    public static final int SEVERAL  = StaticProperty.ALLOWS_ONE_OR_MORE;

    // item()
    public static final ItemType ITEM     = AnyItemType.getInstance();
    // built-in atomic types
    public static final ItemType BASE64   = BuiltInAtomicType.BASE64_BINARY;
    public static final ItemType BOOLEAN  = BuiltInAtomicType.BOOLEAN;
    public static final ItemType BYTE     = BuiltInAtomicType.BYTE;
    public static final ItemType DATE     = BuiltInAtomicType.DATE;
    public static final ItemType DATETIME = BuiltInAtomicType.DATE_TIME;
    public static final ItemType DECIMAL  = BuiltInAtomicType.DECIMAL;
    public static final ItemType DOUBLE   = BuiltInAtomicType.DOUBLE;
    public static final ItemType FLOAT    = BuiltInAtomicType.FLOAT;
    public static final ItemType INT      = BuiltInAtomicType.INT;
    public static final ItemType INTEGER  = BuiltInAtomicType.INTEGER;
    public static final ItemType LONG     = BuiltInAtomicType.LONG;
    public static final ItemType SHORT    = BuiltInAtomicType.SHORT;
    public static final ItemType STRING   = BuiltInAtomicType.STRING;
    public static final ItemType URI      = BuiltInAtomicType.ANY_URI;
    // nodes
    public static final ItemType NODE      = AnyNodeTest.getInstance();
    public static final ItemType ATTRIBUTE = NodeKindTest.ATTRIBUTE;
    public static final ItemType COMMENT   = NodeKindTest.COMMENT;
    public static final ItemType DOCUMENT  = NodeKindTest.DOCUMENT;
    public static final ItemType ELEMENT   = NodeKindTest.ELEMENT;
    public static final ItemType NAMESPACE = NodeKindTest.NAMESPACE;
    public static final ItemType PI        = NodeKindTest.PROCESSING_INSTRUCTION;
    public static final ItemType TEXT      = NodeKindTest.TEXT;

    // the empty sequence
    public static final SequenceType EMPTY_SEQUENCE = SequenceType.EMPTY_SEQUENCE;

    // singles
    public static final SequenceType SINGLE_ITEM     = SequenceType.SINGLE_ITEM;
    // atomic types
    public static final SequenceType SINGLE_BASE64   = make(SINGLE, BASE64);
    public static final SequenceType SINGLE_BOOLEAN  = SequenceType.SINGLE_BOOLEAN;
    public static final SequenceType SINGLE_BYTE     = SequenceType.SINGLE_BYTE;
    public static final SequenceType SINGLE_DATE     = make(SINGLE, DATE);
    public static final SequenceType SINGLE_DATETIME = make(SINGLE, DATETIME);
    public static final SequenceType SINGLE_DECIMAL  = make(SINGLE, DECIMAL);
    public static final SequenceType SINGLE_DOUBLE   = SequenceType.SINGLE_DOUBLE;
    public static final SequenceType SINGLE_FLOAT    = SequenceType.SINGLE_FLOAT;
    public static final SequenceType SINGLE_INT      = SequenceType.SINGLE_INT;
    public static final SequenceType SINGLE_INTEGER  = SequenceType.SINGLE_INTEGER;
    public static final SequenceType SINGLE_LONG     = SequenceType.SINGLE_LONG;
    public static final SequenceType SINGLE_SHORT    = SequenceType.SINGLE_SHORT;
    public static final SequenceType SINGLE_STRING   = SequenceType.SINGLE_STRING;
    public static final SequenceType SINGLE_URI      = SequenceType.SINGLE_ANY_URI;
    // nodes
    public static final SequenceType SINGLE_NODE      = SequenceType.SINGLE_NODE;
    public static final SequenceType SINGLE_ATTRIBUTE = make(SINGLE, ATTRIBUTE);
    public static final SequenceType SINGLE_COMMENT   = make(SINGLE, COMMENT);
    public static final SequenceType SINGLE_DOCUMENT  = make(SINGLE, DOCUMENT);
    public static final SequenceType SINGLE_ELEMENT   = SequenceType.SINGLE_ELEMENT_NODE;
    public static final SequenceType SINGLE_NAMESPACE = make(SINGLE, NAMESPACE);
    public static final SequenceType SINGLE_PI        = make(SINGLE, PI);
    public static final SequenceType SINGLE_TEXT      = make(SINGLE, TEXT);

    // optionals
    public static final SequenceType OPTIONAL_ITEM     = SequenceType.OPTIONAL_ITEM;
    // atomic types
    public static final SequenceType OPTIONAL_BASE64   = make(OPTIONAL, BASE64);
    public static final SequenceType OPTIONAL_BOOLEAN  = SequenceType.OPTIONAL_BOOLEAN;
    public static final SequenceType OPTIONAL_BYTE     = SequenceType.OPTIONAL_BYTE;
    public static final SequenceType OPTIONAL_DATE     = make(OPTIONAL, DATE);
    public static final SequenceType OPTIONAL_DATETIME = SequenceType.OPTIONAL_DATE_TIME;
    public static final SequenceType OPTIONAL_DECIMAL  = SequenceType.OPTIONAL_DECIMAL;
    public static final SequenceType OPTIONAL_DOUBLE   = SequenceType.OPTIONAL_DOUBLE;
    public static final SequenceType OPTIONAL_FLOAT    = SequenceType.OPTIONAL_FLOAT;
    public static final SequenceType OPTIONAL_INT      = SequenceType.OPTIONAL_INT;
    public static final SequenceType OPTIONAL_INTEGER  = SequenceType.OPTIONAL_INTEGER;
    public static final SequenceType OPTIONAL_LONG     = SequenceType.OPTIONAL_LONG;
    public static final SequenceType OPTIONAL_SHORT    = SequenceType.OPTIONAL_SHORT;
    public static final SequenceType OPTIONAL_STRING   = SequenceType.OPTIONAL_STRING;
    public static final SequenceType OPTIONAL_URI      = SequenceType.OPTIONAL_ANY_URI;
    // nodes
    public static final SequenceType OPTIONAL_NODE      = SequenceType.OPTIONAL_NODE;
    public static final SequenceType OPTIONAL_ATTRIBUTE = SequenceType.OPTIONAL_ATTRIBUTE_NODE;
    public static final SequenceType OPTIONAL_COMMENT   = make(OPTIONAL, COMMENT);
    public static final SequenceType OPTIONAL_DOCUMENT  = SequenceType.OPTIONAL_DOCUMENT_NODE;
    public static final SequenceType OPTIONAL_ELEMENT   = SequenceType.OPTIONAL_ELEMENT_NODE;
    public static final SequenceType OPTIONAL_NAMESPACE = make(OPTIONAL, NAMESPACE);
    public static final SequenceType OPTIONAL_PI        = make(OPTIONAL, PI);
    public static final SequenceType OPTIONAL_TEXT      = make(OPTIONAL, TEXT);

    // anys
    public static final SequenceType ANY_ITEM     = SequenceType.ANY_SEQUENCE;
    // atomic types
    public static final SequenceType ANY_BASE64   = SequenceType.BASE64_BINARY_SEQUENCE;
    public static final SequenceType ANY_BOOLEAN  = make(ANY, BOOLEAN);
    public static final SequenceType ANY_BYTE     = make(ANY, BYTE);
    public static final SequenceType ANY_DATE     = make(ANY, DATE);
    public static final SequenceType ANY_DATETIME = make(ANY, DATETIME);
    public static final SequenceType ANY_DECIMAL  = make(ANY, DECIMAL);
    public static final SequenceType ANY_DOUBLE   = make(ANY, DOUBLE);
    public static final SequenceType ANY_FLOAT    = make(ANY, FLOAT);
    public static final SequenceType ANY_INT      = make(ANY, INT);
    public static final SequenceType ANY_INTEGER  = make(ANY, INTEGER);
    public static final SequenceType ANY_LONG     = make(ANY, LONG);
    public static final SequenceType ANY_SHORT    = make(ANY, SHORT);
    public static final SequenceType ANY_STRING   = SequenceType.STRING_SEQUENCE;
    public static final SequenceType ANY_URI      = make(ANY, URI);
    // nodes
    public static final SequenceType ANY_NODE      = SequenceType.NODE_SEQUENCE;
    public static final SequenceType ANY_ATTRIBUTE = make(SINGLE, ATTRIBUTE);
    public static final SequenceType ANY_COMMENT   = make(SINGLE, COMMENT);
    public static final SequenceType ANY_DOCUMENT  = make(SINGLE, DOCUMENT);
    public static final SequenceType ANY_ELEMENT   = SequenceType.SINGLE_ELEMENT_NODE;
    public static final SequenceType ANY_NAMESPACE = make(SINGLE, NAMESPACE);
    public static final SequenceType ANY_PI        = make(SINGLE, PI);
    public static final SequenceType ANY_TEXT      = make(SINGLE, TEXT);

    // severals
    public static final SequenceType SEVERAL_ITEM     = SequenceType.NON_EMPTY_SEQUENCE;
    // atomic types
    public static final SequenceType SEVERAL_BASE64   = make(SEVERAL, BASE64);
    public static final SequenceType SEVERAL_BOOLEAN  = make(SEVERAL, BOOLEAN);
    public static final SequenceType SEVERAL_BYTE     = make(SEVERAL, BYTE);
    public static final SequenceType SEVERAL_DATE     = make(SEVERAL, DATE);
    public static final SequenceType SEVERAL_DATETIME = make(SEVERAL, DATETIME);
    public static final SequenceType SEVERAL_DECIMAL  = make(SEVERAL, DECIMAL);
    public static final SequenceType SEVERAL_DOUBLE   = make(SEVERAL, DOUBLE);
    public static final SequenceType SEVERAL_FLOAT    = make(SEVERAL, FLOAT);
    public static final SequenceType SEVERAL_INT      = make(SEVERAL, INT);
    public static final SequenceType SEVERAL_INTEGER  = make(SEVERAL, INTEGER);
    public static final SequenceType SEVERAL_LONG     = make(SEVERAL, LONG);
    public static final SequenceType SEVERAL_SHORT    = make(SEVERAL, SHORT);
    public static final SequenceType SEVERAL_STRING   = make(SEVERAL, STRING);
    public static final SequenceType SEVERAL_URI      = make(SEVERAL, URI);
    // nodes
    public static final SequenceType SEVERAL_NODE      = make(SEVERAL, NODE);
    public static final SequenceType SEVERAL_ATTRIBUTE = make(SEVERAL, ATTRIBUTE);
    public static final SequenceType SEVERAL_COMMENT   = make(SEVERAL, COMMENT);
    public static final SequenceType SEVERAL_DOCUMENT  = make(SEVERAL, DOCUMENT);
    public static final SequenceType SEVERAL_ELEMENT   = make(SEVERAL, ELEMENT);
    public static final SequenceType SEVERAL_NAMESPACE = make(SEVERAL, NAMESPACE);
    public static final SequenceType SEVERAL_PI        = make(SEVERAL, PI);
    public static final SequenceType SEVERAL_TEXT      = make(SEVERAL, TEXT);

    public Types(Library lib)
    {
        myLib = lib;
    }

    /**
     * Create a list of types.
     */
    public static SequenceType[] types(SequenceType... args)
    {
        return args;
    }

    /**
     * Create a QName in the namespace of the library of this object.
     */
    public StructuredQName qname(String local)
    {
        final String uri    = myLib.getNamespace();
        final String prefix = myLib.getPrefix();
        return new StructuredQName(prefix, uri, local);
    }

    /**
     * Create an "element(ns:xxx)" type, in the namespace of the library of this object.
     */
    public SequenceType singleElement(String local, Processor saxon)
    {
        return element(SINGLE, local, saxon);
    }

    /**
     * Create an "element(ns:xxx)?" type, in the namespace of the library of this object.
     */
    public SequenceType optionalElement(String local, Processor saxon)
    {
        return element(OPTIONAL, local, saxon);
    }

    /**
     * Create an "element(ns:xxx)*" type, in the namespace of the library of this object.
     */
    public SequenceType anyElement(String local, Processor saxon)
    {
        return element(ANY, local, saxon);
    }

    /**
     * Create an "element(ns:xxx)+" type, in the namespace of the library of this object.
     */
    public SequenceType severalElement(String local, Processor saxon)
    {
        return element(SEVERAL, local, saxon);
    }

    private SequenceType element(int occurrence, String local, Processor saxon)
    {
        final int      kind   = Type.ELEMENT;
        final String   uri    = myLib.getNamespace();
        final NamePool pool   = saxon.getUnderlyingConfiguration().getNamePool();
        final ItemType itype  = new NameTest(kind, uri, local, pool);
        return SequenceType.makeSequenceType(itype, occurrence);
    }

    private static SequenceType make(int occurrence, ItemType type)
    {
        return SequenceType.makeSequenceType(type, occurrence);
    }

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
