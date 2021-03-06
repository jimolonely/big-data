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

    for (pair <- Array((0, 1), (1, 1), (1, 0, 2))) {
      val res = pair match {
        case (0, _) => "0..."
        case (y, 0) => y
        case _ => "其他"
      }
      println(res)
    }

    object Square {
      def unapply(z: Double): Option[Double] = Some(math.sqrt(z))

      def apply(z: Double): Double = z * z
    }

    val number: Double = 49.0
    number match {
      case Square(n) => println(n) // 7.0
      case _ => println("不匹配")
    }

    val n = Square(10)
    n match {
      case Square(n) => println(n) // 10.0
      case _ => println("不匹配")
    }

    object Square2 {
      def unapply(z: Double): Option[Double] = Some(z + 1)

      def apply(z: Double): Double = z * z
    }

    val n1 = Square2(10)
    n1 match {
      case Square2(n) => println(n) // 101.0 = 10*10+1
      case _ => println("不匹配")
    }

    object Name {
      def unapplySeq(s: String): Option[Seq[String]] = {
        if (s.contains(",")) Some(s.split(","))
        else None
      }
    }

    val names = "jimo,hehe,lily"

    names match {
      case Name(a, b, c) => println(s"${a}--${b}--${c}")
      case Name(a, b) => println(s"${a}==${b}")
      case _ => println("不匹配")
    } // jimo--hehe--lily
  }
}
