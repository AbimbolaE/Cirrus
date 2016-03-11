package com.godis.network.rebound.json

import com.godis.network.rebound.core.{Response, SimpleClient, SimpleRequest, SimpleResponse}
import spray.json._

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext}

/**
  * Created by esurua01 on 11/03/2016.
  */
object SprayClient {

  val client = SimpleClient()

  case class GET[T: JsonReader](address: String)(implicit ec: ExecutionContext = ExecutionContext.global) {
    def send = {

      val request = SimpleRequest(address = address)

      client.connect(request) map JSONResponse.create[T]
    }

    def ! = send

    def await = Await.result(send, 5 seconds)
  }
}

case class JSONResponse[T : JsonReader](statusCode: Int, headers: Map[String, String], rawBody: String) extends Response {
  override type Content = T

  override def body: Content = implicitly[JsonReader[T]].read(rawBody.parseJson)
}

object JSONResponse {
  def create[T: JsonReader](response: Response) = {
    val simpleResponse = response.asInstanceOf[SimpleResponse]
    JSONResponse[T](simpleResponse.statusCode, simpleResponse.headers, simpleResponse.body)
  }
}

