package com.jimo.scala.col

object ColDemo06 {
  def main(args: Array[String]): Unit = {
    val t4 = (1, 2, 3, "hehe")
    println(t4, t4._1, t4._4)

    println(t4.productElement(3))

    for (elem <- t4.productIterator) {
      println(elem)
    }
  }
}
