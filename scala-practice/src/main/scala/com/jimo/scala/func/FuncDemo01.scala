package com.jimo.scala.func

object FuncDemo01 {
  def main(args: Array[String]): Unit = {
    val list = List(1, 2, 3, "abc")

    // 模式匹配方式
    val list1 = list.map {
      case x: Int => x + 1
      case _ =>
    }
    println(list1) // List(2, 3, 4, ())

    // 偏函数
    val addOne = new PartialFunction[Any, Int] {
      def isDefinedAt(any: Any): Boolean = if (any.isInstanceOf[Int]) true else false

      def apply(any: Any): Int = any.asInstanceOf[Int] + 1
    }
    // map不能调偏函数
    val list2 = list.collect(addOne)
    println(list2) // List(2, 3, 4)

    // 简化
    def addOne2: PartialFunction[Any, Int] = {
      case i: Int => i + 1
    }

    println(list.collect(addOne2)) // List(2, 3, 4)

    // 更简化
    println(list.collect { case i: Int => i + 1 })
  }
}
