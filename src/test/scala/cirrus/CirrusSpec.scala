package cirrus

import cirrus.clients.{ArgonautHTTP, BasicHTTP, PlayHTTP, SprayHTTP}
import cirrus.utils.User._
import cirrus.utils.{User, WireMockContext}
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock._
import org.specs2.Specification
import org.specs2.concurrent.ExecutionEnv
import org.specs2.matcher.ThrownExpectations
import org.specs2.specification.ExecutionEnvironment
import play.api.libs.json.JsSuccess
import play.api.libs.json.Json._
import spray.json._

class CirrusSpec extends Specification with WireMockContext with ExecutionEnvironment with ThrownExpectations {
  
  override def is(implicit ee: ExecutionEnv) = s2"""
    
    The Cirrus features:
      A Basic HEAD request should return a valid body         ${ `A Basic HEAD request should return a valid body` }
      A Basic GET request should return a valid body          ${ `A Basic GET request should return a valid body` }
      A Basic PUT request should return a valid body          ${ `A Basic PUT request should return a valid body` }
      A Basic POST request should return a valid body         ${ `A Basic POST request should return a valid body` }
      A Basic DELETE request should return a valid body       ${ `A Basic DELETE request should return a valid body` }

      A Spray GET request should return a valid body          ${ `A Spray GET request should return a valid body` }
      A Spray PUT request should return a valid body          ${ `A Spray PUT request should return a valid body` }
      A Spray POST request should return a valid body         ${ `A Spray POST request should return a valid body` }
      A Spray DELETE request should return a valid body       ${ `A Spray DELETE request should return a valid body` }

      An Argonaut GET request should return a valid body      ${ `An Argonaut GET request should return a valid body` }
      An Argonaut PUT request should return a valid body      ${ `An Argonaut PUT request should return a valid body` }
      An Argonaut POST request should return a valid body     ${ `An Argonaut POST request should return a valid body` }
      An Argonaut DELETE request should return a valid body   ${ `An Argonaut DELETE request should return a valid body` }

      An Play GET request should return a valid body          ${ `An Play GET request should return a valid body` }
      An Play PUT request should return a valid body          ${ `An Play PUT request should return a valid body` }
      An Play POST request should return a valid body         ${ `An Play POST request should return a valid body` }
      An Play DELETE request should return a valid body       ${ `An Play DELETE request should return a valid body` }
  """

  val abim = User("AbimbolaE", "male", "07831929972", "abimbolaesuruoso@gmail.com")


  def `A Basic HEAD request should return a valid body`(implicit ee: ExecutionEnv) = {

    stubFor(
      head(
        urlEqualTo("/basic/tests"))
        .willReturn(
          aResponse()
            .withStatus(201)))

    Cirrus(BasicHTTP HEAD "http://localhost:8080/basic/tests") must be_==(None).await
  }


  def `A Basic GET request should return a valid body`(implicit ee: ExecutionEnv) = {

    stubFor(
      get(
        urlEqualTo("/basic/tests"))
        .willReturn(
          aResponse()
            .withStatus(201)
            .withBody("OK...")))

    Cirrus(BasicHTTP GET "http://localhost:8080/basic/tests") must be_==("OK...").await
  }


  def `A Basic PUT request should return a valid body`(implicit ee: ExecutionEnv) = {

    stubFor(
      put(
        urlEqualTo("/basic/tests"))
        .withRequestBody(WireMock.matching("Hello"))
        .willReturn(
          aResponse()
            .withStatus(201)
            .withBody("Hi there...")))

    Cirrus((BasicHTTP PUT "http://localhost:8080/basic/tests") ! "Hello") must be_==("Hi there...").await
  }


  def `A Basic POST request should return a valid body`(implicit ee: ExecutionEnv) = {

    stubFor(
      post(
        urlEqualTo("/basic/tests"))
        .withRequestBody(WireMock.matching("Hello"))
        .willReturn(
          aResponse()
            .withStatus(201)
            .withBody("Hi there...")))

    Cirrus((BasicHTTP POST "http://localhost:8080/basic/tests") ! "Hello") must be_==("Hi there...").await
  }


  def `A Basic DELETE request should return a valid body`(implicit ee: ExecutionEnv) = {

    stubFor(
      delete(
        urlEqualTo("/basic/tests"))
        .willReturn(
          aResponse()
            .withStatus(201)
            .withBody("OK...")))

    Cirrus(BasicHTTP DELETE "http://localhost:8080/basic/tests") must be_==("OK...").await
  }


  def `A Spray GET request should return a valid body`(implicit ee: ExecutionEnv) = {

    stubFor(
      get(
        urlEqualTo("/spray/tests"))
        .willReturn(
          aResponse()
            .withStatus(201)
            .withBody(abim.toJson.compactPrint)))

    Cirrus(SprayHTTP.GET[User]("http://localhost:8080/spray/tests")) must be_==(abim).await
  }


