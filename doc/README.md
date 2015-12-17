# Saxon Tools documentation

Saxon Tools provides an implementation for Saxon of the Java Tools, as
well as extra tools specific to Saxon.  For the former, see the
documentation of the Java Tools.  For the latter, the facilities
include helpers to write extension functions for Saxon, in the Java
package `org.expath.tools.saxon.fun`.

You can see a real example of a library of extension functions,
implementing the [EXPath File Module](http://expath.org/spec/file)
specification, on Github, in the repository
[fgeorges/expath-file-java](https://github.com/fgeorges/expath-file-java/tree/master/file-saxon/src/org/expath/file/saxon).
The class `EXPathFileLibrary` in that directory is the library class.
The 3 sub-directories contain together the classes of the 32 function
in the library.

The goal of an extension function is simply to implement an XPath
function.  For instance, in XSLT, you could use the following
function, provided that a function `hello`, in the same namespace,
with 1 parameter, has been declared in Saxon:

```xsl
<xsl:sequence
   xmlns:my="http://example.org/ns/my"
   select="my:hello('world')"/>
```

## Library

The package introduces the concept of `Library`.  A library is a
collection of extension function, all in the same namespace (as well
as a set of error codes they can raise, all in the same namespace).
You can then register a library against the Saxon `Configuration`
object (if you use Saxon from the command line, instead of using it
from Java or .Net, see the section **Register a library** below for
alternatives).

A library provides 3 properties:

- a namespace URI
- a namespace prefix (used to create QNames, for example error codes)
- a set of functions

A library is a class extending `org.expath.tools.saxon.fun.Library`.
It provides the namespace URI and prefix in the constructor, and the
set of fuctions by implementing the method `functions()`.  A typical
example is:

```java
import org.expath.tools.ToolsException;
import org.expath.tools.saxon.fun.Function;
import org.expath.tools.saxon.fun.Library;

public class MyLib
        extends Library
{
    public MyLib()
    {
        super(URI, PREFIX);
    }

    @Override
    protected Function[] functions()
            throws ToolsException
    {
        return new Function[] {
            new Hello(this),
            new Greetings(this),
            new Bonjour(this)
        };
    }

    public static final String URI    = "http://example.org/ns/my";
    public static final String PREFIX = "my";
}
```

In this example, `Hello`, `Greetings` and `Bonjour` are three
functions.

## Functions

A function:

- is part of a library
- contains a definition
- provides an implementation

The library is an instance object of the class `Library` described
above.  The definiton is the "prototype" of the extension function:
its name, its return type, and its parameters names and types.  The
implementation is the (Java) function to be executed everytime the
(XPath) extension is called.

A function is a class extending `org.expath.tools.saxon.fun.Function`.
It provides the library in the constructor, the definition by
implementing `makeDefinition()`, and the implementation by
implementing the method `call()`.  A typical example is:

```java
import org.expath.tools.saxon.fun.Definition;
import org.expath.tools.saxon.fun.Function;
import org.expath.tools.saxon.fun.Library;

public class Hello
        extends Function
{
    public Hello(Library lib)
    {
        super(lib);
    }

    @Override
    protected Definition makeDefinition()
            throws ToolsException
    {
        // return a definition, see below
    }

    @Override
    public Sequence call(XPathContext ctxt, Sequence[] orig_params)
            throws XPathException
    {
        // implement the behaviour, see below
    }

    private static final String NAME = "hello";
}
```

## Definition

Providing a function definition is made easier by using a
`DefBuilder`.  You can get one by providing a function name to the
method `function()` on a library (and you can get a library by using
`library()` in a function object).

The `DefBuilder` let you provide each part of a definiton separatly,
by calling different functions.  Each function returns the builder
itself, so you can chain them, resulting in a declarative style code
in Java.

A definition can represent several functions, all with the same name,
but with different arities (that is, different number of parameters).
Some parameters are mandatory, and some might be optional.  A
definition with 2 mandatory parameters, and 2 optionals, declares 3
functions, taking resp. 2, 3 or 4 parameters.

The class `Types` provides convenient constants to represent different
sequence types for parameter and return types (e.g. "exactly one
string", or "zero or more text nodes").  It also provides helper
functions to build sequence types with element names (e.g. "one or
more elements named *para*").

A def builder is used to simplify the implementation of the method
`makeDefinition()` in a function:

```java
@Override
protected Definition makeDefinition()
        throws ToolsException
{
    return library()
            .function(this, NAME)
            .returns(Types.SINGLE_STRING)
            .param(Types.SINGLE_STRING, "first")
            .optional()
            .param(Types.SINGLE_STRING, "last")
            .make();
}
```

This example defines two functions, both with the same name.  They
both returns exactly one `xs:string`.  One takes one parameter called
`first`, a string.  The second function takes 2 parameters: `first` as
well, and `last`, also a string.

## The function implementation

The method `call()` of a function is the one executed when a function
is called in XPath.  Basically, that is where you get creative and
have the opportunity of getting the stuff done (the reason why you
write an extension in the first place is to implement that method).

This is the standard Saxon `call()` method of an integrated function,
since the `Function` interface extends the Saxon
`net.sf.saxon.lib.ExtensionFunctionCall`.  So it is called straight
from the Saxon processor, without any boiler plate from Saxon Tools.
So it receives the XPath context and the set of (XPath) parameters as
(Java) parameters, and return a value (in Java, which will be also the
result of the XPath function it implements).

Saxon Tools provides 2 different helpers to help you implement a
function:

- `Parameters` to check and extract parameters out of the set of raw
  Saxon XPath sequences provided, and map them to plain Java objects
- `Return` to build function return values out of plain Java objects

Typically, the beginning of `call()` will use `Parameters`, and the
end will use `Return`.  The stuff in between is your business logic.
The useful code you created an extension function for.

```java
@Override
public Sequence call(XPathContext ctxt, Sequence[] originalParams)
        throws XPathException
{
    // the params
    Parameters params = checkParams(originalParams);
    String first = params.asString(0, false);
    // the actual code
    String result;
    if ( params.number() = 1 ) {
        result = "Hello, " + first + "!";
    }
    else {
        String last = params.asString(1, true);
        result = "Hello, " + first + " " + last + "!";
    }
    // return an xs:string
    return Return.value(result);
}
```

The boiler plate code is kept to a minimum: extracting parameters
values, and returning values.

## Register a library

TODO: ...

### From Java

TODO: ...

### As an EXPath package

TODO: ...

### Using an Initializer

TODO: ...
