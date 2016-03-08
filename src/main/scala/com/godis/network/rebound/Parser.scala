package com.godis.network.rebound

/**
 * Created by Abim on 07/03/2016.
 */
trait Parser[T] {

  def parse(json: String): T
}