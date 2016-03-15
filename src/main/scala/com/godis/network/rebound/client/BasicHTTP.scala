package com.godis.network.rebound.client

import com.godis.network.rebound.core._

import scala.concurrent.ExecutionContext

/**
 * Created by Abim on 12/03/2016.
 */
object BasicHTTP {

  case class GET(address: String)(implicit val ec: ExecutionContext) extends EmptyVerb

  case class POST(address: String)(implicit val ec: ExecutionContext) extends LoadedVerb

  case class PUT(address: String)(implicit val ec: ExecutionContext) extends LoadedVerb

  case class DELETE(address: String)(implicit val ec: ExecutionContext) extends EmptyVerb


  trait EmptyVerb extends HTTPVerb {

    implicit val ec: ExecutionContext

    def send() = basicClient connect BasicRequest(method = method, address = address, headers = headers, params = params)

    def ! = send()
  }


  trait LoadedVerb extends HTTPVerb {

    implicit val ec: ExecutionContext

    def send(payload: String) =
      basicClient connect BasicRequest(method = method, address = address, headers = headers, params = params, body = Some(payload))

    def !(payload: String) = send(payload)
  }
}
