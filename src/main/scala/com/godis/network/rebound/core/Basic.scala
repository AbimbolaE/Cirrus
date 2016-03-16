package com.godis.network.rebound.core

import java.net.{HttpURLConnection, URL}
import java.nio.charset.CodingErrorAction

import com.godis.network.rebound.core.BasicClient.Defaults._

import scala.collection.JavaConverters.{iterableAsScalaIterableConverter, mapAsScalaMapConverter}
import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.io.{Codec, Source}
import scala.util.{Failure, Success, Try}

case class BasicRequest(method: String, address: String, headers: List[(String, String)],
                        params: List[(String, String)], body: Option[String] = None) extends Request

case class BasicResponse(statusCode: Int, headers: Map[String, String], body: String) extends Response {
  override type Content = String
}

case class BasicClient(requestBodyCharset: String = charset, codec: Codec = codec,
                       tweaks: List[(HttpURLConnection) => Unit] = tweaks)
                      (implicit val ec: ExecutionContext = ExecutionContext.global) extends Client {

  override def connect(request: Request): Future[Response] = {

    val promise = Promise[Response]()

    var connection: Option[HttpURLConnection] = None

    Future {

      // Open Connection
      val queryParams = if (request.params.nonEmpty) "?" + request.params.map(p => p._1 + "=" + p._2).mkString(",") else ""
      connection = Some(new URL(request.address + queryParams).openConnection().asInstanceOf[HttpURLConnection])

      // Apply Default Tweaks
      connection.foreach(c => tweaks.foreach(_(c)))

      // Set Request Method
      connection.foreach(_.setRequestMethod(request.method))

      // Set Request Headers
      connection.foreach(c => request.headers.foreach(h => c.setRequestProperty(h._1, h._2)))

      // Set Request Body
      connection.foreach(c => request.body.foreach { b =>
        c.setDoOutput(true)
        c.setFixedLengthStreamingMode(b.length)
        c.getOutputStream.write(b.getBytes(requestBodyCharset))
      })

      // Generate Response
      val response = for {
        statusCode <- connection.map(_.getResponseCode)
        headers <- connection.map(_.getHeaderFields.asScala.map(e => (e._1, e._2.asScala.mkString(","))).toMap)
        if connection.get.getErrorStream == null
        body <- connection.map(c => Source.fromInputStream(c.getInputStream)(codec).mkString)
      } yield Success(BasicResponse(statusCode, headers, body))


      // Extract ErrorStream if necessary
      response orElse {

        val response = "\n" + Source.fromInputStream(connection.get.getErrorStream)(codec).mkString

        val error = Try(connection.get.getInputStream).recover { case ex => ex }.get.asInstanceOf[Exception]

        Some(Failure(new FailedRequest(response, error)))
      } foreach promise.complete

    } recover { case ex => promise.failure(ex)
    } andThen { case _ => connection.foreach(_.disconnect()) }

    promise.future
  }
}

object BasicClient {

  object Defaults {

    val charset = "UTF-8"

    val codec = {
      val c = Codec("UTF-8")
      c.onMalformedInput(CodingErrorAction.IGNORE)
      c
    }

    val tweaks = List(
      (h: HttpURLConnection) => h.setReadTimeout(10000),
      (h: HttpURLConnection) => h.setConnectTimeout(5000)
    )
  }

  object Builder {

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
      basicClient = basicClient.copy(tweaks = tweak :: basicClient.tweaks)(basicClient.ec)
      this
    }

    def build() = basicClient
  }
}

case class FailedRequest(response: String, cause: Exception) extends RuntimeException(cause)
