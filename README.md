# Cirrus
Cirrus is a lightweight scala library which provides a http client for making http requests and consuming REST APIs

### Prologue
In quiet a few scala projects I've worked, I've needed a HTTP client for interacting with REST APIs. In the scala community there are only a handful of HTTP clients which are available for interacting with Cloud-based APIs and among them are the HTTP clients provided by the Play and the Spray Frameworks and Dispatch but none come without their disadvantages.

The Play Framework provides the [Play-WS] Client library which comes with bits and pieces of the Play Stack, the Spray Framework provides the [Spray Client] which requires an instance of an implicit ActorSystem within scope and Dispatch provides cryptic hieroglyph DSL for making simple HTTP requests.

In an effort to make a simpler interface for making HTTP requests and consuming REST APIs which expose data in JSON format I created Cirrus. It provides the building blocks of HTTP Requests, Responses and Clients and then implements most of the common use cases with simple Scala case classes. Cirrus has no external dependencies on Third-party libraries and simply uses the HTTPURLConnection class (which comes as part of the Java JDK) for making HTTP requests.

### Contents
1. Architecture
    - Requests, Responses and Clients
2. Making HTTP Requests
    - Creating a Basic Request
    - Creating a Basic Client
    - Retrieving a Basic Response
    - Extracting the contents of a Response
    - Passing HTTP Headers
    - Passing Query Parameters
3. Simplifying HTTP Requests
    - Back to the classics: HEAD, GET, PUT, POST, DELETE
    - Passing HTTP Headers
    - Passing Query Parameters
4. Working with JSON
    - Spray HTTP
    - Argonaut HTTP
    - Play HTTP
5. Advanced Usage
    - Working with Cirrus
    - Making a custom Basic Client
    - HTTPS requests
    - Proxies
    - For-Comprehensions
6. Inspirations
    - Databinder-Dispatch

#### Architecture

##### Requests, Responses and Clients
In Cirrus, a HTTP request is modelled as a trait with five major parts; a method, an address, headers, parameters and a body (optionally). The request body is optional because not all HTTP requests send a message body as part of the request message (GET and HEAD requests for example).

```
trait Request {

  def method: String
  def address: String
  def headers: List[(String, String)]
  def params: List[(String, String)]
  def body: Option[String]
}
```

A HTTP response is modelled in a simpler way with three major parts: a status code, headers and a body. Unlike the a HTTP request, a HTTP response in Cirrus will always yield a response body, even if the response body is an empty string. The sole exception to this rule is in the case of a HTTP HEAD response, which according to the [W3C HTTP/1.1 protocol][W3C-HEAD] should not return a message body.

```
trait Response {

  type Content
  def statusCode: Int
  def headers: Map[String, String]
  def body: Content
}
```

In order to connect a HTTP request to a HTTP response, the Client trait was created. Since all forms of HTTP requests over the Internet are instances of I/O-based operations, all HTTP responses are wrapped in a scala [Future][scala-future]. This abstraction allows users of the client to submit callbacks which will then be applied to the corresponding value of future.

```
trait Client {

  type ClientResponse <: Response

  implicit val ec: ExecutionContext

  def connect(request: Request): Future[ClientResponse]
}
```

#### Making HTTP Requests
You can use the classes found in the `cirrus.internal` package to make HTTP request. This is a way of manually constructing the components required for making a HTTP request and it rather long-winded. Using the `BasicHTTP`, `SprayHTTP`, `PlayHTTP` and `ArgonautHTTP` classes is strongly recommended and is covered later in the documentation


##### Creating a Basic Request
You can create a HTTP request instance using the BasicRequest class
```
val request = BasicRequest(method = "GET", address = "http://www.tiny-web.info/", headers = Nil, params = Nil, body = None)
```

##### Creating a Basic Client
You can create a HTTP Client with the Basic Client class
```
val request = BasicRequest(method = "GET", address = "http://www.tiny-web.info/", headers = Nil, params = Nil, body = None)

val client = BasicClient()
```

##### Retrieving a Basic Response
To retrieve a HTTP request wrapped in a `Future` simply pass the `connect` method on the Client instance an instance of a Request class
```
val request = BasicRequest(method = "GET", address = "http://www.tiny-web.info/", headers = Nil, params = Nil, body = None)
  
val client = BasicClient()

val future: Future[BasicResponse] = client.connect(request)
  
val response: BasicResponse = Await.result(future, 5 seconds)
```

##### Extracting the contents of a Response
You can access the response status code, headers and body as fields on the Response class
```
...

val response: BasicResponse = Await.result(future, 5 seconds)

val statusCode = response.statusCode
val headers = response.headers
val body = response.body
```

