package cirrus

import cirrus.clients.ArgonautHTTP
import cirrus.internal.Response
import cirrus.utils.User
import cirrus.utils.User._

import scala.concurrent.Await
import scala.concurrent.duration._

object CirrusExample extends App {

  implicit val ec = cirrus.client.ec

  val res = Await.result(ArgonautHTTP.POST("http://localhost:9000/user").send(User("", "", "", ""))
    .map { case r: Response => r.body }, 10 seconds)

  println(res)



}
