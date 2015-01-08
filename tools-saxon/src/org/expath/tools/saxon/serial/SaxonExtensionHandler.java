/****************************************************************************/
/*  File:       SaxonExtensionHandler.java                                  */
/*  Author:     F. Georges                                                  */
/*  Company:    H2O Consulting                                              */
/*  Date:       2015-01-08                                                  */
/*  Tags:                                                                   */
/*      Copyright (c) 2015 Florent Georges (see end of file.)               */
/* ------------------------------------------------------------------------ */


package org.expath.tools.saxon.serial;

import javax.xml.namespace.QName;
import org.expath.tools.ToolsException;
import org.expath.tools.serial.ExtensionHandler;

/**
 * Handler for extension serialization parameters.
 * 
 * @author Florent Georges
 * @date   2015-01-08
 */
public class SaxonExtensionHandler
        implements ExtensionHandler
{
    @Override
    public String getExtension(QName name)
            throws ToolsException
    {
        if ( name.equals(ATTRIBUTE_ORDER) ) {
            return myAttributeOrder;
        }
        else if ( name.equals(CHARACTER_REPRESENTATION) ) {
            return myCharacterRepresentation;
        }
        else if ( name.equals(DOUBLE_SPACE) ) {
            return myDoubleSpace;
        }
        else if ( name.equals(IMPLICIT_RESULT_DOCUMENT) ) {
            return myImplicitResultDocument;
        }
        else if ( name.equals(INDENT_SPACES) ) {
            return myIndentSpaces;
        }
        else if ( name.equals(LINE_LENGTH) ) {
            return myLineLength;
        }
        else if ( name.equals(NEXT_IN_CHAIN) ) {
            return myNextInChain;
        }
        else if ( name.equals(NEXT_IN_CHAIN_BASE_URI) ) {
            return myNextInChainBaseUri;
        }
        else if ( name.equals(RECOGNIZE_BINARY) ) {
            return myRecognizeBinary;
        }
        else if ( name.equals(REQUIRE_WELL_FORMED) ) {
            return myRequireWellFormed;
        }
        else if ( name.equals(STYLESHEET_VERSION) ) {
            return myStylesheetVersion;
        }
        else if ( name.equals(SUPPLY_SOURCE_LOCATOR) ) {
            return mySupplySourceLocator;
        }
        else if ( name.equals(SUPPRESS_INDENTATION) ) {
            return mySuppressIndentation;
        }
        else if ( name.equals(WRAP) ) {
            return myWrap;
        }
        else {
            throw new ToolsException("Unknown Saxon extension serialization parameter: " + name);
        }
    }

    @Override
    public void setExtension(QName name, String value)
            throws ToolsException
    {
        if ( name.equals(ATTRIBUTE_ORDER) ) {
            myAttributeOrder = value;
        }
        else if ( name.equals(CHARACTER_REPRESENTATION) ) {
            myCharacterRepresentation = value;
        }
        else if ( name.equals(DOUBLE_SPACE) ) {
            myDoubleSpace = value;
        }
        else if ( name.equals(IMPLICIT_RESULT_DOCUMENT) ) {
            myImplicitResultDocument = value;
        }
        else if ( name.equals(INDENT_SPACES) ) {
            myIndentSpaces = value;
        }
        else if ( name.equals(LINE_LENGTH) ) {
            myLineLength = value;
        }
        else if ( name.equals(NEXT_IN_CHAIN) ) {
            myNextInChain = value;
        }
        else if ( name.equals(NEXT_IN_CHAIN_BASE_URI) ) {
            myNextInChainBaseUri = value;
        }
        else if ( name.equals(RECOGNIZE_BINARY) ) {
            myRecognizeBinary = value;
        }
        else if ( name.equals(REQUIRE_WELL_FORMED) ) {
            myRequireWellFormed = value;
        }
        else if ( name.equals(STYLESHEET_VERSION) ) {
            myStylesheetVersion = value;
        }
        else if ( name.equals(SUPPLY_SOURCE_LOCATOR) ) {
            mySupplySourceLocator = value;
        }
        else if ( name.equals(SUPPRESS_INDENTATION) ) {
            mySuppressIndentation = value;
        }
        else if ( name.equals(WRAP) ) {
            myWrap = value;
        }
        else {
            throw new ToolsException("Unknown Saxon extension serialization parameter: " + name);
        }
    }

    private String myAttributeOrder;
    private String myCharacterRepresentation;
    private String myDoubleSpace;
    private String myImplicitResultDocument;
    private String myIndentSpaces;
    private String myLineLength;
    private String myNextInChain;
    private String myNextInChainBaseUri;
    private String myRecognizeBinary;
    private String myRequireWellFormed;
    private String myStylesheetVersion;
    private String mySupplySourceLocator;
    private String mySuppressIndentation;
    private String myWrap;

    /**
     * The Saxon namespace (to declare extension output properties on web:body).
     * 
     * @see http://saxonica.com/documentation/#!extensions/output-extras
     */
    private static final String NS = "http://saxon.sf.net/";

    private static final QName ATTRIBUTE_ORDER          = new QName(NS, "saxon:attribute-order");
    private static final QName CHARACTER_REPRESENTATION = new QName(NS, "saxon:character-representation");
    private static final QName DOUBLE_SPACE             = new QName(NS, "saxon:double-space");
    private static final QName IMPLICIT_RESULT_DOCUMENT = new QName(NS, "saxon:implicit-result-document");
    private static final QName INDENT_SPACES            = new QName(NS, "saxon:indent-spaces");
    private static final QName LINE_LENGTH              = new QName(NS, "saxon:line-length");
    private static final QName NEXT_IN_CHAIN            = new QName(NS, "saxon:next-in-chain");
    private static final QName NEXT_IN_CHAIN_BASE_URI   = new QName(NS, "saxon:next-in-chain-base-uri");
    private static final QName RECOGNIZE_BINARY         = new QName(NS, "saxon:recognize-binary");
    private static final QName REQUIRE_WELL_FORMED      = new QName(NS, "saxon:require-well-formed");
    private static final QName STYLESHEET_VERSION       = new QName(NS, "saxon:stylesheet-version");
    private static final QName SUPPLY_SOURCE_LOCATOR    = new QName(NS, "saxon:supply-source-locator");
    private static final QName SUPPRESS_INDENTATION     = new QName(NS, "saxon:suppress-indentation");
    private static final QName WRAP                     = new QName(NS, "saxon:wrap"); 
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
