package cirrus.clients

import cirrus.internal.{BasicClient, BasicRequest, HTTPVerb, Response}
import cirrus.{`Accept`, `Content-Type`, `application/json`}
import spray.json._

import scala.concurrent.Future

object SprayHTTP {

  case class GET[T](address: String)(implicit val reader: JsonReader[T], val client: BasicClient) extends EmptyVerb[T]

  case class PUT[T](address: String)(implicit val reader: JsonReader[T], val client: BasicClient) extends LoadedVerb[T]

  case class POST[T](address: String)(implicit val reader: JsonReader[T], val client: BasicClient) extends LoadedVerb[T]

  case class DELETE[T](address: String)(implicit val reader: JsonReader[T], val client: BasicClient) extends EmptyVerb[T]


  trait EmptyVerb[T] extends HTTPVerb {

    implicit val ec = client.ec

    implicit val reader: JsonReader[T]

    override type VerbClient = BasicClient

    withHeader (`Accept` -> `application/json`)

    def send = client connect BasicRequest(method, address, headers, params) flatMap (Future successful asSpray[T](_))
  }


  trait LoadedVerb[T] extends HTTPVerb {

    implicit val ec = client.ec

    implicit val reader: JsonReader[T]

    override type VerbClient = BasicClient

    withHeader (`Accept` -> `application/json`) withHeader `Content-Type` -> `application/json`

    def send[F: JsonWriter](payload: F) = {

      val content = implicitly[JsonWriter[F]].write(payload).compactPrint

      client connect BasicRequest(method, address, headers, params, Some(content)) flatMap (Future successful asSpray[T](_))
    }
  }


  case class SprayResponse[T: JsonReader](statusCode: Int, headers: Map[String, String], rawBody: String)
    extends Response {

    import spray.json._

    override type Content = T
    override lazy val body = implicitly[JsonReader[T]].read(rawBody.parseJson)
  }
}
