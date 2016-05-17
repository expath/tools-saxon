/****************************************************************************/
/*  File:       Function.java                                               */
/*  Author:     F. Georges                                                  */
/*  Company:    H2O Consulting                                              */
/*  Date:       2015-01-13                                                  */
/*  Tags:                                                                   */
/*      Copyright (c) 2015 Florent Georges (see end of file.)               */
/* ------------------------------------------------------------------------ */


package org.expath.tools.saxon.fun;

import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.trans.XPathException;
import org.expath.tools.ToolsException;

/**
 * An extension function for Saxon.
 *
 * @author Florent Georges
 */
public abstract class Function
        extends ExtensionFunctionCall
{
    protected abstract Definition makeDefinition()
            throws ToolsException;

    public Function(Library lib)
    {
        myLib = lib;
    }

    public Library library()
    {
        return myLib;
    }

    public Definition definition()
            throws ToolsException
    {
        if ( null == myDef ) {
            myDef = makeDefinition();
        }
        return myDef;
    }

    public Parameters checkParams(Sequence[] params)
            throws XPathException
    {
        try {
            Definition def = definition();
            return new Parameters(myLib, params, def.getMinimumNumberOfArguments(), def.params());
        }
        catch ( ToolsException ex ) {
            throw new XPathException("Error instantiating the function definition", ex);
        }
    }

    private final Library myLib;
    private Definition myDef = null;
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
