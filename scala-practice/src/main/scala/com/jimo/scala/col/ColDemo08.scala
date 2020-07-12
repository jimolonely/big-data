package com.jimo.scala.col

import scala.util.Random

object ColDemo08 {
  def main(args: Array[String]): Unit = {
    val ints = mkArr(11)
    ints.foreach(i => print(i + " "))
    println()

    revert(ints)
    ints.foreach(i => print(i + " "))
  }

  def mkArr(n: Int): Array[Int] = {
    val a = new Array[Int](n)
    val random = new Random()
    for (i <- a) yield random.nextInt(n)
  }

  def revert(arr: Array[Int]): Unit = {
    for (i <- 0 until(arr.length - 1, 2)) {
      val t = arr(i)
      arr(i) = arr(i + 1)
      arr(i + 1) = t
    }
  }
}

