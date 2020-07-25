package com.jimo.scala.func

object FuncDemo04 {
  def main(args: Array[String]): Unit = {
    val list = List(1, 2, 3, 4)

    // 正常写法
    println(list.map((x: Int) => x + 1))
    // 类型推断省略
    println(list.map((x) => x + 1))
    // 一个参数可替换
    println(list.map(_ + 1))

    // 求和
    println(list.reduce(_ + _))
  }
}
