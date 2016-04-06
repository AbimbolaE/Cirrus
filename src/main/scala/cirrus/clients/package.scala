package cirrus

import argonaut.DecodeJson
import cirrus.internal.{BasicResponse, Response}
import spray.json.JsonReader

/**
 * Created by Abim on 31/03/2016.
 */

package object clients {

  object ResponseBuilder {

    def asEmpty(response: Response) = EmptyResponse(response.statusCode, response.headers)

    def asSpray[T: JsonReader](response: Response) = {

      val basicResponse = response.asInstanceOf[BasicResponse]
      SprayResponse[T](basicResponse.statusCode, basicResponse.headers, basicResponse.body)
    }

    def asArgonaut[T: DecodeJson](response: Response) = {

      val basicResponse = response.asInstanceOf[BasicResponse]
      ArgonautResponse[T](basicResponse.statusCode, basicResponse.headers, basicResponse.body)
    }
  }

  case class EmptyResponse(statusCode: Int, headers: Map[String, String])
    extends Response {

    override type Content = None.type
    override val body = None
  }

  case class SprayResponse[T: JsonReader](statusCode: Int, headers: Map[String, String], rawBody: String)
    extends Response {

    import spray.json._

    override type Content = T
    override lazy val body: Content = implicitly[JsonReader[T]].read(rawBody.parseJson)
  }

  case class ArgonautResponse[T: DecodeJson](statusCode: Int, headers: Map[String, String], rawBody: String)
    extends Response {

    import argonaut._
    import Argonaut._

    override type Content = T
    override lazy val body: Content = rawBody.decodeOption(implicitly[DecodeJson[T]]).get
  }
}
