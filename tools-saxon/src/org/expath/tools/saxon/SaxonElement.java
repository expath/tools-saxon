/****************************************************************************/
/*  File:       SaxonElement.java                                           */
/*  Author:     F. Georges - H2O Consulting                                 */
/*  Date:       2011-03-10                                                  */
/*  Tags:                                                                   */
/*      Copyright (c) 2011 Florent Georges (see end of file.)               */
/* ------------------------------------------------------------------------ */


package org.expath.tools.saxon;

import java.util.Arrays;
import java.util.Iterator;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.om.AxisInfo;
import net.sf.saxon.om.NamePool;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.pattern.NameTest;
import net.sf.saxon.pattern.NamespaceTest;
import net.sf.saxon.pattern.NodeKindTest;
import net.sf.saxon.pattern.NodeTest;
import net.sf.saxon.tree.iter.AxisIterator;
import net.sf.saxon.type.Type;
import org.expath.tools.Attribute;
import org.expath.tools.Element;
import org.expath.tools.ToolsException;
import org.expath.tools.Sequence;

/**
 * Saxon implementation of {@link Element}, relying on {@link NodeInfo}.
 *
 * @author Florent Georges
 * @date   2011-03-10
 */
public class SaxonElement
        implements Element
{
    public SaxonElement(NodeInfo node, XPathContext ctxt)
            throws ToolsException
    {
        if ( node == null ) {
            throw new ToolsException("the node is null");
        }
        if ( node.getNodeKind() != Type.ELEMENT ) {
            throw new ToolsException("the node is not an element");
        }
        myNode = node;
        myCtxt = ctxt;
    }

    @Override
    public String getLocalName()
    {
        return myNode.getLocalPart();
    }

    @Override
    public String getNamespaceUri()
    {
        return myNode.getURI();
    }

    @Override
    public String getDisplayName()
    {
        return myNode.getDisplayName();
    }

    @Override
    public String getAttribute(String local_name)
    {
        // get the attribute
        NamePool pool = myNode.getNamePool();
        NodeTest pred = new NameTest(Type.ATTRIBUTE, "", local_name, pool);
        AxisIterator attrs = myNode.iterateAxis(AxisInfo.ATTRIBUTE, pred);
        NodeInfo a = (NodeInfo) attrs.next();
        // return its string value, or null if there is no such attribute
        if ( a == null ) {
            return null;
        }
        else {
            return a.getStringValue();
        }
    }

    @Override
    public Iterable<Attribute> attributes()
    {
        AxisIterator it = myNode.iterateAxis(AxisInfo.ATTRIBUTE);
        return new AttributeIterable(it);
    }

    @Override
    public boolean hasNoNsChild()
    {
        NamePool pool = myNode.getNamePool();
        NodeTest no_ns_pred = new NamespaceTest(pool, Type.ELEMENT, "");
        NodeInfo next = myNode.iterateAxis(AxisInfo.CHILD, no_ns_pred).next();
        return next != null;
    }

    @Override
    public void noOtherNCNameAttribute(String[] names, String[] forbidden_ns)
            throws ToolsException
    {
        if ( names == null ) {
            throw new NullPointerException("the names array is null");
        }
        if ( forbidden_ns == null ) {
            forbidden_ns = new String[] { };
        }
        String[] sorted_names = sortCopy(names);
        String[] sorted_ns = sortCopy(forbidden_ns);
        String elem_name = myNode.getDisplayName();
        AxisIterator it = myNode.iterateAxis(AxisInfo.ATTRIBUTE);
        NodeInfo attr;
        while ( (attr = (NodeInfo) it.next()) != null ) {
            String attr_name = attr.getDisplayName();
            String ns = attr.getURI();
            if ( Arrays.binarySearch(sorted_ns, ns) >= 0 ) {
                throw new ToolsException("@" + attr_name + " in namespace " + ns + " not allowed on " + elem_name);
            }
            else if ( ! "".equals(ns) ) {
                // ignore other-namespace-attributes
            }
            else if ( Arrays.binarySearch(sorted_names, attr.getLocalPart()) < 0 ) {
                throw new ToolsException("@" + attr_name + " not allowed on " + elem_name);
            }
        }
    }

    private String[] sortCopy(String[] array)
    {
        String[] sorted = new String[array.length];
        for ( int i = 0; i < array.length; ++i ) {
            sorted[i] = array[i];
        }
        Arrays.sort(sorted);
        return sorted;
    }

    @Override
    public Sequence getContent()
    {
        SequenceIterator it = myNode.iterateAxis(AxisInfo.CHILD);
        return new SaxonSequence(it, myCtxt);
    }

    @Override
    public Iterable<Element> children()
    {
        AxisIterator it = myNode.iterateAxis(AxisInfo.CHILD, NodeKindTest.ELEMENT);
        return new ElemIterable(it);
    }

    @Override
    public Iterable<Element> children(String ns)
    {
        NamePool pool = myNode.getNamePool();
        NodeTest pred = new NamespaceTest(pool, Type.ELEMENT, ns);
        AxisIterator it = myNode.iterateAxis(AxisInfo.CHILD, pred);
        return new ElemIterable(it);
    }

    private NodeInfo myNode;
    private XPathContext myCtxt;

    private static class AttributeIterable
            implements Iterable<Attribute>
    {
        public AttributeIterable(AxisIterator it)
        {
            myIter = new AttributeIteratorWrapper(it);
        }

        @Override
        public Iterator<Attribute> iterator()
        {
            return myIter;
        }

        private final Iterator myIter;
    }

    private static class AttributeIteratorWrapper
            implements Iterator<Attribute>
    {
        public AttributeIteratorWrapper(AxisIterator it)
        {
            myIter = it;
            myNext = (NodeInfo) it.next();
        }

        @Override
        public boolean hasNext()
        {
            return myNext != null;
        }

        @Override
        public Attribute next()
        {
            if ( myNext == null ) {
                // TODO: Throw an exception instead?
                return null;
            }
            Attribute a = new SaxonAttribute(myNext);
            myNext = (NodeInfo) myIter.next();
            return a;
        }

        @Override
        public void remove()
        {
            throw new UnsupportedOperationException("remove() is not supported");
        }

        private final AxisIterator myIter;
        private NodeInfo myNext;
    }

    private class ElemIterable
            implements Iterable<Element>
    {
        public ElemIterable(AxisIterator it)
        {
            myIter = new ElemIteratorWrapper(it);
        }

        @Override
        public Iterator<Element> iterator()
        {
            return myIter;
        }

        private final Iterator myIter;
    }

    private class ElemIteratorWrapper
            implements Iterator<Element>
    {
        public ElemIteratorWrapper(AxisIterator it)
        {
            myIter = it;
            myNext = (NodeInfo) it.next();
        }

        @Override
        public boolean hasNext()
        {
            return myNext != null;
        }

        @Override
        public Element next()
        {
            if ( myNext == null ) {
                // TODO: Throw an exception instead?
                return null;
            }
            Element e;
            try {
                e = new SaxonElement(myNext, myCtxt);
            }
            catch ( ToolsException ex ) {
                // because we're implementing the Iterator interface, we don't
                // have the choice but to throw a runtime exception, but we know
                // by construction this is not possible to arrive here (we've
                // just check nullness above, and we iterate only on elements,
                // those are the only two reasons the constructor can throw an
                // exception)
                throw new RuntimeException("[cannot happen] error building the saxon element", ex);
            }
            myNext = (NodeInfo) myIter.next();
            return e;
        }

        @Override
        public void remove()
        {
            throw new UnsupportedOperationException("remove() is not supported");
        }

        private final AxisIterator myIter;
        private NodeInfo myNext;
    }
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
