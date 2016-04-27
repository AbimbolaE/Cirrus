# Cirrus
Cirrus is a lightweight scala library which provides a http client for making http requests and consuming REST APIs

### Prologue
In quiet a few scala projects I've worked, I've needed a lightweight HTTP client for interacting with REST APIs. In the scala community there are only a handful of HTTP clients which are available for interacting with Cloud-based APIs and among them are the HTTP clients provided by the Play and the Spray Frameworks and Dispatch but none come without their disadvantages.

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
    - Making a Custom Basic Client
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

These classes all require an instance of a Basic Client to be within implicit scope. The `cirrus` package object provides one by default however, you can pass your own instance by bringing it within block scope

###### Example 3: A Basic HTTP POST request with a custom Basic client
In this example a custom `BasicClient` is created and used in creating a `BasicHTTP.POST`.
```
implicit val client = BasicClient()

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

##### Working with JSON
Cirrus is designed to be a lightweight library with easy integration and as such, it proves support for working with JSON using the Spray, Play and Argonaut JSON AST libraries.

##### Spray HTTP
To use Cirrus with the Spray JSON library, simply use the case classes inside the `SprayHTTP` object instead of the `BasicHTTP` object. These classes all take a type parameter `T` and require an instance of a `JsonReader[T]` within implicit scope to deserialise the response body into the appropriate type.

In the following examples, we will be assuming that a `Book` case class has been created along with a `JsonFormat[Book]` instance within impicit scope in it's companion object.
```
case class Book(id: Int, name: String, author: String)
object Book {
    implicit val format: JsonFormat[Book] = jsonFormat3(Book.apply)
}
```
See https://github.com/spray/spray-json#providing-jsonformats-for-case-classes for more info.

###### Example 1: A Spray HTTP GET request
In this example a `SprayHTTP.GET` instance is created and the `send` method is used to produce a `Future[SprayResponse[T]]`. This example can also be applied to `SprayHTTP.DELETE`
```
import spray.json.DefaultJsonProtocol._
import spray.json._

val getRequest = SprayHTTP.GET[Book]("http://localhost:9000/book/1")

val asyncTask: Future[SprayResponse[Book]] = getRequest.send

...
```

###### Example 1: A Spray HTTP PUT request
In this example a `SprayHTTP.PUT` instance is created and the `send` method is used to produce a `Future[SprayResponse[T]]`. This example can also be applied to `SprayHTTP.POST`
```
import spray.json.DefaultJsonProtocol._
import spray.json._

val bible = Book(1, "Bible", "Jesus Christ")

val putRequest = SprayHTTP.PUT[Book]("http://localhost:9000/book/create")

val asyncTask: Future[SprayResponse[Book]] = putRequest.send(bible)

...
```

##### Play HTTP
To use Cirrus with the Play JSON library, simply use the case classes inside the `PlayHTTP` object instead of the `BasicHTTP` object. These classes all take a type parameter `T` and require an instance of a `Reads[T]` within implicit scope to deserialise the response body into the appropriate type.

In the following examples, we will be assuming that a `Book` case class has been created along with a `OFormat[Book]` instance within impicit scope in it's companion object.
```
case class Book(id: Int, name: String, author: String)
object Book {
    implicit val format: OFormat[Book] = play.api.libs.json.Json.format[Book]
}
```
See https://www.playframework.com/documentation/2.6.x/ScalaJsonCombinators for more info.

###### Example 1: A Play HTTP GET request
In this example a `PlayHTTP.GET` instance is created and the `send` method is used to produce a `Future[PlayResponse[T]]`. This example can also be applied to `PlayHTTP.DELETE`
```
val getRequest = PlayHTTP.GET[Book]("http://localhost:9000/book/1")

val asyncTask: Future[PlayResponse[Book]] = getRequest.send

...
```

###### Example 1: A Play HTTP PUT request
In this example a `PlayHTTP.PUT` instance is created and the `send` method is used to produce a `Future[PlayResponse[T]]`. This example can also be applied to `PlayHTTP.POST`
```
val bible = Book(1, "Bible", "Jesus Christ")

val putRequest = PlayHTTP.PUT[Book]("http://localhost:9000/book/create")