##### Passing HTTP Headers
You can add HTTP headers to your HTTP request using a `List[(String -> String)]`
```
val customHeaders = List("foo" -> "bar")

val request = BasicRequest(method = "GET", address = "http://www.tiny-web.info/", headers = customHeaders, params = Nil, body = None)
```

##### Passing Query Parameters
You can add HTTP query parameters to your HTTP request using a `List[(String -> String)]`
```
val customParams = List("foo" -> "bar")

val request = BasicRequest(method = "GET", address = "http://www.tiny-web.info/", headers = Nil, params = customParams, body = None)
```

#### Simplifying HTTP Requests

##### Back to the classics: HEAD, GET, PUT, POST, DELETE
To make working with Cirrus a lot easier, the `BasicHTTP` object contains the five most commonly used HTTP verbs expressed a case classes - HEAD, GET, PUT, POST and DELETE. You can use these convenience classes to shorthand the task of making HTTP requests. All of these classes have a `send` method which is used to perform the I/O request and produce a `Future[BasicResponse]`.

###### Example 1: A Basic HTTP GET request
In this example a `BasicHTTP.GET` instance is created and the `send` method is used to produce a `Future[BasicResponse]`. This example can also be applied to `BasicHTTP.HEAD` and  `BasicHTTP.DELETE`
```
val getRequest = BasicHTTP.GET("http://www.tiny-web.info/")

val asyncTask: Future[BasicResponse] = getRequest.send

...
```

###### Example 2: A Basic HTTP POST request
In this example a `BasicHTTP.POST` instance is created and the `send` method is used to produce a `Future[BasicResponse]`. This example can also be applied to `BasicHTTP.PUT`
```
val postRequest = BasicHTTP.POST("http://www.tiny-web.info/")
  
val asyncTask: Future[BasicResponse] = postRequest.send("foo=bar")

...
```

##### Passing HTTP Headers
To add a single HTTP header to a HTTP request from the `BasicHTTP` object use the `withHeader` method.
```
val putRequest = BasicHTTP.PUT("http://www.tiny-web.info/")
  
val header = "fuche" -> "ball"
  
putRequest.withHeader(header)

val asyncTask: Future[BasicResponse] = putRequest.send("foo=bar")

...
```

Multiple headers can be added in one go using the `withHeaders` method
```
val putRequest = BasicHTTP.PUT("http://www.tiny-web.info/")

val headers = List("fuche" -> "ball", "ist" -> "gut")

putRequest.withHeaders(headers)

val asyncTask: Future[BasicResponse] = putRequest.send("foo=bar")
```

To view the headers which have been set on a HTTP request, use the `headers` method
```
val putRequest = BasicHTTP.PUT("http://www.tiny-web.info/")

val putHeaders = List("fuche" -> "ball", "ist" -> "gut")

putRequest.withHeaders(putHeaders)

putRequest.headers  // List((fuche,ball), (ist,gut))
```

The headers on the `BasicHTTP` classes are mutable, so you can reset them using the `dropHeaders` method
```
val putRequest = BasicHTTP.PUT("http://www.tiny-web.info/")

val putHeaders = List("fuche" -> "ball", "ist" -> "gut")

putRequest.dropHeaders  // List()
```

##### Passing Query Parameters
To add a single HTTP query parameter to a HTTP request from the `BasicHTTP` object use the `withParam` method.
```
val putRequest = BasicHTTP.PUT("http://www.tiny-web.info/")

val param = "fuche" -> "ball"

putRequest.withParam(param)

val asyncTask: Future[BasicResponse] = putRequest.send("foo=bar")

...
```

Multiple query parameters can be added in one go using the `withParams` method
```
val putRequest = BasicHTTP.PUT("http://www.tiny-web.info/")

val params = List("fuche" -> "ball", "ist" -> "gut")

putRequest.withParams(params)

val asyncTask: Future[BasicResponse] = putRequest.send("foo=bar")
```

To view the params which have been set on a HTTP request, use the `params` method
```
val putRequest = BasicHTTP.PUT("http://www.tiny-web.info/")

val putParams = List("fuche" -> "ball", "ist" -> "gut")

putRequest.withParams(putParams)

putRequest.params  // List((fuche,ball), (ist,gut))
```

The params on the `BasicHTTP` classes are mutable, so you can reset them using the `dropParams` method
```
val putRequest = BasicHTTP.PUT("http://www.tiny-web.info/")

val putParams = List("fuche" -> "ball", "ist" -> "gut")

putRequest.dropParams  // List()
```

[Dispatch]: <http://dispatch.databinder.net/Dispatch.html>
[Play-WS]: <https://www.playframework.com/documentation/2.6.x/ScalaWS>
[Spray Client]: <http://spray.io/documentation/1.2.3/spray-client/>
[W3C-HEAD]: <https://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html#sec9.4>
[scala-future]: <http://docs.scala-lang.org/overviews/core/futures.html>

