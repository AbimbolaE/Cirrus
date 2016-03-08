package com.godis.network.rebound

import com.goebl.david.{Request, Webb}
import spray.json.JsonWriter

/**
  * Created by esurua01 on 08/03/2016.
  */
sealed trait RequestBuilder {
  def apply(webb: Webb): Request
}

case class GETBuilder(url: String) extends RequestBuilder {
  def apply(webb: Webb) =
    webb
      .get(url)
      .ensureSuccess()
}

case class POSTBuilder[F: JsonWriter](url: String, content: F) extends RequestBuilder {
  def apply(webb: Webb) =
    webb
      .post(url)
      .body(implicitly[JsonWriter[F]].write(content).compactPrint)
      .ensureSuccess()
}
