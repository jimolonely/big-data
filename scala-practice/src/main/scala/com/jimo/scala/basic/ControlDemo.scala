package com.jimo.scala.basic

import scala.util.control.Breaks

/**
 * 控制流
 */
object ControlDemo {

  def testWhile(): Unit = {
    var n = 0
    Breaks.breakable {
      while (n < 20) {
        println(n)
        if (n > 10) {
          Breaks.break()
        }
        n += 1
      }
    }
  }

  def main(args: Array[String]): Unit = {
    //    testFor

    testWhile()
  }

  private def testFor = {
    var v = {}
    println(v)
    println(v.isInstanceOf[Unit])

    for (i <- 0 to 10) {
      print(i + ",")
    }
    println()
    for (i <- 0 to 10 reverse) {
      print(i + ",")
    }

    val s = "Hello"
    for (c <- s) {
      print(c + ",")
    }

    s.foreach(c => print(c + ","))
    var sum = 1L
    s.foreach(sum += _.toLong)
    println(sum)

    println("s.take(1)", s.take(1))
    println("s.take(2)", s.take(2))
    println("s.drop(1)", s.drop(1))
  }
}
