package com.jimo.singleton

import scala.math._

class Circle(r: Double) {

  import Circle._

  def area: Double = calArea(r)
}

object Circle {
  private def calArea(r: Double): Double = Pi * pow(r, 2)

  def main(args: Array[String]): Unit = {
    val c = new Circle(1.11)
    println(c.area)
  }
}
