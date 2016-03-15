package com.godis.network.rebound

import com.godis.network.rebound.client.Defaults
import com.godis.network.rebound.client.SprayHTTP.{GET, POST, PUT}

import scala.concurrent.Await

object SprayHTTPTest extends App {

  import Protocol._

  import Defaults._
//  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.concurrent.duration._

  val user = User("James", "F", "+234808888330", "james@gmail.com")


//  val response = Await.result(GET[List[User]]("http://192.168.0.8:9000/users") !, 10 seconds)

//  val post = POST[String]("http://192.168.0.8:9000/user")
  val post = GET[List[User]]("http://demo7281011.mockable.io/user")
  post header("Content-Type" -> "application/json")
  val response = Await.result(post ! , 3 seconds)

  println(s"Content: ${response.body}")
}
