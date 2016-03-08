package com.godis.network.rebound

import com.goebl.david.Webb
import spray.json.JsonReader

import scala.concurrent.{Future, Promise, ExecutionContext}
import scala.util.Success
import spray.json._

/**
  * Created by esurua01 on 08/03/2016.
  */
object RequestMaker {

  def performIO[T: JsonReader](requestBuilder: RequestBuilder)(implicit ec: ExecutionContext) = {
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
