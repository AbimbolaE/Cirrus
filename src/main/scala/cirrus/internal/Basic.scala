package cirrus.internal

import java.io.IOException
import java.net.{HttpURLConnection, URL}
import java.nio.charset.CodingErrorAction

import cirrus.clients.BasicHTTP.BasicResponse
import cirrus.internal.ClientConfig._
import play.api.data.validation.ValidationError
import play.api.libs.json.JsPath

import scala.collection.JavaConverters.{iterableAsScalaIterableConverter, mapAsScalaMapConverter}
import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.io.{Codec, Source}
import scala.util.Success
import scala.util.control.NonFatal

case class BasicClient(requestBodyCharset: String = charset, codec: Codec = codec,
                       defaultHeaders: Seq[(String, String)] = headers,
                       tweaks: Seq[(HttpURLConnection) => Unit] = tweaks)
                      (implicit val ec: ExecutionContext = ExecutionContext.global) extends Client {

  override type ClientResponse = BasicResponse

  override def connect(request: Request) = {

    val promise = Promise[BasicResponse]()

    var connectionOpt: Option[HttpURLConnection] = None

    Future {

      // Open Connection
      val queryParams = if (request.params.nonEmpty) request.params.map(p => p._1 + "=" + p._2).mkString("?", "&", "") else ""
      connectionOpt = Some(new URL(request.address + queryParams).openConnection().asInstanceOf[HttpURLConnection])

      // Set Request Method
      connectionOpt foreach (_.setRequestMethod(request.method))

      // Apply Default Tweaks
      connectionOpt foreach (c => tweaks.foreach(_ (c)))

      // Apply Default Headers
      connectionOpt foreach (c => defaultHeaders.foreach(h => c.setRequestProperty(h._1, h._2)))

      // Set Request Headers
      connectionOpt foreach (c => request.headers.foreach(h => c.setRequestProperty(h._1, h._2)))

      // Set Request Body
      connectionOpt foreach (c => request.body.foreach { b =>
        c.setDoOutput(true)
        c.setFixedLengthStreamingMode(b.length)
        c.getOutputStream.write(b.getBytes(requestBodyCharset))
      })

      // Generate Response
      for {
        statusCode <- connectionOpt map (_.getResponseCode)
        headers <- connectionOpt map (_.getHeaderFields.asScala.map(e => (e._1, e._2.asScala.mkString(","))).toMap)
        body <- {
          Option(connectionOpt.get.getErrorStream)
            .orElse(Some(connectionOpt.get.getInputStream))
            .map(Source.fromInputStream(_)(codec).mkString)
//          val stream = if (statusCode < 400) connectionOpt.get.getInputStream else connectionOpt.get.getErrorStream
//          Option(stream) map (Source.fromInputStream(_)(codec).mkString) orElse Some("")
        }
      } yield promise complete Success(BasicResponse(statusCode, headers, body))

    } recover { case NonFatal(ex) => promise failure FailedRequest(ex)
    } andThen { case _ => connectionOpt foreach (_.disconnect()) }

    promise.future
  }
}

object BasicClient {

  case class Builder() {

    private var basicClient = BasicClient()

    def withExecutionContext(ec: ExecutionContext) = {
      basicClient = basicClient.copy()(ec)
      this
    }

    def withCodec(codec: Codec) = {
      basicClient = basicClient.copy(codec = codec)(basicClient.ec)
      this
    }

    def withRequestBodyCharset(charset: String) = {
      basicClient = basicClient.copy(requestBodyCharset = charset)(basicClient.ec)
      this
    }

    def withTweak(tweak: (HttpURLConnection) => Unit) = {
      basicClient = basicClient.copy(tweaks = basicClient.tweaks :+ tweak)(basicClient.ec)
      this
    }

    def withDefaultHeader(header: (String, String)) = {
      basicClient = basicClient.copy(defaultHeaders = basicClient.defaultHeaders :+ header)(basicClient.ec)
      this
    }

    def withDefaultHeaders(headers: Seq[(String, String)]) = {
      basicClient = basicClient.copy(defaultHeaders = basicClient.defaultHeaders ++ headers)(basicClient.ec)
      this
    }

    def build() = basicClient
  }
}

case class BasicRequest(method: String, address: String, headers: List[(String, String)],
                        params: List[(String, String)], body: Option[String] = None) extends Request

case class FailedRequest(cause: Throwable) extends IOException(cause)
object FailedRequest {
  def apply(message: String): FailedRequest = FailedRequest(new IOException(message))

  def apply(errors: Seq[(JsPath, Seq[ValidationError])]): FailedRequest =
    errors
      .map(e => (e._1.toJsonString, e._2.head.message))
      .map(e => e._1 + " has error " + e._2)
      .map(m => FailedRequest(m))
      .head
}

object ClientConfig {

  val charset = "UTF-8"

  val codec = {
    val c = Codec("UTF-8")
    c.onMalformedInput(CodingErrorAction.IGNORE)
    c
  }

  val headers = List.empty[(String, String)]

  val tweaks = List(
    (h: HttpURLConnection) => h.setReadTimeout(10000),
    (h: HttpURLConnection) => h.setConnectTimeout(5000)
  )
}
