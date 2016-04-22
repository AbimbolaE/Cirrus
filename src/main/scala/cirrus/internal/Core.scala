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

  type ClientResponse <: Response

  implicit val ec: ExecutionContext

  def connect(request: Request): Future[ClientResponse]
}


trait HTTPVerb extends HeaderManipulation with ParamManipulation {

  type VerbClient <: Client

  implicit val client: VerbClient

  val address: String
  val method = getClass.getSimpleName
}


trait HeaderManipulation {

  protected val requestHeaders = ListBuffer.empty[(String, String)]

  def headers = requestHeaders.toList

  def ~(entry: (String, String)): this.type = withHeader(entry)
  def ~~(entries: TraversableOnce[(String, String)]): this.type = withHeaders(entries)

  def withHeader(entry: (String, String)): this.type = { requestHeaders += entry; this }
  def withHeaders(entries: TraversableOnce[(String, String)]): this.type = { requestHeaders ++= entries; this }

  def dropHeaders = requestHeaders.clear()
}

trait ParamManipulation {

  protected val requestParams = ListBuffer.empty[(String, String)]

  def params = requestParams.toList

  def ?(entry: (String, String)): this.type = withParam(entry)
  def ??(entries: TraversableOnce[(String, String)]): this.type = withParams(entries)

  def withParam(entry: (String, String)): this.type = { requestParams += entry; this }
  def withParams(entries: TraversableOnce[(String, String)]): this.type = { requestParams ++= entries; this }

  def dropParams = requestParams.clear()
}