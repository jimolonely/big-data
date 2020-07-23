package com.jimo.scala.patternmatch

object MatchDemo03 {
  def main(args: Array[String]): Unit = {
    for (amt <- Array(Dollar(100.0), Currency(1000, "RMB"), NoAmount)) {
      val res = amt match {
        case Dollar(v) => "$" + v
        case Currency(v, u) => v + u
        case NoAmount => "没钱"
      }
      println(amt + ":" + res)
    }

    val c1 = Currency(19.9, "RMB")
    val c2 = c1.copy()
    val c3 = c1.copy(value = 29.9)
    val c4 = c1.copy(unit = "英镑")
    println(c2)
    println(c3)
    println(c4)
  }
}

abstract class Amount

case class Dollar(value: Double) extends Amount

case class Currency(value: Double, unit: String) extends Amount

case object NoAmount extends Amount
