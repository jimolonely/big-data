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

    // 数组匹配
    for (arr <- Array(Array(0), Array(1, 0), Array(0, 1, 0),
      Array(1, 1, 0), Array(1, 1, 0, 1), Array("hh", 123))) {
      val res = arr match {
        // 精确匹配Array(0)
        case Array(0) => "0"
        // 匹配2个元素的数组
        case Array(x, y) => s"(${x},${y})"
        case Array(0, _*) => "以0开头的数组"
        case _ => "默认"
      }
      println(res)
    }

    for (list <- Array(List(0), List(1, 0), List(0, 0, 0), List(1, 0, 0))) {
      val res = list match {
        case 0 :: Nil => "0"
        case x :: y :: Nil => s"[${x},${y}]" // 2个元素
        case 0 :: tail => "0..."
        case _ => "其他"
      }
      println(res)
    }
  }
}
