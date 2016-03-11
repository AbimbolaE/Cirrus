package com.godis.network.rebound

import java.net.{HttpURLConnection, URL}

import spray.json._

import scala.concurrent.{ExecutionContext, Future}
import scala.io.Source
import scala.util.Failure

object Rebound {

  case class GET[T: JsonReader](address: String)(implicit ec: ExecutionContext = ExecutionContext.global) {
    def send = {

      var connection: Option[HttpURLConnection] = None

      Future {

        connection = Some(new URL(address).openConnection().asInstanceOf[HttpURLConnection])
        connection
          .map(c => Source.fromInputStream(c.getInputStream).buffered.mkString)
          .map(s => implicitly[JsonReader[T]].read(s.parseJson))
          .get
      } andThen {
        case Failure(ex) => println(ex)
        case _ => connection.foreach(_.disconnect())
      }
    }

    def ! = send
  }

  case class POST[T : JsonReader](address: String)(implicit ec: ExecutionContext = ExecutionContext.global) {
    def send[F : JsonWriter](content: F) = {

      var connection: Option[HttpURLConnection] = None

      Future {

        connection.foreach(_.setRequestMethod("POST"))

        connection = Some(new URL(address).openConnection().asInstanceOf[HttpURLConnection])
        connection.foreach(_.setDoOutput(true))

        val payload = implicitly[JsonWriter[F]].write(content).compactPrint.getBytes

        connection.foreach(_.getOutputStream.write(payload))

        connection
          .map(c => Source.fromInputStream(c.getInputStream).buffered.mkString)
          .map(s => implicitly[JsonReader[T]].read(s.parseJson))
          .get
      } andThen {
        case Failure(ex) => println(ex)
        case _ => connection.foreach(_.disconnect())
      }
    }
    def ![F : JsonWriter](content: F) = send(content)
  }

//  case class PUT[T : JsonReader](url: String)(implicit ec: ExecutionContext = ExecutionContext.global) {
//    def send[F : JsonWriter](content: F) = {
//      performIO(PUTBuilder(url, content))
//    }
//    def ![F : JsonWriter](content: F) = send(content)
//  }
//
//  case class DELETE[T : JsonReader](url: String)(implicit ec: ExecutionContext = ExecutionContext.global) {
//    def send = {
//      performIO(DELETEBuilder(url))
//    }
//    def ! = send
//  }
}
