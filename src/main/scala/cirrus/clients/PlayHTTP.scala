package cirrus.clients

import cirrus.internal._
import cirrus.{`Accept`, `Content-Type`, `application/json`}
import play.api.libs.json.{Json, Reads, Writes}

import scala.concurrent.Future

object PlayHTTP {

  case class GET[T](address: String)(implicit val reads: Reads[T], val client: BasicClient) extends EmptyVerb[T]

  case class PUT[T](address: String)(implicit val reads: Reads[T], val client: BasicClient) extends LoadedVerb[T]

  case class POST[T](address: String)(implicit val reads: Reads[T], val client: BasicClient) extends LoadedVerb[T]

  case class DELETE[T](address: String)(implicit val reads: Reads[T], val client: BasicClient) extends EmptyVerb[T]


  trait EmptyVerb[T] extends HTTPVerb {

    implicit val ec = client.ec

    implicit val reads: Reads[T]

    override type VerbClient = BasicClient

    withHeader (`Accept` -> `application/json`)

    def send = client connect BasicRequest(method, address, headers, params) flatMap (Future successful asPlay[T](_))
  }


  trait LoadedVerb[T] extends HTTPVerb {

    implicit val ec = client.ec

    implicit val reads: Reads[T]

    override type VerbClient = BasicClient

    withHeader (`Accept` -> `application/json`) withHeader `Content-Type` -> `application/json`

    def send[F: Writes](payload: F) = {

      val content = Json stringify (implicitly[Writes[F]] writes payload)

      client connect BasicRequest(method, address, headers, params, Some(content)) flatMap (Future successful asPlay[T](_))
    }
  }


  case class PlayResponse[T: Reads](statusCode: Int, headers: Map[String, String], rawBody: String)
    extends Response {

    import play.api.libs.json.{JsResult, Json}

    override type Content = JsResult[T]
    override lazy val body = implicitly[Reads[T]].reads(Json.parse(rawBody))
  }
}
