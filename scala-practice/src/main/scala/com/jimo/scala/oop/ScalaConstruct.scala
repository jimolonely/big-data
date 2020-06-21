package com.jimo.scala.oop

object ScalaConstruct {

  def main(args: Array[String]): Unit = {
    new JI()
    println("=========")
    new JI("jimo")

    new KI()
    println("=========")
    new KI("jimo")
  }
}

class J {
  var name: String = _
  println("J...")
}

class JI extends J {
  def this(name: String) {
    this
    this.name = name
    println("JI...")
  }
}

class K(name: String) {
  println("K")
}

class KI(name: String) extends K(name = name) {
  println("KI")

  def this() {
    this(null)
    println("KI:this")
  }
}