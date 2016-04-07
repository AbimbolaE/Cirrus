package cirrus.clients

import argonaut.Argonaut._
import argonaut._
import cirrus.internal.Headers._
import cirrus.internal.{BasicClient, HTTPVerb}

/**
 * Created by Abim on 30/03/2016.
 */
object ArgonautHTTP {
  
  case class GET[T](address: String)(implicit val decoder: DecodeJson[T], val client: BasicClient)
    extends EmptyArgonautVerb[T]

  case class PUT[T](address: String)(implicit val decoder: DecodeJson[T], val client: BasicClient)
    extends LoadedArgonautVerb[T]

  case class POST[T](address: String)(implicit val decoder: DecodeJson[T], val client: BasicClient)
    extends LoadedArgonautVerb[T]

  case class DELETE[T](address: String)(implicit val decoder: DecodeJson[T], val client: BasicClient)
    extends EmptyArgonautVerb[T]


  trait EmptyArgonautVerb[T] extends HTTPVerb {

    implicit val decoder: DecodeJson[T]

    def send = {

      implicit val ec = client.ec

      val verb = if (method == "GET") BasicHTTP GET address else BasicHTTP DELETE address

      verb withHeaders this.headers
      verb withHeader `Accept` -> `application/json`
      verb.send map ResponseBuilder.asArgonaut[T]
    }
  }


  trait LoadedArgonautVerb[T] extends HTTPVerb {

    implicit val decoder: DecodeJson[T]

    def send[F: EncodeJson](payload: F) = {

      implicit val ec = client.ec

      val content = payload.asJson.nospaces

      val verb = if (method == "POST") BasicHTTP POST address else BasicHTTP PUT address

      verb withHeaders this.headers
      verb withHeader `Content-Type` -> `application/json` withHeader `Accept` -> `application/json`
      verb send content map ResponseBuilder.asArgonaut[T]
    }
  }
}
