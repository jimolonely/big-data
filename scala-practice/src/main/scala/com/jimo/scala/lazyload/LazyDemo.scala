package com.jimo.scala.lazyload

object LazyDemo {

  def longJob(): Int = {
    println("这是个很耗资源的任务")
    1
  }

  def main(args: Array[String]): Unit = {
    lazy val res = longJob()
    println("============================")
    println(res)
  }
}
