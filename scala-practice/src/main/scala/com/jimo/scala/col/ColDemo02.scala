package com.jimo.scala.col

object ColDemo02 {
  def main(args: Array[String]): Unit = {
    // 方式1
    val ints = new Array[Int](4)
    println(ints.length) // 4
    ints(3) = 9
    for (i <- ints) {
      println(i)
    }

    // 方式2
    // Array[Int]
    val arr01 = Array(1, 2)
    arr01.foreach(println(_))
    // Array[Any]
    val arr02 = Array(1, 3, "hello")
    arr02.foreach(println)
  }
}
