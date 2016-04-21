package cirrus.clients

import java.net.URLEncoder

import cirrus.internal._
import cirrus.{`Content-Type`, `application/x-www-form-urlencoded`}

import scala.concurrent.Future

object BasicHTTP {

  case class HEAD(address: String)(implicit val client: BasicClient) extends VoidVerb

  case class GET(address: String)(implicit val client: BasicClient) extends EmptyVerb

  case class PUT(address: String)(implicit val client: BasicClient) extends LoadedVerb

  case class POST(address: String)(implicit val client: BasicClient) extends LoadedVerb with LoadedFormVerb

  case class DELETE(address: String)(implicit val client: BasicClient) extends EmptyVerb


  trait VoidVerb extends HTTPVerb {

    implicit val ec = client.ec

    override type VerbClient = BasicClient

    def send = client connect BasicRequest(method, address, headers, params) flatMap (Future successful asEmpty(_))
  }


  trait EmptyVerb extends HTTPVerb {

    override type VerbClient = BasicClient

    def send = client connect BasicRequest(method, address, headers, params)
  }


  trait LoadedVerb extends HTTPVerb {

    override type VerbClient = BasicClient

    def send(payload: String) = client connect BasicRequest(method, address, headers, params, Some(payload))
  }


  trait LoadedFormVerb { self: HTTPVerb =>

    override type VerbClient = BasicClient

    def send(form: Map[String, String]) = {

      if (!headers.exists(_._1 == `Content-Type`)) withHeader(`Content-Type` -> `application/x-www-form-urlencoded`)

      val encode: (String) => String = URLEncoder.encode(_, client.requestBodyCharset)

      val encodedPayload = form map (e => (encode(e._1), encode(e._2))) map (e => e._1 + "=" + e._2) mkString "&"

      client connect BasicRequest(method, address, headers, params, Some(encodedPayload))
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
