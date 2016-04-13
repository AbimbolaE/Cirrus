package cirrus.clients

import argonaut.Argonaut._
import argonaut._
import cirrus.internal._
import cirrus.{`Accept`, `Content-Type`, `application/json`}

import scala.concurrent.Future

object ArgonautHTTP {
  
  case class GET[T](address: String)(implicit val decoder: DecodeJson[T], val client: BasicClient) extends EmptyVerb[T]

  case class PUT[T](address: String)(implicit val decoder: DecodeJson[T], val client: BasicClient) extends LoadedVerb[T]

  case class POST[T](address: String)(implicit val decoder: DecodeJson[T], val client: BasicClient) extends LoadedVerb[T]

  case class DELETE[T](address: String)(implicit val decoder: DecodeJson[T], val client: BasicClient) extends EmptyVerb[T]


  trait EmptyVerb[T] extends HTTPVerb {

    implicit val ec = client.ec

    implicit val decoder: DecodeJson[T]

    override type VerbClient = BasicClient

    withHeader (`Accept` -> `application/json`)

    def send = client connect BasicRequest(method, address, headers, params) flatMap (Future successful asArgonaut[T](_))
  }


  trait LoadedVerb[T] extends HTTPVerb {

    implicit val ec = client.ec

    implicit val decoder: DecodeJson[T]

    override type VerbClient = BasicClient

    withHeader (`Accept` -> `application/json`) withHeader `Content-Type` -> `application/json`

    def send[F: EncodeJson](payload: F) = {

      val content = payload.asJson.nospaces

      client connect BasicRequest(method, address, headers, params, Some(content)) flatMap (Future successful asArgonaut[T](_))
    }
  }


  case class ArgonautResponse[T: DecodeJson](statusCode: Int, headers: Map[String, String], rawBody: String)
    extends Response {

    import argonaut._
    import Argonaut._

    override type Content = Option[T]
    override lazy val body = rawBody.decodeOption(implicitly[DecodeJson[T]])
  }
}
