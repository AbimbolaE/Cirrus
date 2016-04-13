package cirrus

import argonaut.DecodeJson
import cirrus.clients.ArgonautHTTP.ArgonautResponse
import cirrus.clients.BasicHTTP.{BasicResponse, EmptyResponse}
import cirrus.clients.PlayHTTP.PlayResponse
import cirrus.clients.SprayHTTP.SprayResponse
import play.api.libs.json.Reads
import spray.json.JsonReader

package object clients {

  def asEmpty(response: BasicResponse) = EmptyResponse(response.statusCode, response.headers)

  def asPlay[T: Reads](response: BasicResponse) = PlayResponse[T](response.statusCode, response.headers, response.body)

  def asSpray[T: JsonReader](response: BasicResponse) = SprayResponse[T](response.statusCode, response.headers, response.body)

  def asArgonaut[T: DecodeJson](response: BasicResponse) = ArgonautResponse[T](response.statusCode, response.headers, response.body)
}
