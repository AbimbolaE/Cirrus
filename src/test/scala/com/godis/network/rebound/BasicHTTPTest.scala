package com.godis.network.rebound


import com.godis.network.rebound.client.BasicHTTP.{GET, POST}
import com.godis.network.rebound.core.{FailedRequest, BasicResponse}

import scala.concurrent.Await

/**
  * Created by esurua01 on 11/03/2016.
  */
object BasicHTTPTest extends App {

  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.concurrent.duration._

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
