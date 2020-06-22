package com.jimo.scala.oop

object ScalaOverrideDemo {
  def main(args: Array[String]): Unit = {
    val a: AA = new BB
    val b: BB = new BB
    println("a.age=" + a.age)
    println("b.age=" + b.age)
    println("a.name=" + b.name)
    println("b.name=" + b.name)

    val c: CC = new DD
    val d: DD = new DD
    println("c.height=" + c.height)
    println("d.height=" + d.height)
  }
}

class AA {
  val age: Int = 10
  //  var name: String = "AA"

  def name(): String = {
    "AA"
  }
}

class BB extends AA {
  override val age: Int = 20

  //  override var name: String = "BB"

  override val name: String = "BB"
}

abstract class CC {
  var height: Int
}

class DD extends CC {
  override var height: Int = 20
}