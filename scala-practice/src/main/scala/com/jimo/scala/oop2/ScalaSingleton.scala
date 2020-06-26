package com.jimo.scala.oop2

object ScalaSingleton {
  def main(args: Array[String]): Unit = {
    println(SSingleton.name)
    println(SSingleton.hello())
  }
}

object SSingleton {
  var name: String = "hello"

  def hello(): Unit = {}
}
