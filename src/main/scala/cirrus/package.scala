import argonaut.{DecodeJson, EncodeJson}
import cirrus.clients.{ArgonautHTTP, BasicHTTP, PlayHTTP, SprayHTTP}
import cirrus.internal.BasicClient
import play.api.libs.json.{Reads, Writes}
import spray.json.{JsonReader, JsonWriter}

package object cirrus {

  implicit val client = BasicClient()


  val `Accept` = "Accept"
  val `Accept-Charset` = "Accept-Charset"
  val `Accept-Encoding` = "Accept-Encoding"
  val `Authorization` = "Authorization"
  val `Cache-Control` = "Cache-Control"
  val `Content-Type` = "Content-Type"

  val `application/json` = "application/json"
  val `application/x-www-form-urlencoded` = "application/x-www-form-urlencoded"
  val `application/xml` = "application/xml"
  val `text/plain` = "text/plain"


  object Cirrus {

    def apply(v: BasicHTTP.VoidVerb) = v.send.map(_.body)(v.client.ec)
    def apply(v: BasicHTTP.EmptyVerb) = v.send.map(_.body)(v.client.ec)
    def apply(r: BasicPreppedRequest) = r.verb.send(r.payload).map(_.body)(r.verb.client.ec)

    def apply[T: JsonReader](v: SprayHTTP.EmptySprayVerb[T]) = v.send.map(_.body)(v.client.ec)
    def apply[T: JsonReader, F: JsonWriter](r: SprayPreppedRequest[T, F]) = r.verb.send(r.payload).map(_.body)(r.verb.client.ec)

    def apply[T: DecodeJson](v: ArgonautHTTP.EmptyArgonautVerb[T]) = v.send.map(_.body)(v.client.ec)
    def apply[T: DecodeJson, F: EncodeJson](r: ArgonautPreppedRequest[T, F]) = r.verb.send(r.payload).map(_.body)(r.verb.client.ec)

    def apply[T: Reads](v: PlayHTTP.EmptyPlayVerb[T]) = v.send.map(_.body)(v.client.ec)
    def apply[T: Reads, F: Writes](r: PlayPreppedRequest[T, F]) = r.verb.send(r.payload).map(_.body)(r.verb.client.ec)
  }


  case class BasicPreppedRequest(verb: BasicHTTP.LoadedVerb, payload: String)

  case class SprayPreppedRequest[T, F](verb: SprayHTTP.LoadedSprayVerb[T], payload: F)

  case class ArgonautPreppedRequest[T, F](verb: ArgonautHTTP.LoadedArgonautVerb[T], payload: F)

  case class PlayPreppedRequest[T, F](verb: PlayHTTP.LoadedPlayVerb[T], payload: F)


  implicit class ColdBasicRequest(verb: BasicHTTP.LoadedVerb) {
    def !(payload: String) = BasicPreppedRequest(verb, payload)
  }

  implicit class ColdSprayRequest[T](verb: SprayHTTP.LoadedSprayVerb[T]) {
    def ![F](payload: F) = SprayPreppedRequest[T, F](verb, payload)
  }

  implicit class ColdArgonautRequest[T](verb: ArgonautHTTP.LoadedArgonautVerb[T]) {
    def ![F](payload: F) = ArgonautPreppedRequest[T, F](verb, payload)
  }

  implicit class ColdPlayRequest[T](verb: PlayHTTP.LoadedPlayVerb[T]) {
    def ![F](payload: F) = PlayPreppedRequest[T, F](verb, payload)
  }
}
