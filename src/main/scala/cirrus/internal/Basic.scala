package cirrus.internal

import java.io.FileNotFoundException
import java.net.{HttpURLConnection, URL}

import cirrus.internal.ClientConfig._

import scala.collection.JavaConverters.{iterableAsScalaIterableConverter, mapAsScalaMapConverter}
import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.io.{Codec, Source}
import scala.util.control.NonFatal
import scala.util.{Failure, Success, Try}

case class BasicRequest(method: String, address: String, headers: List[(String, String)],
                        params: List[(String, String)], body: Option[String] = None) extends Request

case class BasicResponse(statusCode: Int, headers: Map[String, String], body: String) extends Response {
  override type Content = String
}

case class BasicClient(requestBodyCharset: String = charset, codec: Codec = codec,
                       defaultHeaders: Seq[(String, String)] = headers,
                       tweaks: Seq[(HttpURLConnection) => Unit] = tweaks)
                      (implicit val ec: ExecutionContext = ExecutionContext.global) extends Client {

  override def connect(request: Request): Future[Response] = {

    val promise = Promise[Response]()

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
          val stream = if (statusCode < 400) connectionOpt.get.getInputStream else connectionOpt.get.getErrorStream
          Option(stream) map (Source.fromInputStream(_)(codec).mkString) orElse Some("")
        }
      } yield promise complete Success(BasicResponse(statusCode, headers, body))

    } recover { case NonFatal(ex) => promise.failure(FailedRequest(ex))
    } andThen { case _ => connectionOpt.foreach(_.disconnect()) }

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

case class FailedRequest(cause: Throwable) extends RuntimeException(cause)
