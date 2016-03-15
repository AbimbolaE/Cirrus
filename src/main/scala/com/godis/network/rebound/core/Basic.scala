package com.godis.network.rebound.core

import java.net.{HttpURLConnection, URL}

import scala.collection.JavaConverters.{iterableAsScalaIterableConverter, mapAsScalaMapConverter}
import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.io.Source
import scala.util.{Try, Failure, Success}

/**
 * Created by Abim on 14/03/2016.
 */
case class BasicRequest(method: String, address: String, headers: List[(String, String)],
                        params: List[(String, String)], body: Option[String] = None) extends Request

case class BasicResponse(statusCode: Int, headers: Map[String, String], body: String) extends Response {
  override type Content = String
}

case class BasicClient() extends Client {

  override def connect(request: Request)(implicit ec: ExecutionContext): Future[Response] = {

    val promise = Promise[Response]()

    var connection: Option[HttpURLConnection] = None

    println(request)

    Future {

      // Open Connection
      val queryParams = "?" + request.params.map(p => p._1 + "=" + p._2).mkString(",")
      connection = Some(new URL(request.address + queryParams).openConnection().asInstanceOf[HttpURLConnection])

      // Set connection Connect Timeout and Read Timeout
      connection.foreach(_.setConnectTimeout(10000))
      connection.foreach(_.setReadTimeout(5000))

      // Set Request Method
      connection.foreach(_.setRequestMethod(request.method))

      // Set Request Headers
      connection.foreach(c => request.headers.foreach(h => c.setRequestProperty(h._1, h._2)))

      // Set Request Body
      connection.foreach(c => request.body.foreach { b =>
        c.setDoOutput(true)
        c.getOutputStream.write(b.getBytes("UTF-8"))
      })

      if (connection.get.getErrorStream != null) {

        connection.foreach(c => {
          val body = Source.fromInputStream(c.getErrorStream).mkString
          val ex = new RuntimeException(body)
          promise.complete(Failure(ex))
        })
      } else {

        // Generate Response
        val response = for {
          statusCode <- connection.map(_.getResponseCode)
          headers <- connection.map(_.getHeaderFields.asScala.map(e => (e._1, e._2.asScala.mkString(","))).toMap)
          if connection.get.getErrorStream == null
          body <- connection.map(c => Source.fromInputStream(c.getInputStream).mkString)
        } yield BasicResponse(statusCode, headers, body)


        // Extract ErrorStream if necessary
        response map (Success(_)) orElse {

          val errorMessage = Source.fromInputStream(connection.get.getErrorStream).mkString

          val error = Try { connection.get.getInputStream }
            .recover { case ex => ex }
            .get.asInstanceOf[Exception]

          Some(Failure(new FailedRequest(errorMessage, error)))
        } foreach promise.complete
      }

    } recover { case ex => promise.failure(ex)
    } andThen { case _ => connection.foreach(_.disconnect()) }

    promise.future
  }
}

case class FailedRequest(message: String, cause: Exception) extends RuntimeException(message, cause)