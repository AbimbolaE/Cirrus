package com.godis.network.rebound

import com.godis.network.rebound.client.BasicClient

/**
 * Created by Abim on 12/03/2016.
 */
package object core {

  lazy val basicClient = BasicClient()

  object RequestMethods {

    val GET = "GET"
    val POST = "POST"
    val PUT = "PUT"
    val DELETE = "DELETE"
  }
}
