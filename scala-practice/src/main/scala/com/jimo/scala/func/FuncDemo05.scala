package com.jimo.scala.func

object FuncDemo05 {
  def main(args: Array[String]): Unit = {
    def makeSuffix(suffix: String) = {
      (name: String) => {
        if (name.endsWith(suffix)) {
          name
        } else {
          name + suffix
        }
      }
    }

    val jpg = makeSuffix(".jpg")
    println(jpg("a.jpg")) // a.jpg
    println(jpg("b")) // b.jpg

    // 普通
    def mul1(x: Int, y: Int) = x * y

    println(mul1(10, 9))

    // 闭包
    def mul2(x: Int) = (y: Int) => x * y

    println(mul2(10)(9))

    // 柯里化
    def mul3(x: Int)(y: Int) = x * y

    println(mul3(10)(9))

    def eq(s1: String)(s2: String): Boolean = {
      s1.toLowerCase == s2.toLowerCase
    }

    println(eq("hello")("HellO"))

    // 高级的方式
    implicit class AddEqToString(s: String) {
      def myEq(ss: String)(f: (String, String) => Boolean): Boolean = {
        f(s.toLowerCase, ss.toLowerCase)
      }
    }

    def equal(s1: String, s2: String): Boolean = {
      s1.equals(s2)
    }

    println("hello".myEq("HellO")(equal))
  }
}