  def `A Spray PUT request should return a valid body`(implicit ee: ExecutionEnv) = {

    stubFor(
      put(
        urlEqualTo("/spray/tests"))
        .withRequestBody(WireMock.equalTo(abim.toJson.compactPrint))
        .willReturn(
          aResponse()
            .withStatus(201)
            .withBody(abim.toJson.compactPrint)))

    Cirrus(SprayHTTP.PUT[User]("http://localhost:8080/spray/tests") ! abim) must be_==(abim).await
  }


  def `A Spray POST request should return a valid body`(implicit ee: ExecutionEnv) = {

    stubFor(
      post(
        urlEqualTo("/spray/tests"))
        .withRequestBody(WireMock.equalTo(abim.toJson.compactPrint))
        .willReturn(
          aResponse()
            .withStatus(201)
            .withBody(abim.toJson.compactPrint)))

    Cirrus(SprayHTTP.POST[User]("http://localhost:8080/spray/tests") ! abim) must be_==(abim).await
  }


  def `A Spray DELETE request should return a valid body`(implicit ee: ExecutionEnv) = {

    stubFor(
      delete(
        urlEqualTo("/spray/tests"))
        .willReturn(
          aResponse()
            .withStatus(201)
            .withBody(abim.toJson.compactPrint)))

    Cirrus(SprayHTTP.DELETE[User]("http://localhost:8080/spray/tests")) must be_==(abim).await
  }


  def `An Argonaut GET request should return a valid body`(implicit ee: ExecutionEnv) = {

    stubFor(
      get(
        urlEqualTo("/argonaut/tests"))
        .willReturn(
          aResponse()
            .withStatus(201)
            .withBody(abim.toJson.compactPrint)))

    Cirrus(ArgonautHTTP.GET[User]("http://localhost:8080/argonaut/tests")) must be_==(Some(abim)).await
  }


  def `An Argonaut PUT request should return a valid body`(implicit ee: ExecutionEnv) = {

    stubFor(
      put(
        urlEqualTo("/argonaut/tests"))
        .withRequestBody(WireMock.equalTo(abim.toJson.compactPrint))
        .willReturn(
          aResponse()
            .withStatus(201)
            .withBody(abim.toJson.compactPrint)))

    Cirrus(ArgonautHTTP.PUT[User]("http://localhost:8080/argonaut/tests") ! abim) must be_==(Some(abim)).await
  }


  def `An Argonaut POST request should return a valid body`(implicit ee: ExecutionEnv) = {

    stubFor(
      post(
        urlEqualTo("/argonaut/tests"))
        .withRequestBody(WireMock.equalTo(abim.toJson.compactPrint))
        .willReturn(
          aResponse()
            .withStatus(201)
            .withBody(abim.toJson.compactPrint)))

    Cirrus(ArgonautHTTP.POST[User]("http://localhost:8080/argonaut/tests") ! abim) must be_==(Some(abim)).await
  }


  def `An Argonaut DELETE request should return a valid body`(implicit ee: ExecutionEnv) = {

    stubFor(
      delete(
        urlEqualTo("/argonaut/tests"))
        .willReturn(
          aResponse()
            .withStatus(201)
            .withBody(abim.toJson.compactPrint)))

    Cirrus(ArgonautHTTP.DELETE[User]("http://localhost:8080/argonaut/tests")) must be_==(Some(abim)).await
  }


  def `An Play GET request should return a valid body`(implicit ee: ExecutionEnv) = {

    stubFor(
      get(
        urlEqualTo("/play/tests"))
        .willReturn(
          aResponse()
            .withStatus(201)
            .withBody(stringify(userPlayFormat.writes(abim)))))

    Cirrus(PlayHTTP.GET[User]("http://localhost:8080/play/tests")) must be_==(JsSuccess(abim)).await
  }


  def `An Play PUT request should return a valid body`(implicit ee: ExecutionEnv) = {

    stubFor(
      put(
        urlEqualTo("/play/tests"))
        .withRequestBody(WireMock.equalTo(stringify(userPlayFormat.writes(abim))))
        .willReturn(
          aResponse()
            .withStatus(201)
            .withBody(stringify(userPlayFormat.writes(abim)))))

    Cirrus(PlayHTTP.PUT[User]("http://localhost:8080/play/tests") ! abim) must be_==(JsSuccess(abim)).await
  }


  def `An Play POST request should return a valid body`(implicit ee: ExecutionEnv) = {

    stubFor(
      post(
        urlEqualTo("/play/tests"))
        .withRequestBody(WireMock.equalTo(stringify(userPlayFormat.writes(abim))))
        .willReturn(
          aResponse()
            .withStatus(201)
            .withBody(stringify(userPlayFormat.writes(abim)))))

    Cirrus(PlayHTTP.POST[User]("http://localhost:8080/play/tests") ! abim) must be_==(JsSuccess(abim)).await
  }


  def `An Play DELETE request should return a valid body`(implicit ee: ExecutionEnv) = {

    stubFor(
      delete(
        urlEqualTo("/play/tests"))
        .willReturn(
          aResponse()
            .withStatus(201)
            .withBody(stringify(userPlayFormat.writes(abim)))))

    Cirrus(PlayHTTP.DELETE[User]("http://localhost:8080/play/tests")) must be_==(JsSuccess(abim)).await
  }
}
