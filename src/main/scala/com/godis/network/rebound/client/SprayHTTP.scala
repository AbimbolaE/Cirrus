package com.godis.network.rebound.client

import com.godis.network.rebound.core._
import spray.json._

import scala.concurrent.{ExecutionContext, Future}

object SprayHTTP {

  case class GET[T](address: String)(implicit val reader: JsonReader[T], val ec: ExecutionContext)
    extends EmptySprayRequest[T]

  case class POST[T](address: String)(implicit val reader: JsonReader[T], val ec: ExecutionContext)
    extends LoadedSprayRequest[T]

  case class PUT[T](address: String)(implicit val reader: JsonReader[T], val ec: ExecutionContext)
    extends LoadedSprayRequest[T]

  case class DELETE[T](address: String)(implicit val reader: JsonReader[T], val ec: ExecutionContext)
    extends EmptySprayRequest[T]


  trait EmptySprayRequest[T] extends HTTPVerb {

    implicit val ec: ExecutionContext
    implicit val reader: JsonReader[T]

    def send(): Future[Response] = {

      val verb  = if (method == "GET") BasicHTTP GET address else BasicHTTP DELETE address

      verb headers this.headers
      verb send() map JSONResponse.create[T]
    }

    def ! = send()
  }


  trait LoadedSprayRequest[T] extends HTTPVerb {

    implicit val ec: ExecutionContext
    implicit val reader: JsonReader[T]

    def send[F : JsonWriter](payload: F): Future[Response] = {

      val content = implicitly[JsonWriter[F]].write(payload).compactPrint

      val verb  = if (method == "POST") BasicHTTP POST address else BasicHTTP PUT address

      verb headers this.headers
      verb send content map JSONResponse.create[T]
    }

    def ![F : JsonWriter](payload: F) = send[F](payload)
  }


  case class JSONResponse[T: JsonReader](statusCode: Int, headers: Map[String, String], rawBody: String) extends Response {

    override type Content = T
    override def body: Content = implicitly[JsonReader[T]].read(rawBody.parseJson)
  }


  object JSONResponse {
    def create[T: JsonReader](response: Response) = {

      val simpleResponse = response.asInstanceOf[BasicResponse]
      JSONResponse[T](simpleResponse.statusCode, simpleResponse.headers, simpleResponse.body)
    }
  }
}
