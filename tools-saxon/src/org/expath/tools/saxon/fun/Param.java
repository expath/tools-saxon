/****************************************************************************/
/*  File:       Definition.java                                             */
/*  Author:     F. Georges                                                  */
/*  Company:    H2O Consulting                                              */
/*  Date:       2015-01-13                                                  */
/*  Tags:                                                                   */
/*      Copyright (c) 2015 Florent Georges (see end of file.)               */
/* ------------------------------------------------------------------------ */


package org.expath.tools.saxon.fun;

import net.sf.saxon.value.SequenceType;

/**
 * The definition of a parameter of an extension function for Saxon.
 *
 * @author Florent Georges
 */
public class Param
{
    public Param(SequenceType type, String name, boolean optional)
    {
        myType = type;
        myName = name;
        myOptional = optional;
    }

    public SequenceType type()
    {
        return myType;
    }

    public String name()
    {
        return myName;
    }

    public boolean optional()
    {
        return myOptional;
    }

    private final SequenceType myType;
    private final String       myName;
    private final boolean      myOptional;
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
