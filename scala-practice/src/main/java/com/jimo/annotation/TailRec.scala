package com.jimo.annotation

import scala.annotation.tailrec

object TailRec {

  //  @tailrec
  //  def factorial(x: Int): Int = {
  //    if (x == 1) 1 else x * factorail(x - 1)
  //  }

  @tailrec
  def factorial(x: Int, total: Int): Int = {
    if (x == 1) total else factorial(x - 1, total * x)
  }

  def main(args: Array[String]): Unit = {
    println(factorial(5, 1))
  }
}
