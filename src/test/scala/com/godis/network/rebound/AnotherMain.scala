package com.godis.network.rebound

import com.godis.network.rebound.client.SprayHTTP.{GET, POST}

import scala.concurrent.Await

/**
  * Created by esurua01 on 11/03/2016.
  */
object AnotherMain extends App {

  import Protocol._

  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.concurrent.duration._

  val user = User("James", "F", "+234808888330", "james@gmail.com")


//  val response = Await.result(GET[List[User]]("http://192.168.0.8:9000/users") !, 10 seconds)

//  val post = POST[String]("http://192.168.0.8:9000/user")
  val post = POST[List[User]]("http://demo7281011.mockable.io/user")
  post header("Content-Type" -> "application/json")
  val response = Await.result(post ! user, 3 seconds)

  println(s"Content: ${response.body}")
}
