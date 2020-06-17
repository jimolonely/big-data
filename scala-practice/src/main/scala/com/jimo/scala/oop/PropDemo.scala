package com.jimo.scala.oop

import scala.beans.BeanProperty

object PropDemo {
  def main(args: Array[String]): Unit = {
    println("ok")

    val p = new P4()
    p.setAge(19)
    println(p.getAge)

    new P5("jimo")
  }
}

class P1(name: String) {
  println(name)
}

class P2(val name: String) {
  println(name)
}

class P3(var name: String) {
  println(name)
}

class P4 {
  var name: String = _
  @BeanProperty
  var age: Int = 0
}

class P5 {
  var name: String = "hehe"

  def this(name: String) {
    this()
    println(this.name)
    this.name = name
    println(this.name)
  }
}
