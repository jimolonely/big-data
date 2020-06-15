package com.jimo.scala.oop

object Demo01 {
  def main(args: Array[String]): Unit = {
    var p1 = new Person
    p1.name = "jimo"
    p1.age = 18
    println(p1.name, p1.age)
  }
}

class Person {
  var name: String = _
  var age: Int = 0
}
