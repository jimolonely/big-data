package com.jimo.scala.jimplicit

object ImplicitDemo03 {
  def main(args: Array[String]): Unit = {
    implicit val str: String = "jimo"

    def hello(implicit name: String): Unit = {
      println(s"hello,$name")
    }

    hello("hehe")
    hello

    //    implicit val ss: String = "mime"

    def hi(implicit name: String = "lily"): Unit = {
      println(s"hi, $name")
    }

    hi
  }
}
