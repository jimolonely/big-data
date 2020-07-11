package com.jimo.scala.col

object ColDemo04 {
  def main(args: Array[String]): Unit = {
    val arr = Array.ofDim[Int](3, 4)
    arr(1)(1) = 3
    for (a1 <- arr) {
      for (elem <- a1) {
        print(elem + ",")
      }
      println()
    }
  }
}
