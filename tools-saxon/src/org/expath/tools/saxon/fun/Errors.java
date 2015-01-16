/****************************************************************************/
/*  File:       Errors.java                                                 */
/*  Author:     F. Georges - H2O Consulting                                 */
/*  Date:       2013-09-16                                                  */
/*  Tags:                                                                   */
/*      Copyright (c) 2013 Florent Georges (see end of file.)               */
/* ------------------------------------------------------------------------ */


package org.expath.tools.saxon.fun;

import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.trans.XPathException;

/**
 * Utilities for extension functions errors for Saxon.
 * 
 * This object is constructed with a Library, to get the namespace prefix and
 * URI of the function library.
 * 
 * This class gives the basic way to construct Saxon exception to represent
 * XPath errors, given a specific code.  It is strongly recommended that a
 * specific project creates its own error utility class, providing a method for
 * each error code it defines.  In order to centralize all codes in one place
 * and avoid having codes as strings all over the place.
 *
 * @author Florent Georges
 * @date   2013-09-16
 */
public class Errors
{
    public Errors(Library lib)
    {
        myNs = lib.getNamespace();
        myPrefix = lib.getPrefix();
    }

    /**
     * Make an XPath exception with {@code code} in the web:* namespace.
     */
    public XPathException make(String code, String msg)
    {
        XPathException  ex    = new XPathException(msg);
        StructuredQName qname = new StructuredQName(myPrefix, myNs, code);
        ex.setErrorCodeQName(qname);
        return ex;
    }

    /**
     * Make an XPath exception with {@code code} in the web:* namespace.
     */
    public XPathException make(String code, String msg, Throwable cause)
    {
        XPathException  ex    = new XPathException(msg, cause);
        StructuredQName qname = new StructuredQName(myPrefix, myNs, code);
        ex.setErrorCodeQName(qname);
        return ex;
    }

    private final String  myNs;
    private final String  myPrefix;
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
