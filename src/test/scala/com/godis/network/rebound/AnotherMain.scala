package com.godis.network.rebound

import com.godis.network.rebound.json.SprayClient.GET

import scala.concurrent.Await

/**
  * Created by esurua01 on 11/03/2016.
  */
object AnotherMain extends App {

  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.concurrent.duration._
  import Protocol._

  val user = User("James", "F", "239423", "james@gmail.com")

  val response = Await.result(GET[List[User]]("http://demo6556920.mockable.io/user") !, 3 seconds)

  println(s"Content: $response")
}
