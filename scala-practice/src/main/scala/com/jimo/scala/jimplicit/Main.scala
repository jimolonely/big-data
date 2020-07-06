package com.jimo.scala.jimplicit

object Main {
  def main(args: Array[String]): Unit = {
    //    val num1: Int = 3.5 // 转换出错
    implicit def doubleToInt(d: Double): Int = {
      d.toInt
    }

    val num: Int = 3.5 // 转换ok

    def f(): Unit = {
      def f(): Unit = {
        println("f")
      }
    }

    def f1(): Unit = {
      def f2(): Unit = {
        println("f2")
      }
    }
  }

}
