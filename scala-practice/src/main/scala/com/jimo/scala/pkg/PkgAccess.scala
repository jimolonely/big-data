package com.jimo.scala.pkg

//import scala.collection.mutable.{HashMap, HashSet}
//import java.util.{HashMap => JavaHashMap}

import java.util.{HashSet => _}

class A {
  private var a: Int = 18

  def say(): Unit = {
    println("hello")
//    new util.HashSet[String]()
  }

  private[jimo] var b = "bbb"
}

object A {
  def main(args: Array[String]): Unit = {
    val a = new A
    println(a.a)
    a.say()
  }
}

object B {
  def main(args: Array[String]): Unit = {
    val a = new A
    // 不能访问
    // println(a.a)
    a.say()
    println(a.b)
  }
}