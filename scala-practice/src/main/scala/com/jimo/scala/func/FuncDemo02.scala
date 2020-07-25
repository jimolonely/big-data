package com.jimo.scala.func

object FuncDemo02 {
  def main(args: Array[String]): Unit = {
    def plus(x: Int): Int = x + 1

    println(Array(1, 2, 3, 4).map(plus).mkString(",")) // 2,3,4,5
  }
}
