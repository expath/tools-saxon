/****************************************************************************/
/*  File:       Return.java                                                 */
/*  Author:     F. Georges - H2O Consulting                                 */
/*  Date:       2013-12-22                                                  */
/*  Tags:                                                                   */
/*      Copyright (c) 2013 Florent Georges (see end of file.)               */
/* ------------------------------------------------------------------------ */


package org.expath.tools.saxon.fun;

import java.math.BigInteger;
import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import net.sf.saxon.om.AtomicArray;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XdmValue;
import net.sf.saxon.value.AnyURIValue;
import net.sf.saxon.value.AtomicValue;
import net.sf.saxon.value.Base64BinaryValue;
import net.sf.saxon.value.BigIntegerValue;
import net.sf.saxon.value.BooleanValue;
import net.sf.saxon.value.DateTimeValue;
import net.sf.saxon.value.DateValue;
import net.sf.saxon.value.EmptySequence;
import net.sf.saxon.value.Int64Value;
import net.sf.saxon.value.StringValue;

/**
 * Utilities for return value of extension functions for Saxon.
 * 
 * TODO: Instead of {@code value()} functions, defining for each of them both
 * {@code required()} and {@code optional()} could be better, to handle
 * differently {@code null}s (in the former it is an error, in the latter it
 * returns {@code empty()}).
 *
 * @author Florent Georges
 * @date   2013-12-22
 */
public class Return
{
    public static Sequence empty()
    {
        return EmptySequence.getInstance();
    }

    public static Sequence value(XdmNode node) {
        return node(node);
    }
    public static Sequence value(XdmValue value) {
        return item(value);
    }
    public static Sequence value(byte[] bytes) {
        return binary(bytes);
    }
    public static Sequence value(Boolean b) {
        return bool(b);
    }
    public static Sequence value(String string) {
        return string(string);
    }
    public static Sequence value(Iterable<String> strings) {
        return stringList(strings);
    }
    public static Sequence value(Date date) {
        return dateTime(date, false);
    }
    public static Sequence value(Date date, boolean tz) {
        return dateTime(date, tz);
    }
    public static Sequence value(Calendar dt) {
        return dateTime(dt, false);
    }
    public static Sequence value(Calendar dt, boolean tz) {
        return dateTime(dt, tz);
    }
    public static Sequence value(Long i) {
        return integer(i);
    }
    public static Sequence value(BigInteger i) {
        return integer(i);
    }
    public static Sequence value(URI uri) {
        return uri(uri);
    }

    public static Sequence node(XdmNode node)
    {
        if ( node == null ) {
            return empty();
        }
        return node.getUnderlyingNode();
    }

    public static Sequence item(XdmValue value)
    {
        if ( value == null ) {
            return empty();
        }
        return value.getUnderlyingValue();
    }

    public static Sequence binary(byte[] bytes)
    {
        if ( bytes == null ) {
            return empty();
        }
        return new Base64BinaryValue(bytes);
    }

    public static Sequence bool(Boolean b)
    {
        if ( b == null ) {
            return empty();
        }
        else if ( b ) {
            return BooleanValue.TRUE;
        }
        else {
            return BooleanValue.FALSE;
        }
    }

    public static Sequence string(String string)
    {
        if ( string == null ) {
            return empty();
        }
        return new StringValue(string);
    }

    public static Sequence stringList(Iterable<String> strings)
    {
        if ( strings == null ) {
            return empty();
        }
        List<AtomicValue> items = new ArrayList<>();
        for ( String s : strings ) {
            StringValue v = new StringValue(s);
            items.add(v);
        }
        if ( items.isEmpty() ) {
            return empty();
        }
        return new AtomicArray(items);
    }

    public static Sequence dateTime(Date date, boolean tz)
    {
        if ( date == null ) {
            return empty();
        }
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        return dateTime(cal, tz);
    }

    public static Sequence dateTime(Calendar dt, boolean tz)
    {
        if ( dt == null ) {
            return empty();
        }
        return new DateTimeValue(dt, tz);
    }

    public static Sequence date(Date date)
    {
        if ( date == null ) {
            return empty();
        }
        return new DateValue(
                date.getYear(),
                (byte) date.getMonth(),
                (byte) date.getDay());
    }

    public static Sequence date(Calendar date, boolean tz)
    {
        if ( date == null ) {
            return empty();
        }
        return new DateValue(
                date.get(Calendar.YEAR),
                (byte) date.get(Calendar.MONTH),
                (byte) date.get(Calendar.DAY_OF_MONTH));
    }

    public static Sequence integer(Long i)
    {
        if ( i == null ) {
            return empty();
        }
        return new Int64Value(i);
    }

    public static Sequence integer(BigInteger i)
    {
        if ( i == null ) {
            return empty();
        }
        return new BigIntegerValue(i);
    }

    public static Sequence uri(URI uri)
    {
        if ( uri == null ) {
            return empty();
        }
        return new AnyURIValue(uri.toASCIIString());
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
