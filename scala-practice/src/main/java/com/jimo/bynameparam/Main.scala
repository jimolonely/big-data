package com.jimo.bynameparam

object Main {

  def whileLoop(con: => Boolean)(body: => Unit): Unit = {
    if (con) {
      body
      whileLoop(con)(body)
    }
  }

  def whileLoop2(con: Boolean)(body: Unit): Unit = {
    if (con) {
      println(con)
      body
      whileLoop2(con)(body)
    }
  }

  def main(args: Array[String]): Unit = {

    var i = 2
    whileLoop(i > 0) {
      println(i)
      i -= 1
    }

    println("--------------")
    i = 2
    whileLoop2(i > 0) {
      println(i)
      i -= 1
    }
  }
}
