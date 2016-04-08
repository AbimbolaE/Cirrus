package cirrus.utils

import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat

/**
 * Created by Abim on 07/03/2016.
 */
case class User(username: String, gender: String, mobile: String, email: String)

object User {
  implicit val userFormat: RootJsonFormat[User] = jsonFormat4(User.apply)
}

