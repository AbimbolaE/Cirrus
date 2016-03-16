package com.godis.cirrus

import spray.json.DefaultJsonProtocol

/**
 * Created by Abim on 07/03/2016.
 */
object Protocol extends DefaultJsonProtocol {
  implicit val userFormat = jsonFormat4(User)
  implicit val messageFormat = jsonFormat1(Message)
}

case class User(username: String, gender: String, mobile: String, email: String)
case class Message(message: String)
