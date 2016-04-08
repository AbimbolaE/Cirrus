package cirrus.internal

import scala.collection.mutable.ListBuffer
import scala.concurrent.{ExecutionContext, Future}

trait Request {

  def method: String
  def address: String
  def headers: List[(String, String)]
  def params: List[(String, String)]
  def body: Option[String]
}


trait Response {

  type Content
  def statusCode: Int
  def headers: Map[String, String]
  def body: Content
}


trait Client {

  implicit val ec: ExecutionContext

  def connect(request: Request): Future[Response]
}


trait HTTPVerb {

  implicit val client: Client

  val address: String
  val method = getClass.getSimpleName

  protected val requestHeaders = ListBuffer.empty[(String, String)]
  protected val requestParams = ListBuffer.empty[(String, String)]

  protected def headers = requestHeaders.toList
  protected def params = requestParams.toList

  def withHeader(entry: (String, String)): this.type = { requestHeaders += entry; this }
  def withHeaders(entries: TraversableOnce[(String, String)]): this.type = { requestHeaders ++= entries; this }

  def withParam(entry: (String, String)): this.type = { requestParams += entry; this }
  def withParams(entries: TraversableOnce[(String, String)]): this.type = { requestParams ++= entries; this }
}
