package com.jimo.generic

object Main {

  def main(args: Array[String]): Unit = {
    val stack = new Stack[Int]
    stack.push(1)
    stack.push(2)
    println(stack.pop())
    println(stack.pop())
  }
}
