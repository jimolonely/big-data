package com.jimo.scala.oop

object DemoConstruct {

  def main(args: Array[String]): Unit = {
    val jimo = new Student("jimo", 18)
    jimo.hello()

//    val teacher = new Teacher()
  }
}

class Student(name: String, ageIn: Int) {

  def this(name: String) {
    this(name, 0)
  }

  def this(age: Int) {
    this(null, age)
  }

  def this() {
    this("jimo")
  }

  var age: Int = ageIn
  println("hello 1")

  def hello(): Unit = {
    println("hello,I am " + name + ",age = " + age)
  }

  println("hello 2")

  age += 10
}

class Teacher private() {
  def this(name: String) {
    this()
  }
}