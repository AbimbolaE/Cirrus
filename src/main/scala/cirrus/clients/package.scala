package cirrus

import argonaut.DecodeJson
import cirrus.clients.ArgonautHTTP.ArgonautResponse
import cirrus.clients.BasicHTTP.{BasicResponse, EmptyResponse}
import cirrus.clients.PlayHTTP.PlayResponse
import cirrus.clients.SprayHTTP.SprayResponse
import cirrus.internal.Response
import play.api.libs.json.Reads
import spray.json.JsonReader

package object clients {

  def asBasic(response: Response) = response.asInstanceOf[BasicResponse]

  def asEmpty(response: Response) = EmptyResponse(response.statusCode, response.headers)

  def asSpray[T: JsonReader](response: Response) = {

    val basicResponse = asBasic(response)
    SprayResponse[T](basicResponse.statusCode, basicResponse.headers, basicResponse.body)
  }

  def asArgonaut[T: DecodeJson](response: Response) = {

    val basicResponse = asBasic(response)
    ArgonautResponse[T](basicResponse.statusCode, basicResponse.headers, basicResponse.body)
  }

  def asPlay[T: Reads](response: Response) = {

    val basicResponse = asBasic(response)
    PlayResponse[T](basicResponse.statusCode, basicResponse.headers, basicResponse.body)
  }
}
