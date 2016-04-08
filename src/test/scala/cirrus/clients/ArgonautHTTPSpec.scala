package cirrus.clients

import cirrus.clients.ArgonautHTTP.{DELETE, GET, POST, PUT}
import cirrus.internal.Implicits.client
import cirrus.utils.{User, WireMockContext}
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock._
import org.specs2.Specification
import org.specs2.concurrent.ExecutionEnv
import org.specs2.matcher.ThrownExpectations
import org.specs2.specification.ExecutionEnvironment
import argonaut._
import Argonaut._

import scala.concurrent.Await
import scala.concurrent.duration._

class ArgonautHTTPSpec extends Specification with WireMockContext with ExecutionEnvironment with ThrownExpectations {


  def is(implicit ee: ExecutionEnv) = s2"""

    The Argonaut HTTP features:
      A GET request should return a valid response          ${ `A GET request should return a valid response` }
      A PUT request should return a valid response          ${ `A PUT request should return a valid response` }
      A POST request should return a valid response         ${ `A POST request should return a valid response` }
      A DELETE request should return a valid response       ${ `A DELETE request should return a valid response` }
  """

  val abim = User("AbimbolaE", "male", "07831929972", "abimbolaesuruoso@gmail.com")

  implicit val userCodec: CodecJson[User] = casecodec4(User.apply, User.unapply)("username", "gender", "mobile", "email")


  def `A GET request should return a valid response`(implicit ee: ExecutionEnv) = {

    stubFor(
      get(
        urlEqualTo("/argonaut/tests"))
        .willReturn(
          aResponse()
            .withStatus(201)
            .withHeader("foo", "bar")
            .withBody(abim.asJson.nospaces)))

    val response = Await result(GET[User]("http://localhost:8080/argonaut/tests") send, 5 seconds)

    response.statusCode === 201
    response.headers must havePair ("foo" -> "bar")
    response.body === abim
  }


  def `A PUT request should return a valid response`(implicit ee: ExecutionEnv) = {

    stubFor(
      put(
        urlEqualTo("/argonaut/tests"))
        .withRequestBody(WireMock.equalTo(abim.asJson.nospaces))
        .willReturn(
          aResponse()
            .withStatus(201)
            .withHeader("foo", "bar")
            .withBody(abim.asJson.nospaces)))

    val response = Await result(PUT[User]("http://localhost:8080/argonaut/tests") send abim, 5 seconds)

    response.statusCode === 201
    response.headers must havePair ("foo" -> "bar")
    response.body === abim
  }


  def `A POST request should return a valid response`(implicit ee: ExecutionEnv) = {

    stubFor(
      post(
        urlEqualTo("/argonaut/tests"))
        .withRequestBody(WireMock.equalTo(abim.asJson.nospaces))
        .willReturn(
          aResponse()
            .withStatus(201)
            .withHeader("foo", "bar")
            .withBody(abim.asJson.nospaces)))

    val response = Await result(POST[User]("http://localhost:8080/argonaut/tests") send abim, 5 seconds)

    response.statusCode === 201
    response.headers must havePair ("foo" -> "bar")
    response.body === abim
  }


  def `A DELETE request should return a valid response`(implicit ee: ExecutionEnv) = {

    stubFor(
      delete(
        urlEqualTo("/argonaut/tests"))
        .willReturn(
          aResponse()
            .withStatus(201)
            .withHeader("foo", "bar")
            .withBody(abim.asJson.nospaces)))

    val response = Await result(DELETE[User]("http://localhost:8080/argonaut/tests") send, 5 seconds)

    response.statusCode === 201
    response.headers must havePair ("foo" -> "bar")
    response.body === abim
  }
}
