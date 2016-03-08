package com.godis.network.rebound

import com.goebl.david.Webb
import spray.json._

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future, Promise}
import scala.util.Success

object Rebound {

  def GET[T: JsonReader](url: String)(implicit ec: ExecutionContext = ExecutionContext.global) = {
    io(GETBuilder(url))
  }

//  def POST[T: JsonReader, F: JsonWriter](url: String, content: F)(implicit ec: ExecutionContext = ExecutionContext.global) = {
//    io(POSTBuilder(url, content))
//  }

  private def io[T: JsonReader](requestBuilder: RequestBuilder)(implicit ec: ExecutionContext) = {
    val promise = Promise[T]()

    Future {
      val request = requestBuilder(Webb.create())

      val response = request.asString()

      val body = response.getBody

      val content = body.parseJson.convertTo[T]

      promise.complete(Success(content))
    } recover {
      case ex => promise.failure(ex)
    }

    promise.future
  }
}

object Main extends App {

  import Protocol._
  import Rebound._

  val user: User = User("James", "F", "239423", "james@gmail.com")

  val get = Await result(GET[List[User]]("http://demo1123999.mockable.io/users"), 3 seconds)

//  val post = Await result(POST[List[User]]("http://demo1123999.mockable.io/users")(user), 3 seconds)
  //  val result = Await result(GET[List[User]]("http://192.168.0.40:9000/users"), 3 seconds)

  println(s"GET Result: $get")
//  println(s"POST Result: $post")
}