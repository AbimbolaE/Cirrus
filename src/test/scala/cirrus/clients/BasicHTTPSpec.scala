package cirrus.clients

import cirrus.clients.BasicHTTP._
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock._
import org.specs2.Specification
import org.specs2.concurrent.ExecutionEnv
import org.specs2.matcher.ThrownExpectations
import org.specs2.specification.ExecutionEnvironment
import utils.WireMockContext

import scala.concurrent.Await
import scala.concurrent.duration._

class BasicHTTPSpec extends Specification with WireMockContext with ExecutionEnvironment with ThrownExpectations {

  def is(implicit ee: ExecutionEnv) = s2"""

    The Basic HTTP features:
      A HEAD request should return a valid response         ${ `A HEAD request should return a valid response` }
      A GET request should return a valid response          ${ `A GET request should return a valid response` }
      A PUT request should return a valid response          ${ `A PUT request should return a valid response` }
      A POST request should return a valid response         ${ `A POST request should return a valid response` }
      A DELETE request should return a valid response       ${ `A DELETE request should return a valid response` }
  """


  def `A HEAD request should return a valid response`(implicit ee: ExecutionEnv) = {

    stubFor(
      head(
        urlEqualTo("/basic/tests"))
        .willReturn(
          aResponse()
            .withStatus(201)
            .withHeader("foo", "bar")))

    val response = Await result(HEAD("http://localhost:8080/basic/tests") send, 5 seconds)

    response.statusCode === 201
    response.headers must havePair ("foo" -> "bar")
    response.body === None
  }


  def `A GET request should return a valid response`(implicit ee: ExecutionEnv) = {

    stubFor(
      get(
        urlEqualTo("/basic/tests"))
        .willReturn(
          aResponse()
            .withStatus(201)
            .withHeader("foo", "bar")
            .withBody("OK...")))

    val response = Await result(GET("http://localhost:8080/basic/tests") send, 5 seconds)

    response.statusCode === 201
    response.headers must havePair ("foo" -> "bar")
    response.body === "OK..."
  }


  def `A PUT request should return a valid response`(implicit ee: ExecutionEnv) = {

    stubFor(
      put(
        urlEqualTo("/basic/tests"))
        .withRequestBody(WireMock.matching("Hello"))
        .willReturn(
          aResponse()
            .withStatus(201)
            .withHeader("foo", "bar")
            .withBody("Hi there...")))

    val response = Await result(PUT("http://localhost:8080/basic/tests") send "Hello", 5 seconds)

    response.statusCode === 201
    response.headers must havePair ("foo" -> "bar")
    response.body === "Hi there..."
  }


  def `A POST request should return a valid response`(implicit ee: ExecutionEnv) = {

    stubFor(
      post(
        urlEqualTo("/basic/tests"))
        .withRequestBody(WireMock.matching("Hello"))
        .willReturn(
          aResponse()
            .withStatus(201)
            .withHeader("foo", "bar")
            .withBody("Hi there...")))

    val response = Await result(POST("http://localhost:8080/basic/tests") send "Hello", 5 seconds)

    response.statusCode === 201
    response.headers must havePair ("foo" -> "bar")
    response.body === "Hi there..."
  }


  def `A DELETE request should return a valid response`(implicit ee: ExecutionEnv) = {

    stubFor(
      delete(
        urlEqualTo("/basic/tests"))
        .willReturn(
          aResponse()
            .withStatus(201)
            .withHeader("foo", "bar")
            .withBody("OK...")))

    val response = Await result(DELETE("http://localhost:8080/basic/tests") send, 5 seconds)

    response.statusCode === 201
    response.headers must havePair ("foo" -> "bar")
    response.body === "OK..."
  }
}
