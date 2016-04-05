package com.godis.cirrus

import com.godis.cirrus.core.{BasicClient, FailedRequest}

import scala.language.postfixOps

//import com.godis.cirrus.Defaults._
import com.godis.cirrus.Protocol._
import com.godis.cirrus.client.SprayHTTP.POST

import scala.concurrent.Await
import scala.concurrent.duration._

object SprayHTTPTest extends App {

  implicit val client = BasicClient
    .Builder()
    .withDefaultHeaders(List(
      "Content-Type" -> "application/json",
      "Accept" -> "application/json"
    ))
    .withRequestBodyCharset("UTF-8")
    .build()

  val user = User("James", "F", "+234808888330", "james@gmail.com")

  val post = POST[List[User]]("http://demo6556920.mockable.io/users") withParam("foo" -> "bar")

  try {

    val start = System.currentTimeMillis()
    val response = Await.result(post ! user, 3 seconds)
    println(s"Request took ${System.currentTimeMillis - start}ms")

    println(s"Content: ${response.body}")

  } catch {
    case e: FailedRequest => e.printStackTrace()
  }
}