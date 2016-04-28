package cirrus.clients

import cirrus.clients.PlayHTTP.{DELETE, GET, POST, PUT}
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock._
import org.specs2.Specification
import org.specs2.concurrent.ExecutionEnv
import org.specs2.matcher.ThrownExpectations
import org.specs2.specification.ExecutionEnvironment
import play.api.libs.json.JsSuccess
import play.api.libs.json.Json._
import utils.User._
import utils.{User, WireMockContext}

import scala.concurrent.Await
import scala.concurrent.duration._

class PlayHTTPSpec extends Specification with WireMockContext with ExecutionEnvironment with ThrownExpectations {


  def is(implicit ee: ExecutionEnv) = s2"""

    The Play HTTP features:
      A GET request should return a valid response          ${ `A GET request should return a valid response` }
      A PUT request should return a valid response          ${ `A PUT request should return a valid response` }
      A POST request should return a valid response         ${ `A POST request should return a valid response` }
      A DELETE request should return a valid response       ${ `A DELETE request should return a valid response` }
  """

  val abim = User("AbimbolaE", "male", "07831929972", "abimbolaesuruoso@gmail.com")


  def `A GET request should return a valid response`(implicit ee: ExecutionEnv) = {

    stubFor(
      get(
        urlEqualTo("/play/tests"))
        .willReturn(
          aResponse()
            .withStatus(201)
            .withHeader("foo", "bar")
            .withBody(stringify(userPlayFormat.writes(abim)))))

    val response = Await result(GET[User]("http://localhost:8080/play/tests") send, 5 seconds)

    response.statusCode === 201
    response.headers must havePair ("foo" -> "bar")
    response.body === JsSuccess(abim)
  }


  def `A PUT request should return a valid response`(implicit ee: ExecutionEnv) = {

    stubFor(
      put(
        urlEqualTo("/play/tests"))
        .withRequestBody(WireMock.equalTo(stringify(userPlayFormat.writes(abim))))
        .willReturn(
          aResponse()
            .withStatus(201)
            .withHeader("foo", "bar")
            .withBody(stringify(userPlayFormat.writes(abim)))))

    val response = Await result(PUT[User]("http://localhost:8080/play/tests") send abim, 5 seconds)

    response.statusCode === 201
    response.headers must havePair ("foo" -> "bar")
    response.body === JsSuccess(abim)
  }


  def `A POST request should return a valid response`(implicit ee: ExecutionEnv) = {

    stubFor(
      post(
        urlEqualTo("/play/tests"))
        .withRequestBody(WireMock.equalTo(stringify(userPlayFormat.writes(abim))))
        .willReturn(
          aResponse()
            .withStatus(201)
            .withHeader("foo", "bar")
            .withBody(stringify(userPlayFormat.writes(abim)))))

    val response = Await result(POST[User]("http://localhost:8080/play/tests") send abim, 5 seconds)

    response.statusCode === 201
    response.headers must havePair ("foo" -> "bar")
    response.body === JsSuccess(abim)
  }


  def `A DELETE request should return a valid response`(implicit ee: ExecutionEnv) = {

    stubFor(
      delete(
        urlEqualTo("/play/tests"))
        .willReturn(
          aResponse()
            .withStatus(201)
            .withHeader("foo", "bar")
            .withBody(stringify(userPlayFormat.writes(abim)))))

    val response = Await result(DELETE[User]("http://localhost:8080/play/tests") send, 5 seconds)

    response.statusCode === 201
    response.headers must havePair ("foo" -> "bar")
    response.body === JsSuccess(abim)
  }
}
