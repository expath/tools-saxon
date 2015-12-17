tools-saxon
===========

Implementation of the tools for Saxon.

It is mainly two things now: the implementation of the data model from
tools-java, and additional tools to ease writing extension functions
for Saxon.

This README file contains an introduction to the tools provided here.
For a more complete documentation, see [doc/](doc/).

Function tools
--------------

A good example of the API for extension functions is the project `file-saxon`,
implementing the EXPath [File module](http://expath.org/spec/file), in the
repository [expath-file-java](https://github.com/fgeorges/expath-file-java).

Let us have a look for instance at the class
[Exists](https://github.com/fgeorges/expath-file-java/blob/master/file-saxon/src/org/expath/file/saxon/props/Exists.java):

```java
public class Exists
        extends Function
{
    public Exists(Library lib)
    {
        super(lib);
    }

    @Override
    protected Definition makeDefinition()
            throws ToolsException
    {
        return library()
                .function(this, LOCAL_NAME)
                .returns(Types.SINGLE_BOOLEAN)
                .param(Types.SINGLE_STRING, PARAM_PATH)
                .make();
    }

    @Override
    public Sequence call(XPathContext ctxt, Sequence[] orig_params)
            throws XPathException
    {
        // the params
        Parameters params = checkParams(orig_params);
        String path = params.asString(0, false);
        // the actual call
        Properties props = new Properties();
        boolean res = props.exists(path);
        return Return.value(res);
    }

    private static final String LOCAL_NAME = "exists";
    private static final String PARAM_PATH = "path";
}
```

We can see interesting points:

- an extension function extends the class [Function](https://github.com/expath/tools-saxon/blob/master/tools-saxon/src/org/expath/tools/saxon/fun/Function.java)
- its constructor takes a [Library](https://github.com/expath/tools-saxon/blob/master/tools-saxon/src/org/expath/tools/saxon/fun/Library.java) object
- the library is available through the `library()` method
- it implements one method to return the function definition (describing its signature)
- it implements a second method to actually implement the function behaviour
- it uses several tools to deal with type descriptions, type conversions, parameters, etc.

**Type description**

The class
[Types](https://github.com/expath/tools-saxon/blob/master/tools-saxon/src/org/expath/tools/saxon/fun/Types.java)
contains static variables for all item types and sequence types, using a
consistent naming scheme.  For instance `xs:integer+` is `SEVERAL_INTEGER`, and
`element()?` is `OPTIONAL_ELEMENT`.

It also contains methods to construct more complex item types like
`element(name)`, which would be: `singleElement("name", saxon)` (requires the
Saxon processor object).

**Function definition**

The function definition is described through a utility class,
[Definition](https://github.com/expath/tools-saxon/blob/master/tools-saxon/src/org/expath/tools/saxon/fun/Definition.java).
This class implements the Saxon's abstract class for function definitions,
`ExtensionFunctionDefinition`, based on values passed to its constructor.  But
you never use it directly, you construct a definition by using a builder object,
itself instantiated by the method `Library.function()`.  You add the local name,
return type, parameters, and then ask for the definition object.

Given the following example function (yes, there are 3 different functions, all
with the same name, with different arities, which Saxon represents with the same
function definition object):

```
my:hello() as xs:string
my:hello($name as xs:string) as xs:string
my:hello($name as xs:string, $lang as xs:string*) as xs:string
```

This function can be describe by the following code (assuming you already have a
library object for this library, with the namespace URI and prefix):

```java
library()
    .function(this, "hello")
    .returns(Types.SINGLE_STRING)
    .optional()
    .param(Types.SINGLE_STRING, "name")
    .param(Types.ANY_STRING, "lang")
    .make();
```

**Parameters**

The class
[Parameters](https://github.com/expath/tools-saxon/blob/master/tools-saxon/src/org/expath/tools/saxon/fun/Parameters.java)
provides helpers for dealing with Saxon's raw parameters.  In particular, it
helps accessing one specific parameter as a generic Java type, given its
positional number.  It reports the parameter name in the error message in case
of an error.  For instance (assuming the parameter is not optional):

```java
String path = params.asString(0, false);
```

**Return values**

The class
[Return](https://github.com/expath/tools-saxon/blob/master/tools-saxon/src/org/expath/tools/saxon/fun/Return.java)
is like the class
[Parameters](https://github.com/expath/tools-saxon/blob/master/tools-saxon/src/org/expath/tools/saxon/fun/Parameters.java),
but the other way around: it returns a Saxon sequence object from a java generic
object.  For instance, this will return an `xs:boolean`:

```java
return Return.value(true);
```

**Library**

A library represents a set of functions in the same namespace.  It contains a
namespace URI, a namespace prefix, a set of functions and a way to create Saxon
XPath errors (based on code names, typically bound to exceptions).  The namespace
prefix is used every time a function or error name has to be presented to the
user, as in error messages.  It looks typically like this (look at this example,
[EXPathFileLibrary](https://github.com/fgeorges/expath-file-java/blob/master/file-saxon/src/org/expath/file/saxon/EXPathFileLibrary.java)):

```java
public class SomeLibrary
        extends Library
{
    public SomeLibrary()
    {
        super(NS_URI, NS_PREFIX);
    }

    @Override
    protected Function[] functions()
            throws ToolsException
    {
        return new Function[] {
            new SomeFunction(this),
            new AnotherFunction(this),
            new YetAnotherOne(this)
        };
    }

    public XPathException error(SomeException ex)
    {
        switch ( ex.getType() ) {
            case NOT_FOUND:
                return error(ERR_NOT_FOUND, ex.getMessage(), ex);
            case FOOBAR:
                return error(ERR_FOOBAR, ex.getMessage(), ex);
            default:
                return error(ERR_DEFAULT, ex.getMessage(), ex);
        }
    }

    public static final String NS_URI    = "http://example.org/ns/my-library";
    public static final String NS_PREFIX = "my";

    // error codes, my:not-found, my:foobar and my:default-error
    private static final String ERR_NOT_FOUND = "not-found";
    private static final String ERR_FOOBAR    = "foobar";
    private static final String ERR_DEFAULT   = "default-error";
}
```

Given a library object, all the functions it contains can be registered against
a Saxon `Configuration` object.  This object is available regardless of the
Saxon API you use.  For instance, using S9API's `Processor`:

```java
// the Saxon objects
Processor saxon = ...;
Configuration config = saxon.getUnderlyingConfiguration();
// the library
Library lib = new SomeLibrary();
lib.register(config);
```
