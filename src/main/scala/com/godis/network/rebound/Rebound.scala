package com.godis.network.rebound

import com.goebl.david.Webb
import com.google.gson.Gson
import org.json.JSONArray

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future, Promise}
import scala.reflect.ClassTag
import scala.util.Success

object Rebound {

  object GET {
    def apply[T : Parser](url: String)(implicit ec: ExecutionContext) = {
      val promise = Promise[T]()

      Future {
        val request = Webb.create()
          .get(url)
          .ensureSuccess()

        val response = request.asString()

        val body = response.getBody
        val content = new Gson().fromJson(body, classOf[T])
        promise.complete(Success(content))
      } recover {
        case ex => promise.failure(ex)
      }

      promise.future
    }
  }
}

object Main extends App {

  import Rebound._

  import scala.concurrent.ExecutionContext.Implicits.global

  val result = Await result(GET[User]("http://192.168.0.40:9000/users"), 3 seconds)

  println(s"Result: $result")
}