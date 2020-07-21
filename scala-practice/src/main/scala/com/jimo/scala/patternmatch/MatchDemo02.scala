package com.jimo.scala.patternmatch

object MatchDemo02 {
  def main(args: Array[String]): Unit = {
    val c = 'H'
    c match {
      case '+' => println("ok")
      case myChar => println("Char:" + myChar)
      case _ => println("default")
    }

    def typeMatch(res: Any): Unit = {
      res match {
        case a: Int => println("整数")
        case _: Map[String, Int] => println("map[String,Int]")
        case _: Array[String] => println("Array[String]")
        case _: Array[Int] => println("Array[Int]")
        case _: BigInt => println("BigInt")
        case _ => println("啥也不是")
      }
    }

    typeMatch(3)
    typeMatch("hehe")
    typeMatch(Map(("name", 18)))
    typeMatch(Array(1, 2, 3))
  }
}
