/****************************************************************************/
/*  File:       Definition.java                                             */
/*  Author:     F. Georges                                                  */
/*  Company:    H2O Consulting                                              */
/*  Date:       2015-01-13                                                  */
/*  Tags:                                                                   */
/*      Copyright (c) 2015 Florent Georges (see end of file.)               */
/* ------------------------------------------------------------------------ */


package org.expath.tools.saxon.fun;

import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.value.SequenceType;
import org.expath.tools.ToolsException;

/**
 * The definition of an extension function for Saxon.
 *
 * @author Florent Georges
 */
public class Definition
        extends ExtensionFunctionDefinition
{
    public Definition(
                Function        call,
                StructuredQName name,
                SequenceType    result,
                Param...        params)
            throws ToolsException
    {
        myCall = call;
        myName = name;
        myResult = result;
        myParams = null == params ? new Param[0] : params;
        myMinArity = validateArity(myParams);
        myMaxArity = myParams.length;
        if ( myMinArity < 0 ) {
            throw new IllegalArgumentException("Minimum arity cannot be lower than 0: " + myMinArity);
        }
        if ( myMinArity > myMaxArity ) {
            throw new IllegalArgumentException(
                    "Minimum arity cannot be greater than maximum arity: "
                    + myMinArity + " > " + myMaxArity);
        }
        myParamTypes = new SequenceType[myParams.length];
        for ( int i = 0; i < myParams.length; ++i ) {
            myParamTypes[i] = myParams[i].type();
        }
    }

    private int validateArity(Param[] params)
            throws ToolsException
    {
        int optional = 0;
        for ( Param p : params ) {
            boolean opt = p.optional();
            if ( ! opt && optional > 0 ) {
                throw new ToolsException("Mandatory parameter after an optional one: " + p.name());
            }
            if ( opt ) {
                ++optional;
            }
        }
        return params.length - optional;
    }

    public Param[] params()
    {
        return myParams;
    }

    public Param param(int pos)
    {
        return myParams[pos];
    }

    @Override
    public StructuredQName getFunctionQName()
    {
        return myName;
    }

    @Override
    public int getMinimumNumberOfArguments()
    {
        return myMinArity;
    }

    @Override
    public int getMaximumNumberOfArguments()
    {
        return myMaxArity;
    }

    @Override
    public SequenceType[] getArgumentTypes()
    {
        return myParamTypes;
    }

    @Override
    public SequenceType getResultType(SequenceType[] types)
    {
        return myResult;
    }

    @Override
    public Function makeCallExpression()
    {
        return myCall;
    }

    private final Function        myCall;
    private final StructuredQName myName;
    private final SequenceType    myResult;
    private final Param[]         myParams;
    private final SequenceType[]  myParamTypes;
    private final int             myMinArity;
    private final int             myMaxArity;
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
