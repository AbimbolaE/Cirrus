package cirrus.internal

import java.net.HttpURLConnection
import java.nio.charset.CodingErrorAction

import scala.io.Codec

object Implicits {

  implicit val client = BasicClient()
}

object Headers {

  val `Accept` = "Accept"
  val `Accept-Charset` = "Accept-Charset"
  val `Accept-Encoding` = "Accept-Encoding"
  val `Authorization` = "Authorization"
  val `Cache-Control` = "Cache-Control"
  val `Content-Type` = "Content-Type"

  val `application/json` = "application/json"
  val `application/x-www-form-urlencoded` = "application/x-www-form-urlencoded"
  val `application/xml` = "application/xml"
  val `text/plain` = "text/plain"
}

object ClientConfig {

  val charset = "UTF-8"

  val codec = {
    val c = Codec("UTF-8")
    c.onMalformedInput(CodingErrorAction.IGNORE)
    c
  }

  val headers = List.empty[(String, String)]

  val tweaks = List(
    (h: HttpURLConnection) => h.setReadTimeout(10000),
    (h: HttpURLConnection) => h.setConnectTimeout(5000)
  )
}
