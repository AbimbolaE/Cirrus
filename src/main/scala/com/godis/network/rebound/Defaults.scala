package com.godis.network.rebound

import java.net.HttpURLConnection
import java.nio.charset.CodingErrorAction

import com.godis.network.rebound.core.BasicClient

import scala.io.Codec

object Defaults {

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

  implicit val defaultClient = BasicClient()
}
