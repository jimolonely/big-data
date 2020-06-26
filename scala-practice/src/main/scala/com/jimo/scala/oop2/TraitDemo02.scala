package com.jimo.scala.oop2

object TraitDemo02 {
  def main(args: Array[String]): Unit = {
    val tai = new TAI
    tai.hello()
  }
}

trait TA {
  def hello(): Unit = {
    println("TA hello()")
  }

  def go(): Unit
}

class TAI extends TA {
  override def go(): Unit = {
    println("TAI go()")
  }
}
