# ![](https://raw.githubusercontent.com/nextlets/erc/master/docs/erc.png) Easy REST client for JAVA

This project was conceived as easy and lightweight alternative
to the existing REST frameworks such as Spring, also the project
can be used for educational purposes.

You can download, use, modify this code without any restrictions.

-------------------

NOTE: the project uses Apache HTTP client and Google GSON.

-------------------
Author: https://www.linkedin.com/in/yurishadrin, ymshadrin@gmail.com

# Architecture design
## Main API
Easy REST Client (ERC) uses factory for creating REST Services. Each service is defined as annotated interface, the implementation of service interface is not needed. Such approach is very popular for many annotation-oriented implementations of various services and ERC is not an exclusion.
## Annotations
ERC uses the following list of annotations:
* @ERCEndpoint
* @ERCParam
* @ERClient
* @ERCHeader
* @ERCObject

Actually, only first two are used by ERC core always, the remaining three - are optional and dedicated to resolve non-standard situations.

What do they mean?

**@ERCEndpoint** contains properties:
* endpoint - **mandatory**, defines endpoint of method
* method - optional, GET by default, specifies HTTP method
* deserializer - optional, contains class of response deserializer
* errorHandler - optional, contains class of error handler
* httpInterceptor - optional, contains class of HTTP interceptor

@ERCEndpoint is needed for service methods annotating, this annotation requires **endpoint**, the endpoint means path in URL which is used to call server method. Endpoint can be simple (like `/1/2/3`) and complex (like `/1/{path-param}/3?query-param={query-param}`), for example:

```java
@ERCEndpoint(endpoint="/echo")
String echo();
```
@ERCEndpoint also provides possibility to specify HTTP method (if method is not specified, HTTP GET is used):
* GET
* POST
* PUT
* DELETE

For example:
```java
@ERCEndpoint(endpoint="/echo", method=ERCHttpMethod.POST)
String echo();
```
Because @ERCEndpoint is used for method annotations; a method, as a rule, returns a value, this annotation can redefine response **deserializer** instead of default implementation.

Also it is possible to redefine default error handler and HTTP interceptor (mode details about handlers see below).

**@ERCParam** contains properties:
* name - **mandatory**, defines name of HTTP parameter or name of {macro}
* required - optional, `true` by default, forbids or allows `null` value
* serializer - optional, contains class of parameter serializer

@ERCParam marks method parameters for HTTP request, this annotation requires definition of **name**, name means name of HTTP parameter. Usually name of method parameter and name in HTTP request are equal, but it is not a rule. For example:
```java
// http://host:port/echo?echo=<value>
@ERCEndpoint(endpoint="/echo")
String echo(@ERCParam(name="echo") String echo);
```
or:
```java
// http://host:port/echo/<value>
@ERCEndpoint(endpoint="/echo/{echo}")
String echo(@ERCParam(name="echo") String echo);
```
or:
```java
// http://host:port/echo?echo-string=<value>
@ERCEndpoint(endpoint="/echo?echo-string={echo}")
String echo(@ERCParam(name="echo") String echo);
```
or the same:
```java
// http://host:port/echo?echo-string=<value>
@ERCEndpoint(endpoint="/echo")
String echo(@ERCParam(name="echo-string") String echo);
```

By default all parameters annotated with @ERCParam are mandatory (i.e. cannot be a `null`), but, you can always allow to pass `null` values using **required** annotation property.

Each HTTP parameter should be serialized, usually default implementation of parameters serializer is used but, @ERCParam allows to redefine default implementation with any custom **serializer** if it is needed.

**@ERClient** contains properties:
* serializer - optional, contains class of parameters serializer
* deserializer - optional, contains class of response deserializer
* errorHandler - optional, contains class of error handler
* httpInterceptor - optional, contains class of HTTP interceptor

@ERClient - it is for service class (sorry, - interface). So, if you want to redefine one or more of ERC handlers for a whole service, you can use @ERClient one time instead of define handlers everywhere in @ERCEndpoint or/and @ERCParam. Can be useful for heavy-customized systems.

**@ERCHeader** contains properties:
* name - **mandatory**, defines name of HTTP header.

@ERCHeader annotation provide possibility to use HTTP headers as method parameters, for example:

```java
// Content-Type: <value>
// http://host:port/echo/<value>
@ERCEndpoint(endpoint="/echo/{echo}")
String echo(@ERCParam(name="echo") String echo, @ERCHeader(name="Content-Type") String contentType);
```
**@ERCObject** contains properties:
* serializer - optional, contains class of parameters serializer
* deserializer - optional, contains class of response deserializer

@ERCObject provides yet another way to serialize / deserialize objects. So, if any object (parameter or response) marked with @ERCObject, ERC will use serializer / deserializer which is defined inside. Of course, for request parameters only serializer is used, for method response - deserializer.

## Parameters and headers
ERC provides two ways to pass parameters and headers to HTTP request:
* using annotations
* using special collections

First way assumes that each parameter or header is annotated with the corresponding annotation.
Second way means all (or part) parameters or headers are passed inside collections:
* ERCParams - for parameters
* ERCHeaders - for headers

