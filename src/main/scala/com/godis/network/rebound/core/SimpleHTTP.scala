package com.godis.network.rebound.core

import scala.collection.mutable.ListBuffer
import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by esurua01 on 11/03/2016.
  */
trait Request {

  def method: String
  def address: String
  def headers: List[(String, String)]
  def body: Option[String]
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


trait HTTPVerb {

  val address: String
  val method = getClass.getSimpleName

  protected val requestHeaders = ListBuffer.empty[(String, String)]
  protected def headers = requestHeaders.toList

  def header(entry: (String, String)) = requestHeaders += entry
  def headers(entries: TraversableOnce[(String, String)]) = requestHeaders ++= entries
}
