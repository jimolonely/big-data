package com.jimo.scala.patternmatch

object MatchDemo01 {
  def main(args: Array[String]): Unit = {
    val op = "*"
    val n1 = 10
    val n2 = 20

    val res = op match {
      case "+" => n1 + n2
      case "-" => n1 - n2
      case "*" => n1 * n2
      case "/" => n1 / n2
      case _ => 0
    }
    println(res) // 200

    for (i <- (0 to 10)) {
      i match {
        case 0 => println("start")
        case _ if i % 2 == 0 => println("偶数")
        case _ => println("其他数")
      }
    }
  }
}
