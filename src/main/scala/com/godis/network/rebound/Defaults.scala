package com.godis.network.rebound

import java.net.HttpURLConnection
import java.nio.charset.CodingErrorAction

import com.godis.network.rebound.core.BasicClient

import scala.io.Codec

object Defaults {

  implicit val defaultClient = BasicClient()

  object Headers {
    val `Accept` = "Accept"
    val `Content-Type` = "Content-Type"
    val `application/json` = "application/json"
  }

  object ClientConfig {

    val charset = "UTF-8"

    val codec = {
      val c = Codec("UTF-8")
      c.onMalformedInput(CodingErrorAction.IGNORE)
      c
    }

    val headers = List("Content-Type" -> "text/plain")

    val tweaks = List(
      (h: HttpURLConnection) => h.setReadTimeout(10000),
      (h: HttpURLConnection) => h.setConnectTimeout(5000)
    )
  }
}
