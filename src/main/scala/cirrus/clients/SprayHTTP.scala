package cirrus.clients

import cirrus.internal.{BasicClient, HTTPVerb, Response}
import cirrus.{`Accept`, `Content-Type`, `application/json`}
import spray.json._

object SprayHTTP {

  case class GET[T](address: String)(implicit val reader: JsonReader[T], val client: BasicClient) extends EmptyVerb[T]

  case class PUT[T](address: String)(implicit val reader: JsonReader[T], val client: BasicClient) extends LoadedVerb[T]

  case class POST[T](address: String)(implicit val reader: JsonReader[T], val client: BasicClient) extends LoadedVerb[T]

  case class DELETE[T](address: String)(implicit val reader: JsonReader[T], val client: BasicClient) extends EmptyVerb[T]


  trait EmptyVerb[T] extends HTTPVerb {

    implicit val reader: JsonReader[T]

    def send = {

      implicit val ec = client.ec

      val verb = if (method == "GET") BasicHTTP GET address else BasicHTTP DELETE address

      verb withHeaders this.headers
      verb withHeader `Accept` -> `application/json`
      verb.send map asSpray[T]
    }
  }


  trait LoadedVerb[T] extends HTTPVerb {

    implicit val reader: JsonReader[T]

    def send[F: JsonWriter](payload: F) = {

      implicit val ec = client.ec

      val content = implicitly[JsonWriter[F]].write(payload).compactPrint

      val verb = if (method == "POST") BasicHTTP POST address else BasicHTTP PUT address

      verb withHeaders this.headers
      verb withHeader `Content-Type` -> `application/json` withHeader `Accept` -> `application/json`
      verb send content map asSpray[T]
    }
  }


  case class SprayResponse[T: JsonReader](statusCode: Int, headers: Map[String, String], rawBody: String)
    extends Response {

    import spray.json._

    override type Content = T
    override lazy val body = implicitly[JsonReader[T]].read(rawBody.parseJson)
  }
}
