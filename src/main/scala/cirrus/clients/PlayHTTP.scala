package cirrus.clients

import cirrus.internal.{BasicRequest, BasicClient, HTTPVerb, Response}
import cirrus.{`Accept`, `Content-Type`, `application/json`}
import play.api.libs.json.{Json, Reads, Writes}

object PlayHTTP {

  case class GET[T](address: String)(implicit val reads: Reads[T], val client: BasicClient) extends EmptyVerb[T]

  case class PUT[T](address: String)(implicit val reads: Reads[T], val client: BasicClient) extends LoadedVerb[T]

  case class POST[T](address: String)(implicit val reads: Reads[T], val client: BasicClient) extends LoadedVerb[T]

  case class DELETE[T](address: String)(implicit val reads: Reads[T], val client: BasicClient) extends EmptyVerb[T]


  trait EmptyVerb[T] extends HTTPVerb {

    implicit val reads: Reads[T]

    implicit val ec = client.ec

    def send = {

      withHeader(`Accept` -> `application/json`)

      client
        .connect(BasicRequest(method, address, headers, params))
        .map(asPlay[T])
    }
  }


  trait LoadedVerb[T] extends HTTPVerb {

    implicit val reads: Reads[T]

    implicit val ec = client.ec

    def send[F: Writes](payload: F) = {

      withHeader(`Accept` -> `application/json`) withHeader `Content-Type` -> `application/json`

      val content = Json stringify implicitly[Writes[F]].writes(payload)

      client
        .connect(BasicRequest(method, address, headers, params, Some(content)))
        .map(asPlay[T])
    }
  }


  case class PlayResponse[T: Reads](statusCode: Int, headers: Map[String, String], rawBody: String)
    extends Response {

    import play.api.libs.json.{JsResult, Json}

    override type Content = JsResult[T]
    override lazy val body = implicitly[Reads[T]].reads(Json.parse(rawBody))
  }
}
