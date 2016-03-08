package com.godis.network.rebound

import spray.json.DefaultJsonProtocol

/**
 * Created by Abim on 07/03/2016.
 */
object Protocol extends DefaultJsonProtocol {
  implicit val userFormat = jsonFormat4(User)
}

case class User(username: String, gender: String, mobile: String, email: String)
