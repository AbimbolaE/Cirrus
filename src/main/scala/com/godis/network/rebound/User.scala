package com.godis.network.rebound

import com.google.gson.Gson

/**
 * Created by Abim on 07/03/2016.
 */
case class User(username: String, gender: String, mobile: String, email: String)

object User {
  implicit val reader: Parser[User] = new Parser[User] {
    override def parse(json: String): User = {
      new Gson().fromJson(json, classOf[User])
    }
  }
}