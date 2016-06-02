# Cirrus
Cirrus is a lightweight scala library which provides a http client for making http requests and consuming REST APIs

### Prologue
~~In quite a few scala projects I've worked, I've needed a lightweight HTTP client for interacting with REST APIs. In the scala community there are only a handful of HTTP clients which are available for interacting with Cloud-based APIs and among them are the HTTP clients provided by the Play and the Spray Frameworks and Dispatch but none come without their disadvantages.~~

~~The Play Framework provides the [Play-WS] Client library which comes with bits and pieces of the Play Stack, the Spray Framework provides the [Spray Client] library which requires an instance of an implicit ActorSystem within scope and [Dispatch] provides cryptic hieroglyph DSL for making simple HTTP requests.~~

~~In an effort to make a simpler interface for making HTTP requests and consuming REST APIs which expose data in JSON format I created Cirrus. It provides the building blocks of HTTP Requests, Responses and Clients and then implements most of the common use cases with simple Scala case classes.~~

~~Cirrus has no external dependencies on Third-party libraries and simply uses the HTTPURLConnection class (which comes as part of the Java JDK) for making HTTP requests. As a result, it is extremely lightweight (~221 KB jar) as comes with none of it's third party dependencies.~~

One weekend, I got bored and decided to create a simple and easy to use HTTP Client library in Scala....The End.
=]

### A Minimal Example

```
import cirrus.clients.BasicHTTP.GET

import scala.concurrent.Await
import scala.concurrent.duration._

object MinimalExample extends App {

  val html = Await.result(Cirrus(GET("https://www.google.co.uk")), 3 seconds)

  println(html)
}
```

... produces ...

