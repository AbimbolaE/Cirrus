package com.godis.network.rebound.core

import java.io.BufferedOutputStream
import java.net.{HttpURLConnection, URL}

import scala.collection.JavaConverters.{iterableAsScalaIterableConverter, mapAsScalaMapConverter}
import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.io.Source
import scala.util.Success

/**
  * Created by esurua01 on 11/03/2016.
  */
trait Request {

  def address: String
  def headers: Map[String, String]
  def body: Option[Array[Byte]]
}


trait Response {

  type Content
  def statusCode: Int
  def headers: Map[String, String]
  def body: Content
}


trait Client {
  def connect(request: Request)(implicit ec: ExecutionContext): Future[Response]
}


case class SimpleResponse(statusCode: Int, headers: Map[String, String], body: String) extends Response {
  override type Content = String
}


case class SimpleRequest(address: String, headers: Map[String, String] = Map.empty[String, String],
                         body: Option[Array[Byte]] = None) extends Request


case class SimpleClient() extends Client {

  override def connect(request: Request)(implicit ec: ExecutionContext): Future[Response] = {

    val promise = Promise[Response]()

    var connection: Option[HttpURLConnection] = None

    Future {

      // Open Connection
      connection = Some(new URL(request.address).openConnection().asInstanceOf[HttpURLConnection])

      // Set Request Headers
      connection.foreach(c => request.headers.foreach(h => c.setRequestProperty(h._1, h._2)))

      // Set Request Body
      connection.foreach(c => request.body.foreach { b =>
          c.setDoOutput(true)
          new BufferedOutputStream(c.getOutputStream).write(b)
      })

      // Generate Response
      val response = for {
        statusCode <- connection.map(_.getResponseCode)
        headers    <- connection.map(_.getHeaderFields.asScala.map(e => (e._1, e._2.asScala.mkString(","))).toMap)
        body       <- connection.map(c => Source.fromInputStream(c.getInputStream).mkString)
      } yield SimpleResponse(statusCode, headers, body)

      promise.complete(Success(response.get))

    } recover { case ex => promise.failure(ex)
    } andThen { case _ => connection.foreach(_.disconnect()) }

    promise.future
  }
}
