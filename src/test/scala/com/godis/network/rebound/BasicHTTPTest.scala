package com.godis.network.rebound

import com.godis.network.rebound.client.BasicHTTP.{POST, GET}
import com.godis.network.rebound.Defaults.defaultClient
import com.godis.network.rebound.core.FailedRequest

import scala.concurrent.Await
import scala.concurrent.duration._

object BasicHTTPTest extends App {

  val get = GET("https://demo6556920.mockable.io/user")

  try {
    val getResponse = Await.result(get !, 10 seconds)

    println(s"Content: ${getResponse.body}")
  } catch {
    case ex @ (_:FailedRequest) => println(ex.cause)
  }

  val headers = Map("Content-Type" -> "application/text", "Accept" -> "application/json")

//  val post = POST("http://192.168.0.8:9000/user")
  val post = POST("https://demo6556920.mockable.io/users")

  post withHeader "Content-Type" -> "application/json"
  post withHeaders headers

  try {

    val postResponse = Await.result(post ! "{ \"user\": true }", 10 seconds)

    println(s"Content: ${postResponse.body}")
  } catch {
    case ex @ (_:FailedRequest) => println(ex.cause)
  }
}
