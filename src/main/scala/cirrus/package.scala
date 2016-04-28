import argonaut.{DecodeJson, EncodeJson}
import cirrus.clients.{ArgonautHTTP, BasicHTTP, PlayHTTP, SprayHTTP}
import cirrus.internal.{BasicClient, FailedRequest}
import play.api.libs.json._
import spray.json.{DeserializationException, JsonReader, JsonWriter}

import scala.concurrent.Future

package object cirrus {

  implicit val _ = BasicClient()

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

    def apply(v: BasicHTTP.VoidVerb) = {
      implicit val ec = v.client.ec
      v.send map (_.body)
    }
    
    def apply(v: BasicHTTP.EmptyVerb) = {
      implicit val ec = v.client.ec
      v.send map (_.body)
    }
    
    def apply(r: BasicPreppedRequest) = {
      implicit val ec = r.verb.client.ec
      r.verb send r.payload map (_.body)
    }


    def apply[T: JsonReader](v: SprayHTTP.EmptyVerb[T]) = {
      implicit val ec = v.client.ec
      v.send map (_.body) recoverWith sprayHandler
    }

    def apply[T: JsonReader, F: JsonWriter](r: SprayPreppedRequest[T, F]) = {
      implicit val ec = r.verb.client.ec
      r.verb send r.payload map (_.body) recoverWith sprayHandler
    }


    def apply[T: DecodeJson](v: ArgonautHTTP.EmptyVerb[T]) = {
      implicit val ec = v.client.ec
      v.send map (_.body.get) recoverWith argonautHandler
    }

    def apply[T: DecodeJson, F: EncodeJson](r: ArgonautPreppedRequest[T, F]) = {
      implicit val ec = r.verb.client.ec
      r.verb send r.payload map (_.body.get) recoverWith argonautHandler
    }


    def apply[T: Reads](v: PlayHTTP.EmptyVerb[T]) = {
      implicit val ec = v.client.ec
      v.send map (_.body) flatMap playHandler
    }

    def apply[T: Reads, F: Writes](r: PlayPreppedRequest[T, F]) = {
      implicit val ec = r.verb.client.ec
      r.verb send r.payload map (_.body) flatMap playHandler
    }


    private def sprayHandler[T]: PartialFunction[Throwable, Future[T]] = {
      case e: DeserializationException => Future.failed[T](FailedRequest(e))
    }

    private def argonautHandler[T]: PartialFunction[Throwable, Future[T]] = {
      case e: NoSuchElementException => Future.failed[T](FailedRequest(e))
    }

    private def playHandler[T]: PartialFunction[JsResult[T], Future[T]] = {
      case JsError(errors) => Future.failed[T](FailedRequest(errors))
      case JsSuccess(v, p) => Future.successful(v)
    }
  }


  private[cirrus] case class BasicPreppedRequest(verb: BasicHTTP.LoadedVerb, payload: String)

  private[cirrus] case class SprayPreppedRequest[T, F](verb: SprayHTTP.LoadedVerb[T], payload: F)

  private[cirrus] case class ArgonautPreppedRequest[T, F](verb: ArgonautHTTP.LoadedVerb[T], payload: F)

  private[cirrus] case class PlayPreppedRequest[T, F](verb: PlayHTTP.LoadedVerb[T], payload: F)


  implicit class ColdBasicRequest(verb: BasicHTTP.LoadedVerb) {

    def withPayload(payload: String) = BasicPreppedRequest(verb, payload)

    def !(payload: String) = withPayload(payload)
  }

  implicit class ColdSprayRequest[T](verb: SprayHTTP.LoadedVerb[T]) {

    def withPayload[F](payload: F) = SprayPreppedRequest[T, F](verb, payload)

    def ![F](payload: F) = withPayload(payload)
  }

  implicit class ColdArgonautRequest[T](verb: ArgonautHTTP.LoadedVerb[T]) {

    def withPayload[F](payload: F) = ArgonautPreppedRequest[T, F](verb, payload)

    def ![F](payload: F) = withPayload(payload)
  }

  implicit class ColdPlayRequest[T](verb: PlayHTTP.LoadedVerb[T]) {

    def withPayload[F](payload: F) = PlayPreppedRequest[T, F](verb, payload)

    def ![F](payload: F) = withPayload(payload)
  }
}
