package com.jimo.scala.func

object FuncDemo02 {
  def main(args: Array[String]): Unit = {
    def plus(x: Int): Int = x + 1

    println(Array(1, 2, 3, 4).map(plus).mkString(",")) // 2,3,4,5

    // 匿名函数
    val triple = (x: Double) => 3 * x
    println(triple(3.5)) // 10.5
    println(triple) // <function1>

    val mySum = (x: Int, y: Int) => x + y
    println(mySum(1, 2))
  }
}
