package cirrus.clients

import java.net.URLEncoder

import cirrus.internal._
import cirrus.{`Content-Type`, `application/x-www-form-urlencoded`}

import scala.concurrent.Future

object BasicHTTP {

  case class HEAD(address: String)(implicit val client: Client) extends VoidVerb

  case class GET(address: String)(implicit val client: Client) extends EmptyVerb

  case class PUT(address: String)(implicit val client: Client) extends LoadedVerb

  case class POST(address: String)(implicit val client: Client) extends LoadedVerb

  case class DELETE(address: String)(implicit val client: Client) extends EmptyVerb


  trait VoidVerb extends HTTPVerb {

    implicit val ec = client.ec

    def send = client connect BasicRequest(method, address, headers, params) map asEmpty
  }


  trait EmptyVerb extends HTTPVerb {

    implicit val ec = client.ec

    def send = client connect BasicRequest(method, address, headers, params) map asBasic
  }


  trait LoadedVerb extends HTTPVerb {

    implicit val ec = client.ec

    def send(payload: String) = client connect BasicRequest(method, address, headers, params, Some(payload)) map asBasic

    def send(form: Map[String, String]): Future[BasicResponse] = {

      withHeader(`Content-Type` -> `application/x-www-form-urlencoded`)

      val basicClient = client.asInstanceOf[BasicClient]

      val encode: (String) => String = URLEncoder.encode(_, basicClient.requestBodyCharset)

      val encodedPayload = form
        .map(e => (encode(e._1), encode(e._2)))
        .map(e => e._1 + "=" + e._2)
        .mkString("&")

      send(encodedPayload)
    }
  }


  case class BasicResponse(statusCode: Int, headers: Map[String, String], body: String) extends Response {
    override type Content = String
  }

  case class EmptyResponse(statusCode: Int, headers: Map[String, String])
    extends Response {

    override type Content = Option[Nothing]
    override val body: Option[Nothing] = None
  }
}