```
<!doctype html><html itemscope="" itemtype="http://schema.org/WebPage" lang="en-GB"><head><meta content="text/html; charset=UTF-8" http-equiv="Content-Type"><meta content="/images/branding/googleg/1x/googleg_standard_color_128dp.png" itemprop="image"><title>Google</title><script>(function(){window.google={kEI:'nNpOV8rvHcuCgAacv6zoAQ',kEXPI:'18167,1350654,3700278,3700389,4003510,4028875,4029815,4031109,4032677,4036509,4036527,4038012,4039268,4043492,4045841,4048347,4052304,4052811,4056038,4057739,4058337,4059767,4060014,4061155,4061181,4061552,4062333,4062663,4062706,4062972,4062987,4063111,4063126,4063879,4063960,4064133,4064449,4064501,4064904,4064931,4065120,4065154,4065406,4065675,4065786,4065794,4065855,4065918,4066099,4066195,4066328,4066654,4066686,4066756,4066890,4066965,4066973,4067274,4067384,4067518,4067636,4067702,4067860,4068022,4068067,4068186,4068776,8300273,8503585,8504350,8504893,8504930,8505142,10200083,10200095,10201287',authuser:0,kscs:'c9c918f0_24'};google.kHL='en-GB';})();(function(){google.lc=[];google.li=0;google.getEI=function(a){for(var b;a&&(!a.getAttribute||!(b=a.getAttribute("eid")));)a=a.parentNode;return b||google.kEI};google.getLEI=function(a){for(var b=null;a&&(!a.getAttribute||!(b=a.getAttribute("leid")));)a=a.parentNode;return b};google.https=function(){return"https:"==window.location.protocol};google.ml=function(){return null};google.wl=function(a,b){try{google.ml(Error(a),!1,b)}catch(c){}};google.time=function(){return(new Date).getTime()};google.log=function(a,b,c,e,g){a=google.logUrl(a,b,c,e,g);if(""!=a){b=new Image;var d=google.lc,f=google.li;d[f]=b;b.onerror=b.onload=b.onabort=function(){delete d[f]};window.google&&window.google.vel&&window.google.vel.lu&&window.google.vel.lu(a);b.src=a;google.li=f+1}};google.logUrl=function(a,b,c,e,g){var d="",f=google.ls||"";if(!c&&-1==b.search("&ei=")){var h=google.getEI(e),d="&ei="+h;-1==b.search("&lei=")&&((e=google.getLEI(e))?d+="&lei="+e:h!=google.kEI&&(d+="&lei="+google.kEI))}a=c||"/"+(g||"gen_204")+"?atyp=i&ct="+a+"&cad="+b+d+f+"&zx="+google.time();/^http:/i.test(a)&&google.https()&&(google.ml(Error("a"),!1,{src:a,glmm:1}),a="");return a};google.y={};google.x=function(a,b){google.y[a.id]=[a,b];return!1};google.load=function(a,b,c){google.x({id:a+k++},function(){google.load(a,b,c)})};var k=0;})();var _gjwl=location;function _gjuc(){var a=_gjwl.href.indexOf("#");if(0<=a&&(a=_gjwl.href.substring(a),0<a.indexOf("&q=")||0<=a.indexOf("#q="))&&(a=a.substring(1),-1==a.indexOf("#"))){for(var d=0;d<a.length;){var b=d;"&"==a.charAt(b)&&++b;var c=a.indexOf("&",b);-1==c&&(c=a.length);b=a.substring(b,c);if(0==b.indexOf("fp="))a=a.substring(0,d)+a.substring(c,a.length),c=d;else if("cad=h"==b)return 0;d=c}_gjwl.href="/search?"+a+"&cad=h";return 1}return 0}
function _gjh(){!_gjuc()&&window.google&&google.x&&google.x({id:"GJH"},function(){google.nav&&google.nav.gjh&&google.nav.gjh()})};window._gjh&&_gjh();</script><style>#gbar,#guser{font-size:13px;padding-top:1px !important;}#gbar{height:22px}#guser{padding-bottom:7px !important;text-align:right}.gbh,.gbd{border-top:1px solid #c9d7f1;font-size:1px}.gbh{height:0;position:absolute;top:24px;width:100%}@media all{.gb1{height:22px;margin-right:.5em;vertical-align:top}#gbar{float:left}}a.gb1,a.gb4{text-decoration:underline !important}a.gb1,a.gb4{color:#00c !important}.gbi .gb4{color:#dd8e27 !important}.gbf .gb4{color:#900 !important}
</style><style>body,td,a,p,.h{font-family:arial,sans-serif}body{margin:0;overflow-y:scroll}#gog{padding:3px 8px 0}td{line-height:.8em}.gac_m td{line-height:17px}form{margin-bottom:20px}.h{color:#36c}.q{color:#00c}.ts td{padding:0}.ts{border-collapse:collapse}em{font-weight:bold;font-style:normal}.lst{height:25px;width:496px}.gsfi,.lst{font:18px arial,sans-serif}.gsfs{font:17px arial,sans-serif}.ds{display:inline-box;display:inline-block;margin:3px 0 4px;margin-left:4px}input{font-family:inherit}a.gb1,a.gb2,a.gb3,a.gb4{color:#11c !important}body{background:#fff;color:black}a{color:#11c;text-decoration:none}a:hover,a:active{text-decoration:underline}.fl a{color:#36c}a:visited{color:#551a8b}a.gb1,a.gb4{text-decoration:underline}a.gb3:hover{text-decoration:none}#ghead a.gb2:hover{color:#fff !important}.sblc{padding-top:5px}.sblc a{display:block;margin:2px 0;margin-left:13px;font-size:11px}.lsbb{background:#eee;border:solid 1px;border-color:#ccc #999 #999 #ccc;height:30px}.lsbb{display:block}.ftl,#fll a{display:inline-block;margin:0 12px}.lsb{background:url(/images/nav_logo229.png) 0 -261px repeat-x;border:none;color:#000;cursor:pointer;height:30px;margin:0;outline:0;font:15px arial,sans-serif;vertical-align:top}.lsb:active{background:#ccc}.lst:focus{outline:none}</style><script></script><link href="/images/branding/product/ico/googleg_lodp.ico" rel="shortcut icon"></head><body bgcolor="#fff"><script>(function(){var src='/images/nav_logo229.png';var iesg=false;document.body.onload = function(){window.n && window.n();if (document.images){new Image().src=src;}
if (!iesg){document.f&&document.f.q.focus();document.gbqf&&document.gbqf.q.focus();}
}
})();</script><div id="mngb">    <div id=gbar><nobr><b class=gb1>Search</b> <a class=gb1 href="https://www.google.co.uk/imghp?hl=en&tab=wi">Images</a> <a class=gb1 href="https://maps.google.co.uk/maps?hl=en&tab=wl">Maps</a> <a class=gb1 href="https://play.google.com/?hl=en&tab=w8">Play</a> <a class=gb1 href="https://www.youtube.com/?gl=GB&tab=w1">YouTube</a> <a class=gb1 href="https://news.google.co.uk/nwshp?hl=en&tab=wn">News</a> <a class=gb1 href="https://mail.google.com/mail/?tab=wm">Gmail</a> <a class=gb1 href="https://drive.google.com/?tab=wo">Drive</a> <a class=gb1 style="text-decoration:none" href="https://www.google.co.uk/intl/en/options/"><u>More</u> &raquo;</a></nobr></div><div id=guser width=100%><nobr><span id=gbn class=gbi></span><span id=gbf class=gbf></span><span id=gbe></span><a href="http://www.google.co.uk/history/optout?hl=en" class=gb4>Web History</a> | <a  href="/preferences?hl=en" class=gb4>Settings</a> | <a target=_top id=gb_70 href="https://accounts.google.com/ServiceLogin?hl=en&passive=true&continue=https://www.google.co.uk/" class=gb4>Sign in</a></nobr></div><div class=gbh style=left:0></div><div class=gbh style=right:0></div>    </div><center><br clear="all" id="lgpd"><div id="lga"><div style="padding:28px 0 3px"><div style="height:110px;width:276px;background:url(/images/branding/googlelogo/1x/googlelogo_white_background_color_272x92dp.png) no-repeat" title="Google" align="left" id="hplogo" onload="window.lol&&lol()"><div style="color:#777;font-size:16px;font-weight:bold;position:relative;top:70px;left:218px" nowrap="">UK</div></div></div><br></div><form action="/search" name="f"><table cellpadding="0" cellspacing="0"><tr valign="top"><td width="25%">&nbsp;</td><td align="center" nowrap=""><input name="ie" value="ISO-8859-1" type="hidden"><input value="en-GB" name="hl" type="hidden"><input name="source" type="hidden" value="hp"><input name="biw" type="hidden"><input name="bih" type="hidden"><div class="ds" style="height:32px;margin:4px 0"><input style="color:#000;margin:0;padding:5px 8px 0 6px;vertical-align:top" autocomplete="off" class="lst" value="" title="Google Search" maxlength="2048" name="q" size="57"></div><br style="line-height:0"><span class="ds"><span class="lsbb"><input class="lsb" value="Google Search" name="btnG" type="submit"></span></span><span class="ds"><span class="lsbb"><input class="lsb" value="I'm Feeling Lucky" name="btnI" onclick="if(this.form.q.value)this.checked=1; else top.location='/doodles/'" type="submit"></span></span></td><td class="fl sblc" align="left" nowrap="" width="25%"><a href="/advanced_search?hl=en-GB&amp;authuser=0">Advanced search</a><a href="/language_tools?hl=en-GB&amp;authuser=0">Language tools</a></td></tr></table><input id="gbv" name="gbv" type="hidden" value="1"></form><div id="gac_scont"></div><div style="font-size:83%;min-height:3.5em"><br></div><span id="footer"><div style="font-size:10pt"><div style="margin:19px auto;text-align:center" id="fll"><a href="/intl/en/ads/">AdvertisingProgrammes</a><a href="/services/">Business Solutions</a><a href="https://plus.google.com/103583604759580854844" rel="publisher">+Google</a><a href="/intl/en/about.html">About Google</a><a href="https://www.google.co.uk/setprefdomain?prefdom=US&amp;sig=__VpHLnVbACabnBkW1720_s67O1_k%3D" id="fehl">Google.com</a></div></div><p style="color:#767676;font-size:8pt">&copy; 2016 - <a href="/intl/en/policies/privacy/">Privacy</a> - <a href="/intl/en/policies/terms/">Terms</a></p></span></center><script>(function(){window.google.cdo={height:0,width:0};(function(){var a=window.innerWidth,b=window.innerHeight;if(!a||!b)var c=window.document,d="CSS1Compat"==c.compatMode?c.documentElement:c.body,a=d.clientWidth,b=d.clientHeight;a&&b&&(a!=google.cdo.width||b!=google.cdo.height)&&google.log("","","/client_204?&atyp=i&biw="+a+"&bih="+b+"&ei="+google.kEI);})();})();</script><div id="xjsd"></div><div id="xjsi"><script>(function(){function c(b){window.setTimeout(function(){var a=document.createElement("script");a.src=b;document.getElementById("xjsd").appendChild(a)},0)}google.dljp=function(b,a){google.xjsu=b;c(a)};google.dlj=c;})();(function(){window.google.xjsrm=[];})();if(google.y)google.y.first=[];if(!google.xjs){window._=window._||{};window._._DumpException=function(e){throw e};if(google.timers&&google.timers.load.t){google.timers.load.t.xjsls=new Date().getTime();}google.dljp('/xjs/_/js/k\x3dxjs.hp.en_US.pAiJCROIPXo.O/m\x3dsb_he,d/rt\x3dj/d\x3d1/t\x3dzcms/rs\x3dACT90oGLsUyAMx4Wcdl3ttpvKEobkIQ6lQ','/xjs/_/js/k\x3dxjs.hp.en_US.pAiJCROIPXo.O/m\x3dsb_he,d/rt\x3dj/d\x3d1/t\x3dzcms/rs\x3dACT90oGLsUyAMx4Wcdl3ttpvKEobkIQ6lQ');google.xjs=1;}google.pmc={"sb_he":{"agen":true,"cgen":true,"client":"heirloom-hp","dh":true,"dhqt":true,"ds":"","fl":true,"host":"google.co.uk","isbh":28,"jam":0,"jsonp":true,"msgs":{"cibl":"Clear Search","dym":"Did you mean:","lcky":"I\u0026#39;m Feeling Lucky","lml":"Learn more","oskt":"Input tools","psrc":"This search was removed from your \u003Ca href=\"/history\"\u003EWeb History\u003C/a\u003E","psrl":"Remove","sbit":"Search by image","srch":"Google Search"},"ovr":{},"pq":"","refpd":true,"rfs":[],"scd":10,"sce":5,"stok":"2PqnSLwNBLZ6efBLugq64evw3kQ"},"d":{}};google.y.first.push(function(){if(google.med){google.med('init');google.initHistory();google.med('history');}});if(google.j&&google.j.en&&google.j.xi){window.setTimeout(google.j.xi,0);}
</script></div></body></html>
```

### Installation
Cirrus is available in Maven Central under the group id of "com.github.godis".

* Maven
```
<dependency>
    <groupId>com.github.godis</groupId>
    <artifactId>cirrus_2.11</artifactId>
    <version>1.4.1</version>
</dependency>
```

* SBT
```
libraryDependencies += "com.github.godis" % "cirrus_2.11" % "1.4.1"
```

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

val executionContext = ExecutionContext.fromExecutor(Executors.newSingleThreadExecutor())

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
[Maven Central]: <http://search.maven.org/>
