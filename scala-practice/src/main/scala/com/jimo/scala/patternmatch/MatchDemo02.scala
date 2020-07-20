package com.jimo.scala.patternmatch

object MatchDemo02 {
  def main(args: Array[String]): Unit = {
    val c = 'H'
    c match {
      case '+' => println("ok")
      case myChar => println("Char:" + myChar)
      case _ => println("default")
    }
  }
}
