package com.godis.network.rebound.client

import java.net.{HttpURLConnection, URL}

import com.godis.network.rebound.core._

import scala.collection.JavaConverters.{iterableAsScalaIterableConverter, mapAsScalaMapConverter}
import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.io.Source
import scala.util.{Try, Success}


/**
 * Created by Abim on 12/03/2016.
 */

object Basic {

  case class GET(address: String)(implicit ec: ExecutionContext) extends EmptyMessage {
    override val method: String = RequestMethods.GET
  }

  case class POST(address: String)(implicit ec: ExecutionContext) extends LoadedMessage {
    override val method: String = RequestMethods.POST
  }

  case class PUT(address: String)(implicit ec: ExecutionContext) extends LoadedMessage {
    override val method: String = RequestMethods.PUT
  }

  case class DELETE(address: String)(implicit ec: ExecutionContext) extends EmptyMessage {
    override val method: String = RequestMethods.DELETE
  }


  trait EmptyMessage extends HTTPMessage {

    def send() = basicClient
      .connect(BasicRequest(method = method, address = address, headers = headers))

    def ! = send()
  }

  trait LoadedMessage extends HTTPMessage {

    def send(payload: String) = basicClient
      .connect(BasicRequest(method = method, address = address, headers = headers, body = Some(payload)))

    def !(payload: String) = send(payload)
  }
}


case class BasicRequest(method: String, address: String, headers: List[(String, String)],
                        body: Option[String] = None) extends Request


case class BasicResponse(statusCode: Int, headers: Map[String, String], body: String) extends Response {
  override type Content = String
}


case class BasicClient() extends Client {

  override def connect(request: Request)(implicit ec: ExecutionContext): Future[Response] = {

    val promise = Promise[Response]()

    var connection: Option[HttpURLConnection] = None

    Future {

      // Open Connection
      connection = Some(new URL(request.address).openConnection().asInstanceOf[HttpURLConnection])

      // Set Request Method
      connection.foreach(_.setRequestMethod(request.method))

      // Set Request Headers
      connection.foreach(c => request.headers.foreach(h => c.setRequestProperty(h._1, h._2)))

      // Set Request Body
      connection.foreach(c => request.body.foreach { b =>
        c.setDoOutput(true)
        c.getOutputStream.write(b.getBytes("UTF-8"))
      })

      connection.foreach(_.connect())

      // Generate Response
      val response = for {
        statusCode <- connection.map(_.getResponseCode)
        headers <- connection.map(_.getHeaderFields.asScala.map(e => (e._1, e._2.asScala.mkString(","))).toMap)
        body <- connection.map(c => {
          val stream = Option(c.getErrorStream) getOrElse c.getInputStream
          Source.fromInputStream(stream).mkString
        })
      } yield BasicResponse(statusCode, headers, body)

      promise.complete(Success(response.get))

    } recover { case ex => promise.failure(ex)
    } andThen { case _ => connection.foreach(_.disconnect()) }

    promise.future
  }
}
