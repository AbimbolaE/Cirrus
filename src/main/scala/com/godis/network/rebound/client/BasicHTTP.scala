package com.godis.network.rebound.client

import com.godis.network.rebound.core.{BasicClient, BasicRequest, HTTPVerb}

object BasicHTTP {

  case class GET(address: String)(implicit val client: BasicClient) extends EmptyVerb

  case class POST(address: String)(implicit val client: BasicClient) extends LoadedVerb

  case class PUT(address: String)(implicit val client: BasicClient) extends LoadedVerb

  case class DELETE(address: String)(implicit val client: BasicClient) extends EmptyVerb


  trait EmptyVerb extends HTTPVerb {

    def send() = client connect BasicRequest(method = method, address = address,
        headers = headers, params = params)

    def ! = send()
  }


  trait LoadedVerb extends HTTPVerb {

    def send(payload: String) = client connect BasicRequest(method = method, address = address,
        headers = headers, params = params, body = Some(payload))

    def !(payload: String) = send(payload)
  }
}
