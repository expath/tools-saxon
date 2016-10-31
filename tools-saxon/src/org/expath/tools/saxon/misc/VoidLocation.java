/****************************************************************************/
/*  File:       TreeBuilderHelper.java                                      */
/*  Author:     F. Georges - fgeorges.org                                   */
/*  Date:       2016-10-28                                                  */
/*  Tags:                                                                   */
/*      Copyright (c) 2016 Florent Georges (see end of file.)               */
/* ------------------------------------------------------------------------ */


package org.expath.tools.saxon.misc;

import net.sf.saxon.expr.parser.Location;


/**
 * Implementation of {@link Location} with no info.
 *
 * @author Florent Georges
 */
public class VoidLocation
        implements Location
{
    static public VoidLocation instance()
    {
        return INSTANCE;
    }

    public int getColumnNumber()
    {
        return -1;
    }

    public int getLineNumber()
    {
        return -1;
    }

    public String getPublicId()
    {
        return null;
    }

    public String getSystemId()
    {
        return null;
    }

    public Location saveLocation()
    {
        return this;
    }

    private VoidLocation()
    {
    }

    private static final VoidLocation INSTANCE = new VoidLocation();
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
