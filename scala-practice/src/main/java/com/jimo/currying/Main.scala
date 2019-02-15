package com.jimo.currying

object Main {

  def main(args: Array[String]): Unit = {
    val numbers = List(1, 2, 3, 4, 5, 6, 7, 8, 9)
    val res = numbers.foldLeft(0)((a, b) => a + b)
    println(res)

    println(numbers.foldLeft(0)({ (m: Int, n: Int) => m + n }))

    println(numbers.foldLeft(0)(_ + _))

    println((0 /: numbers) (_ + _))
    println((numbers :\ 0) (_ + _))

    val numberFunc = numbers.foldLeft(List[Int]()) _
    println(numberFunc((xs, x) => xs :+ x * x))
    println(numberFunc((xs, x) => xs :+ x * x * x))


  }
}
