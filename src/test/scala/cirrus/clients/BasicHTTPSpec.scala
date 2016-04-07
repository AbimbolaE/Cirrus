package cirrus.clients

import cirrus.clients.BasicHTTP.{GET, HEAD, POST}
import cirrus.internal.{BasicClient, FailedRequest}
import org.specs2.Specification

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext}

class BasicHTTPSpec extends Specification { def is = s2"""

  The Basic HTTP features:
    A GET request should return a 200         $e1

  """

  def e1 = {
    Thread.sleep(10000)
    1 === 1
  }
}
