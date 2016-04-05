package com.godis.cirrus

import argonaut.DecodeJson
import com.godis.cirrus.core.{BasicResponse, Response}
import spray.json.JsonReader

/**
 * Created by Abim on 31/03/2016.
 */

package object client {

  object JSONBuilder {

    def usingSpray[T: JsonReader](response: Response) = {

      val simpleResponse = response.asInstanceOf[BasicResponse]
      SprayResponse[T](simpleResponse.statusCode, simpleResponse.headers, simpleResponse.body)
    }

    def usingArgonaut[T: DecodeJson](response: Response) = {

      val simpleResponse = response.asInstanceOf[BasicResponse]
      ArgonautResponse[T](simpleResponse.statusCode, simpleResponse.headers, simpleResponse.body)
    }
  }

  case class SprayResponse[T: JsonReader](statusCode: Int, headers: Map[String, String], rawBody: String)
    extends Response {

    import spray.json._

    override type Content = T
    override lazy val body: Content = implicitly[JsonReader[T]].read(rawBody.parseJson)
  }

  case class ArgonautResponse[T: DecodeJson](statusCode: Int, headers: Map[String, String], rawBody: String)
    extends Response {

    import argonaut._, Argonaut._

    override type Content = T
    override lazy val body: Content = rawBody.decodeOption(implicitly[DecodeJson[T]]).get
  }
}
