package com.godis.network.rebound


import com.godis.network.rebound.client.BasicHTTP.GET
import com.godis.network.rebound.client.Defaults
import Defaults._
import com.godis.network.rebound.core.FailedRequest

import scala.concurrent.Await
import scala.concurrent.duration._

object BasicHTTPTest extends App {


  val get = GET("https://demo6556920.mockable.io/user")
//  get param("foo" -> "bar")
  get header("foo" -> "bar")

  try {
    val getResponse = Await.result(get !, 10 seconds)

    println(s"Content: ${getResponse.body}")
  } catch {
    case ex @ (_:FailedRequest) => println(ex.cause)
  }

  val headers = Map("Content-Type" -> "application/json", "Accept" -> "application/json")

//  val post = POST("http://192.168.0.8:9000/user")
//  post header("Content-Type" -> "application/json")
//  post headers headers
//
//  try {
//
//    val postResponse = Await.result(post ! "{ \"user\": true }", 10 seconds)
//
//    println(s"Content: ${postResponse.body}")
//  } catch {
//    case ex @ (_:FailedRequest) => println(ex.cause)
//  }
}
