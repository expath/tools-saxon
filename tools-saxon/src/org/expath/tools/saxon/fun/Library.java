/****************************************************************************/
/*  File:       Library.java                                                */
/*  Author:     F. Georges                                                  */
/*  Company:    H2O Consulting                                              */
/*  Date:       2015-01-13                                                  */
/*  Tags:                                                                   */
/*      Copyright (c) 2015 Florent Georges (see end of file.)               */
/* ------------------------------------------------------------------------ */


package org.expath.tools.saxon.fun;

import java.util.HashMap;
import java.util.Map;
import net.sf.saxon.Configuration;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.SequenceType;
import org.expath.tools.ToolsException;

/**
 * A library of extension functions for Saxon.
 *
 * @author Florent Georges
 */
public abstract class Library
{
    protected abstract Function[] functions()
            throws ToolsException;

    public Library(String ns, String prefix)
    {
        myNs = ns;
        myPrefix = prefix;
        myDefs = new HashMap<>();
        myErrors = new Errors(this);
    }

    public String getNamespace()
    {
        return myNs;
    }

    public String getPrefix()
    {
        return myPrefix;
    }

    public Errors getErrors()
    {
        return myErrors;
    }

    public DefBuilder function(Function call, String local_name)
            throws ToolsException
    {
        StructuredQName name = new StructuredQName(myPrefix, myNs, local_name);
        return function(call, name);
    }

    public DefBuilder function(Function call, StructuredQName name)
            throws ToolsException
    {
        return new DefBuilder(call, name, this);
    }

    public Definition function(
                Function     call,
                String       local_name,
                SequenceType result,
                Param...     params)
            throws ToolsException
    {
        StructuredQName name = new StructuredQName(myPrefix, myNs, local_name);
        return function(call, name, result, params);
    }

    public Definition function(
                Function        call,
                StructuredQName name,
                SequenceType    result,
                Param...        params)
            throws ToolsException
    {
        Definition def = new Definition(call, name, result, params);
        addDef(name, def);
        return def;
    }

    public void register(Configuration config)
            throws ToolsException
    {
        for ( Function f : functions() ) {
            Definition def = f.definition();
            config.registerExtensionFunction(def);
        }
    }

    void addDef(StructuredQName name, Definition def)
            throws ToolsException
    {
        if ( myDefs.containsKey(name) ) {
            throw new ToolsException("Library already contains function: " + name);
        }
        myDefs.put(name, def);
    }

    public XPathException error(String code, String msg)
    {
        return myErrors.make(code, msg);
    }

    public XPathException error(String code, String msg, Throwable ex)
    {
        return myErrors.make(code, msg, ex);
    }

    private final String myNs;
    private final String myPrefix;
    private final Map<StructuredQName, Definition> myDefs;
    private final Errors myErrors;
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
