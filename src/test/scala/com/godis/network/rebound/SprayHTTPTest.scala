package com.godis.network.rebound

import com.godis.network.rebound.core.{FailedRequest, BasicClient}

//import com.godis.network.rebound.Defaults._
import com.godis.network.rebound.client.SprayHTTP.GET

import Protocol._
import scala.concurrent.Await
import scala.concurrent.duration._

object SprayHTTPTest extends App {

  implicit val client = BasicClient()

  val user = User("James", "F", "+234808888330", "james@gmail.com")

  val post = GET[List[User]]("http://demo7281011.mockable.io/user")
  post header ("Content-Type" -> "application/json")

  try {

    val response = Await.result(post !, 3 seconds)

    println(s"Content: ${response.body}")

  } catch {
    case e: FailedRequest => println(e)
  }
}
