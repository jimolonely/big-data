package com.jimo.scala.col

object ColDemo12 {
  def main(args: Array[String]): Unit = {
    println(test(twoFold, 2.0))

    val l1 = List(1, 2, 3)
    println(l1.map(_ * 2)) // List(2, 4, 6)
  }

  /**
   * 以函数作为参数的函数
   */
  def test(f: Double => Double, d: Double): Double = {
    f(d)
  }

  def twoFold(d: Double): Double = {
    2 * d
  }
}
