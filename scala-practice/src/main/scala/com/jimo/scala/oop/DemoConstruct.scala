package com.jimo.scala.oop

object DemoConstruct {

  def main(args: Array[String]): Unit = {
    val jimo = new Student("jimo", 18)
    jimo.hello()
  }
}

class Student(name: String, age: Int) {

  def hello(): Unit = {
    println("hello,I am " + name)
  }
}
