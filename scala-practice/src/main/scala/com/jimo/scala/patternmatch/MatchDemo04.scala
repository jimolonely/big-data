package com.jimo.scala.patternmatch

object MatchDemo04 {
  def main(args: Array[String]): Unit = {
    List(1, 3, 4, 6) match {
      case first :: second :: rest => println(first + " " + second + " " + rest.length)
      case _ => println("匹配不到")
    } // 1 3 2

    List(1) match {
      case first :: second :: rest => println(first + " " + second + " " + rest.length)
      case _ => println("匹配不到")
    } // 匹配不到
  }
}
