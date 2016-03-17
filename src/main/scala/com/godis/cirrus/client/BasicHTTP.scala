package com.godis.cirrus.client

import java.net.URLEncoder

import com.godis.cirrus.Defaults.Headers._
import com.godis.cirrus.core.{BasicClient, BasicRequest, HTTPVerb, Response}

import scala.concurrent.Future

object BasicHTTP {

  case class GET(address: String)(implicit val client: BasicClient) extends EmptyVerb

  case class POST(address: String)(implicit val client: BasicClient) extends LoadedVerb

  case class PUT(address: String)(implicit val client: BasicClient) extends LoadedVerb

  case class DELETE(address: String)(implicit val client: BasicClient) extends EmptyVerb


  trait EmptyVerb extends HTTPVerb {

    def send() = client connect BasicRequest(method = method, address = address, headers = headers, params = params)

    def ! = send()
  }


  trait LoadedVerb extends HTTPVerb {

    def send(payload: String) = client connect BasicRequest(method, address, headers, params, Some(payload))

    def send(form: Map[String, String]): Future[Response] = {
      withHeader(`Content-Type` -> `application/x-www-form-urlencoded`)

      val encoder: (String) => String = URLEncoder.encode(_, client.requestBodyCharset)

      val encodedPayload = form
        .map(h => (encoder(h._1), encoder(h._2)))
        .map(h => h._1 + "=" + h._2)
        .mkString("&")

      send(encodedPayload)
    }

    def !(payload: String) = send(payload)
  }
}
