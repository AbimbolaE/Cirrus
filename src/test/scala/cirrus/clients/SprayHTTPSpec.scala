package cirrus.clients

import cirrus.clients.SprayHTTP.{DELETE, GET, POST, PUT}
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock._
import org.specs2.Specification
import org.specs2.concurrent.ExecutionEnv
import org.specs2.matcher.ThrownExpectations
import org.specs2.specification.ExecutionEnvironment
import spray.json._
import utils.{User, WireMockContext}

import scala.concurrent.Await
import scala.concurrent.duration._

class SprayHTTPSpec extends Specification with WireMockContext with ExecutionEnvironment with ThrownExpectations {


  def is(implicit ee: ExecutionEnv) = s2"""

    The Spray HTTP features:
      A GET request should return a valid response          ${ `A GET request should return a valid response` }
      A PUT request should return a valid response          ${ `A PUT request should return a valid response` }
      A POST request should return a valid response         ${ `A POST request should return a valid response` }
      A DELETE request should return a valid response       ${ `A DELETE request should return a valid response` }
  """

  val abim = User("AbimbolaE", "male", "07831929972", "abimbolaesuruoso@gmail.com")


  def `A GET request should return a valid response`(implicit ee: ExecutionEnv) = {

    stubFor(
      get(
        urlEqualTo("/spray/tests"))
        .willReturn(
          aResponse()
            .withStatus(201)
            .withHeader("foo", "bar")
            .withBody(abim.toJson.compactPrint)))

    val response = Await result(GET[User]("http://localhost:8080/spray/tests") send, 5 seconds)

    response.statusCode === 201
    response.headers must havePair ("foo" -> "bar")
    response.body === abim
  }


  def `A PUT request should return a valid response`(implicit ee: ExecutionEnv) = {

    stubFor(
      put(
        urlEqualTo("/spray/tests"))
        .withRequestBody(WireMock.equalTo(abim.toJson.compactPrint))
        .willReturn(
          aResponse()
            .withStatus(201)
            .withHeader("foo", "bar")
            .withBody(abim.toJson.compactPrint)))

    val response = Await result(PUT[User]("http://localhost:8080/spray/tests") send abim, 5 seconds)

    response.statusCode === 201
    response.headers must havePair ("foo" -> "bar")
    response.body === abim
  }


  def `A POST request should return a valid response`(implicit ee: ExecutionEnv) = {

    stubFor(
      post(
        urlEqualTo("/spray/tests"))
        .withRequestBody(WireMock.equalTo(abim.toJson.compactPrint))
        .willReturn(
          aResponse()
            .withStatus(201)
            .withHeader("foo", "bar")
            .withBody(abim.toJson.compactPrint)))

    val response = Await result(POST[User]("http://localhost:8080/spray/tests") send abim, 5 seconds)

    response.statusCode === 201
    response.headers must havePair ("foo" -> "bar")
    response.body === abim
  }


  def `A DELETE request should return a valid response`(implicit ee: ExecutionEnv) = {

    stubFor(
      delete(
        urlEqualTo("/spray/tests"))
        .willReturn(
          aResponse()
            .withStatus(201)
            .withHeader("foo", "bar")
            .withBody(abim.toJson.compactPrint)))

    val response = Await result(DELETE[User]("http://localhost:8080/spray/tests") send, 5 seconds)

    response.statusCode === 201
    response.headers must havePair ("foo" -> "bar")
    response.body === abim
  }
}
