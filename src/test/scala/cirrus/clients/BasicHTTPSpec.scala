package cirrus.clients

import cirrus.clients.BasicHTTP.{POST, DELETE, GET, PUT}
import cirrus.internal.Implicits.client
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock._
import org.specs2.Specification
import org.specs2.concurrent.ExecutionEnv
import org.specs2.matcher.ThrownExpectations
import org.specs2.specification.{BeforeAfterAll, ExecutionEnvironment}

import scala.concurrent.Await
import scala.concurrent.duration._

class BasicHTTPSpec extends Specification with BeforeAfterAll with ExecutionEnvironment with ThrownExpectations {

  def is(implicit ee: ExecutionEnv) = s2"""

    The Basic HTTP features:
      A GET request should return a valid response          ${ `A GET request should return a valid response` }
      A PUT request should return a valid response          ${ `A PUT request should return a valid response` }
      A POST request should return a valid response         ${ `A POST request should return a valid response` }
      A DELETE request should return a valid response       ${ `A DELETE request should return a valid response` }

      Chill out ${ `Chill out` }
  """

  val wireMockServerOpt = Some(new WireMockServer())

  override def beforeAll() { wireMockServerOpt foreach (_.start()) }

  override def afterAll() { wireMockServerOpt foreach (_.stop()) }


  def `Chill out` = {
//    Thread.sleep(30000)
    1 === 1
  }


  def `A GET request should return a valid response`(implicit ee: ExecutionEnv) = {

    stubFor(
      get(
        urlEqualTo("/tests"))
        .willReturn(
          aResponse()
            .withStatus(201)
            .withHeader("foo", "bar")
            .withBody("OK...")))

    val response = Await result(GET("http://localhost:8080/tests") send, 5 seconds)

    println(response)

    response.statusCode === 201
    response.headers must havePair ("foo" -> "bar")
    response.body === "OK..."
  }


  def `A PUT request should return a valid response`(implicit ee: ExecutionEnv) = {

    stubFor(
      put(
        urlEqualTo("/tests"))
        .withRequestBody(WireMock.matching("Hello"))
        .willReturn(
          aResponse()
            .withStatus(201)
            .withHeader("foo", "bar")
            .withBody("Hi there...")))

    val response = Await result(PUT("http://localhost:8080/tests") send "Hell", 5 seconds)

    println(response)

    response.statusCode === 201
    response.headers must havePair ("foo" -> "bar")
    response.body === "Hi there..."
  }


  def `A POST request should return a valid response`(implicit ee: ExecutionEnv) = {

    stubFor(
      post(
        urlEqualTo("/tests"))
        .withRequestBody(WireMock.matching("Hello"))
        .willReturn(
          aResponse()
            .withStatus(201)
            .withHeader("foo", "bar")
            .withBody("Hi there...")))

    val response = Await result(POST("http://localhost:8080/tests") send "Hello", 5 seconds)

    println(response)

    response.statusCode === 201
    response.headers must havePair ("foo" -> "bar")
    response.body === "Hi there..."
  }


  def `A DELETE request should return a valid response`(implicit ee: ExecutionEnv) = {

    stubFor(
      delete(
        urlEqualTo("/tests"))
        .willReturn(
          aResponse()
            .withStatus(201)
            .withHeader("foo", "bar")
            .withBody("OK...")))

    val response = Await result(DELETE("http://localhost:8080/tests") send, 5 seconds)

    println(response)

    response.statusCode === 201
    response.headers must havePair ("foo" -> "bar")
    response.body === "OK..."
  }
}
