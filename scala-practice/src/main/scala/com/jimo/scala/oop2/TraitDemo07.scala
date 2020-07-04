package com.jimo.scala.oop2

object TraitDemo07 {
  def main(args: Array[String]): Unit = {
    val log = new LogExp {}
    log.log()
  }
}

trait LogExp extends Exception {
  def log(): Unit = {
    println(getMessage)
  }
}
