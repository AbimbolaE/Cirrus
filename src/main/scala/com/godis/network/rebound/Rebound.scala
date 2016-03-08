package com.godis.network.rebound

import java.util.concurrent.Executors

import com.godis.network.rebound.RequestMaker._
import spray.json._

import scala.concurrent.ExecutionContext

object Rebound {

  case class GET[T: JsonReader](url: String)(implicit ec: ExecutionContext = ExecutionContext.global) {
    def send = {
      performIO(GETBuilder(url))
    }
    def ! = send
  }

  case class POST[T : JsonReader](url: String)(implicit ec: ExecutionContext = ExecutionContext.global) {
    def send[F : JsonWriter](content: F) = {
      performIO(POSTBuilder(url, content))
    }
    def ![F : JsonWriter](content: F) = send(content)
  }

  case class PUT[T : JsonReader](url: String)(implicit ec: ExecutionContext = ExecutionContext.global) {
    def send[F : JsonWriter](content: F) = {
      performIO(PUTBuilder(url, content))
    }
    def ![F : JsonWriter](content: F) = send(content)
  }

  case class DELETE[T : JsonReader](url: String)(implicit ec: ExecutionContext = ExecutionContext.global) {
    def send = {
      performIO(DELETEBuilder(url))
    }
    def ! = send
  }
}

object Main extends App {

  import Protocol._
  import Rebound._

  val user = User("James", "F", "239423", "james@gmail.com")


  implicit val ec = ExecutionContext.fromExecutor(Executors.newCachedThreadPool())

  for {
    users <- GET[List[User]]("http://demo1123999.mockable.io/users").send
    user = users.head
  } yield println(s"Hurrah: $user")
}