Because these collections are not annotated, it is impossible to redefine names, serializers for each particular parameter or header inside collections. But, in case amount of parameters or headers is too large (method will have too many parameters), or set of of parameters or headers is not defined in advance, such approach can be useful.

ERC supports mix of annotated and collected parameters and headers. But, you should be careful with names - conflicts are not tracked.

## Cookies
ERC supports cookies management as:
* passing `java.net.HttpCookie` as service method parameter
* passing `java.net.CookieManager` as service method parameter

## Handlers
ERC uses handlers for:
* parameters serialization
* response deserialization
* errors handling
* HTTP intercepting

ERC has default implementation of all handlers but, as mentioned above, handlers can be redefined using annotations.

**Parameters serialization**

Interface: `org.nextlets.erc.handler.ERCParameterSerializer`
Default implementation: `org.nextlets.erc.defaults.handler.ERCParameterSerializerJsonImpl`

**Response deserialization**

Interface: `org.nextlets.erc.handler.ERCResultDeserializer`
Default implementation: `org.nextlets.erc.defaults.handler.ERCResultDeserializerJsonImpl`

**Errors handling**

Interface: `org.nextlets.erc.handler.ERCHttpErrorHandler`
Default implementation: `org.nextlets.erc.defaults.handler.ERCHttpErrorHandlerDefaultImpl`

Error handler is invoked only if status code of HTTP response `>= 400`. Default implementation of error handler throws `ERCHttpResponseException`.

**HTTP intercepting**

Interface: `org.nextlets.erc.handler.ERCHttpInterceptor`
Default implementation: `org.nextlets.erc.defaults.handler.ERCHttpInterceptorDefaultImpl`

HTTP interceptor interface has two methods: `before` and `after`, and, accordingly, interceptor is called before and after HTTP request. The purpose of HTTP interceptor is to have possibility to perform some actions immediately before and after request.

Default implementation of HTTP interceptor does nothing.

**Lifecycle of handlers**
Handler is instantiated only once, after instantiation handler is places to a static storage to reuse. 

**Rules of handlers determination**
Some handlers (especially for serializers / deserializers) can be redefined with different annotations. Regarding this the following rules are used to determine what a handler will be used:
* **High priority:** if parameter or response is annotated with @ERCObject - handler is determined.
* **Medium priority:** otherwise, if handler redefined in @ERCEndpoint (for response) or @ERCParam (for parameter) - handler is determined.
* **Low priority:** otherwise, if handler redefined in @ERClient - handler is determined.
* **Default behavior:** otherwise default handler is used.

**Custom handlers best practices**
* Do not use stateful handlers!
* Handler can be instantiated only with empty constructor.
* Try to avoid definition of handler as inner class especially in cases, if you want to have access to fields / methods of outer class. It is related to the feature that instance of inner class can be created only if exists instance of outer class, but ERC has not possibility to get it.

## Exceptions
ERC has a root exception: `ERCException extends RuntimeException`.
All ERC exceptions are divided by two parts:
* Client exceptions
* Server exceptions

Client exceptions can be thrown in case any error occurred on client side. For example: parameter cannot be serialized, response cannot be deserialized, connection cannot be established, handler cannot be instantiated, etc.

Server exception means something wrong on server side. For example: server returned 500 error code.

![](https://raw.githubusercontent.com/nextlets/erc/master/docs/exceptions.png)

## Authentication
ERC contains one authenticator in a box, this authentication provides basic HTTP authentication. Authenticator is activated by factory method: `ERCFactory.getInstance(String serviceUrl, String user, String password)`. 

If you need to have custom authenticator, you have to override `ERCDefaultFactoryImpl.createHttpAuthenticator()` and extend `ERCFactory` with a new method, which will instantiate your factory implementation.

# Examples
**Common usage**
```java
// Service interface
interface EchoService {
    @ERCEndpoint(endpoint="/echo/{echo}")
    String echoPath(@ERCParam(name="echo") String echo);

    @ERCEndpoint(endpoint="/echo")
    String echoParam(@ERCParam(name="echo") String echo);
}
// Usage
    EchoService service = ERCFactory.getInstance("http://host:port");
    System.out.println(service.echoPath("Hi you!"));
    System.out.println(service.echoParam("Hi you!"));
```
**Basic authentication**
```java
    EchoService service = 
        ERCFactory.getInstance("http://host:port", "user", "password");
    System.out.println(service.echo("Hi you!"));
```
**Method POST**
```java
interface EchoService {
    @ERCEndpoint(endpoint="/echo/{echo}", method=ERCHttpMethod.POST)
    String echo(@ERCParam(name="echo") String echo);
}
```

**Mix of parameters**
```java
// Service interface
interface EchoService {
    @ERCEndpoint(endpoint="/echo/{echo}")
    String echo(@ERCParam(name="echo", ) String echo, ERCParams params);
}
// Usage
    EchoService service = ERCFactory.getInstance("http://host:port");
    ERCParams params = new ERCParams();
    params.put("name", "John");
    params.put("lastName", "Smith");
    service.echoPath("Hi you!", params);
// In request:
    http://host:port/echo/Hi+you!?name=John&lastName=Smith
```
