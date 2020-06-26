package com.jimo.scala.oop2

object CreateObj {
  def main(args: Array[String]): Unit = {
    val a = AAA.apply()
    println(a)
  }
}

class AAA {}

object AAA {
  def apply(): AAA = new AAA()
}
