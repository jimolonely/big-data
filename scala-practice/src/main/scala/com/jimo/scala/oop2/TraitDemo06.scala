package com.jimo.scala.oop2

object TraitDemo06 {
  def main(args: Array[String]): Unit = {
    val f1 = new F6
    println("===============")
    val k = new K6 with C6 with D6
    println(k)
  }
}

trait A6 {
  println("A6...")
}

trait B6 extends A6 {
  println("B6...")
}

trait C6 extends B6 {
  println("C6...")
}

trait D6 extends B6 {
  println("D6...")
}

class E6 {
  println("E6...")
}

class F6 extends E6 with C6 with D6 {
  println("F6...")
}

class K6 extends E6 {
  println("K6...")
}
