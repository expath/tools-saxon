/****************************************************************************/
/*  File:       DefBuilder.java                                             */
/*  Author:     F. Georges                                                  */
/*  Company:    H2O Consulting                                              */
/*  Date:       2015-01-13                                                  */
/*  Tags:                                                                   */
/*      Copyright (c) 2015 Florent Georges (see end of file.)               */
/* ------------------------------------------------------------------------ */


package org.expath.tools.saxon.fun;

import java.util.ArrayList;
import java.util.List;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.value.SequenceType;
import org.expath.tools.ToolsException;

/**
 * The definition of a parameter of an extension function for Saxon.
 *
 * @author Florent Georges
 */
public class DefBuilder
{
    public DefBuilder(Function call, StructuredQName name, Library lib)
    {
        myCall = call;
        myName = name;
        myOptional = false;
        myParams = new ArrayList<Param>();
        myLib = lib;
    }

    public DefBuilder returns(SequenceType type)
    {
        myResult = type;
        return this;
    }

    public DefBuilder optional()
            throws ToolsException
    {
        if ( myOptional ) {
            throw new ToolsException("Cannot call optional() twice on the same DefBuilder");
        }
        myOptional = true;
        return this;
    }

    public DefBuilder param(SequenceType type, String name)
    {
        Param p = new Param(type, name, myOptional);
        myParams.add(p);
        return this;
    }

    public Definition make()
            throws ToolsException
    {
        if ( null == myResult ) {
            throw new ToolsException("Result type has not been set on DefBuilder");
        }
        Definition def = new Definition(myCall, myName, myResult, myParams.toArray(MARKER));
        myLib.addDef(myName, def);
        return def;
    }

    private final Function        myCall;
    private final StructuredQName myName;
    private final List<Param>     myParams;
    private final Library         myLib;
    private boolean      myOptional;
    private SequenceType myResult;
    private static final Param[] MARKER = new Param[0];
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
