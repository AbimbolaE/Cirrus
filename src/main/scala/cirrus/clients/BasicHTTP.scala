package cirrus.clients

import java.net.URLEncoder

import cirrus.internal.Headers._
import cirrus.internal.{BasicClient, BasicRequest, HTTPVerb, Response}

import scala.concurrent.Future

object BasicHTTP {

  case class HEAD(address: String)(implicit val client: BasicClient) extends EmptyVerb

  case class GET(address: String)(implicit val client: BasicClient) extends EmptyVerb

  case class PUT(address: String)(implicit val client: BasicClient) extends LoadedVerb

  case class POST(address: String)(implicit val client: BasicClient) extends LoadedVerb

  case class DELETE(address: String)(implicit val client: BasicClient) extends EmptyVerb


  trait VoidVerb extends EmptyVerb {

    import scala.concurrent.ExecutionContext.Implicits.global

    override def send: Future[Response] = super.send map ResponseBuilder.asEmpty
  }


  trait EmptyVerb extends HTTPVerb {

    def send = client connect BasicRequest(method = method, address = address, headers = headers, params = params)

    def ! = send
  }


  trait LoadedVerb extends HTTPVerb {

    def send(payload: String) = client connect BasicRequest(method, address, headers, params, Some(payload))

    def send(form: Map[String, String]): Future[Response] = {
      withHeader(`Content-Type` -> `application/x-www-form-urlencoded`)

      val encode: (String) => String = URLEncoder.encode(_, client.requestBodyCharset)

      val encodedPayload = form
        .map(e => (encode(e._1), encode(e._2)))
        .map(e => e._1 + "=" + e._2)
        .mkString("&")

      send(encodedPayload)
    }

    def !(payload: String) = send(payload)

    def !(form: Map[String, String]) = send(form)
  }
}
