package com.jimo.scala.func

object FuncDemo03 {
  def main(args: Array[String]): Unit = {
    // 以函数作为参数
    def test(f: Double => Double, n: Double) = {
      f(n)
    }

    def sum(n: Double) = {
      n + n
    }

    println(test(sum, 10.6)) // 21.2

    // 返回函数类型
    val minusXy = (x: Int) => (y: Int) => x - y
    println(minusXy(100)) // <function1>
    println(minusXy(100)(30)) // 70
  }
}
