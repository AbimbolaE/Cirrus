package com.godis.cirrus

import java.util.concurrent.Executors

import com.godis.cirrus.client.BasicHTTP.{GET, POST}
import com.godis.cirrus.core.{BasicClient, FailedRequest}
//import com.godis.cirrus.Defaults._

import scala.concurrent.{ExecutionContext, Await}
import scala.concurrent.duration._

object BasicHTTPTest extends App {

  implicit val client = BasicClient
      .Builder
      .withTweak(_.setInstanceFollowRedirects(false))
      .build()

  val get = GET("https://demo6556920.mockable.io/user")

  try {
    val getResponse = Await.result(get !, 10 seconds)

    println(s"Content: ${getResponse.body}")
  } catch {
    case ex @ (_:FailedRequest) => println(ex.cause)
  }

  val headers = Map("Content-Type" -> "application/json", "Accept" -> "application/json")

//  val post = POST("http://192.168.0.8:9000/user")

  val post = POST("https://www.xms-portal.com/xms/j_spring_security_check")
//  val post = POST("https://demo6556920.mockable.io/users")

  val creds = Map(
    "j_username" -> "abimbolaesuruoso@gmail.com",
    "j_password" -> "Bola.104",
    "__spring_security_remember_me" -> ""
  )

  implicit val ec = ExecutionContext.global

  Console println Await.result(post send creds map { _.headers }, 10 seconds)

}