val asyncTask: Future[PlayResponse[Book]] = putRequest.send(bible)

...
```

##### Argonaut HTTP
To use Cirrus with the Argonaut JSON library, simply use the case classes inside the `ArgonautHTTP` object instead of the `BasicHTTP` object. These classes all take a type parameter `T` and require an instance of a `DecodeJson[T]` within implicit scope to deserialise the response body into the appropriate type.

In the following examples, we will be assuming that a `Book` case class has been created along with a `CodecJson[Book]` instance within impicit scope in it's companion object.
```
import argonaut.Argonaut._
import argonaut._

case class Book(id: Int, name: String, author: String)
object Book {
    implicit val format: CodecJson[Book] = casecodec4(Book.apply, Book.unapply)("id", "name", "author")
}
```
See http://argonaut.io/doc/codec/ for more info.

###### Example 1: An Argonaut HTTP GET request
In this example a `ArgonautHTTP.GET` instance is created and the `send` method is used to produce a `Future[ArgonautResponse[T]]`. This example can also be applied to `ArgonautHTTP.DELETE`
```
import argonaut.Argonaut._

val getRequest = ArgonautHTTP.GET[Book]("http://localhost:9000/book/1")

val asyncTask: Future[ArgonautResponse[Book]] = getRequest.send

...
```

###### Example 1: An Argonaut HTTP PUT request
In this example a `ArgonautHTTP.PUT` instance is created and the `send` method is used to produce a `Future[ArgonautResponse[T]]`. This example can also be applied to `ArgonautHTTP.POST`
```
import argonaut.Argonaut._

val bible = Book(1, "Bible", "Jesus Christ")

val putRequest = ArgonautHTTP.PUT[Book]("http://localhost:9000/book/create")

val asyncTask: Future[ArgonautResponse[Book]] = putRequest.send(bible)

...
```

#### Advanced Usage

##### Working with Cirrus
In Cirrus, it is possible to unwrap a Response and access it's body directly by using the `Cirrus` object. `GET`,`DELETE` and `HEAD` requests can simply be passed into its `apply` method to be invoked immediately, while `PUT` and `POST` requests require the use of the `!` method to send the request along with a payload.

###### Example 1: An Argonaut HTTP GET request with Cirrus
In this example a `ArgonautHTTP.GET` instance is created and the `apply` method of the `Cirrus` object is used to produce a `Future[ArgonautResponse[T]]`.
```
import argonaut.Argonaut._

val bible: Book = Cirrus(ArgonautHTTP.GET[Book]("http://localhost:9000/book/1"))
```

###### Example 2: A Play HTTP PUT request with Cirrus
In this example a `PlayHTTP.PUT` instance is created and the `apply` method of the `Cirrus` object is used to produce a `Future[PlayResponse[T]]`.
```
val bible = Book(1, "Bible", "Jesus Christ")

val anotherBible = Cirrus(PlayHTTP.PUT[Book]("http://localhost:9000/book/create") ! bible)
```

##### Making a Custom Basic Client
In Cirrus, it is possible to determine how the underlying `HTTPUrlConnection` instance will be configured. To do so requires creating a customised instance of the `BasicClient` class. The `cirrus` package object comes with a plain `BasicClient` instance in implicit scope which has been used in all the previous examples. The `BasicClient` class comes with a `Builder` case class which provides a fluent API for creating `BasicClient` instances.

###### Example 1: Creating a Basic Client with a custom Execution Context
In this example a Basic Client is created with a custom Execution Context
```
import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext

implicit val executionContext = ExecutionContext.fromExecutor(Executors.newSingleThreadExecutor())

val client: BasicClient = BasicClient
      .Builder()
      .withExecutionContext(executionContext)
      .build()
```

[Dispatch]: <http://dispatch.databinder.net/Dispatch.html>
[Play-WS]: <https://www.playframework.com/documentation/2.6.x/ScalaWS>
[Spray Client]: <http://spray.io/documentation/1.2.3/spray-client/>
[W3C-HEAD]: <https://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html#sec9.4>
[scala-future]: <http://docs.scala-lang.org/overviews/core/futures.html>

