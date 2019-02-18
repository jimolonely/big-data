package com.jimo.ford

object Main {

  case class User(name: String, age: Int)

  def main(args: Array[String]): Unit = {

    val userBase = List(User("jimo", 10),
      User("hehe", 20),
      User("lili", 30),
      User("keke", 40)
    )
    val twenty = for (user <- userBase if user.age >= 20 && user.age < 30)
      yield user.name

    twenty.foreach(name => println(name))

    // for until
    foo(10, 10) foreach {
      case (i, j) => println(s"($i,$j)")
    }
  }

  def foo(n: Int, v: Int) =
    for (i <- 0 until n;
         j <- i until n if i + j == v)
      yield (i, j)
}
