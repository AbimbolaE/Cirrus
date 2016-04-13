package utils

import com.github.tomakehurst.wiremock.WireMockServer

import scala.util.Try

trait WireMockContext {

  val server = new WireMockServer()

  Try { server.start() }

  sys addShutdownHook {
    server.shutdown()
  }
}
