package com.jimo.operator

object Main {

  def main(args: Array[String]): Unit = {

    // 10 + 1
    val a = 10.+(1)
    println(a)

    // self define + operator
    val v1 = Vec(1.2, 3.4)
    val v2 = Vec(4.5, 6.7)
    println(v1 + v2)
  }

  case class Vec(x: Double, y: Double) {
    def +(that: Vec) = new Vec(this.x + that.x, this.y + that.y)
  }

  case class MyBool(x: Boolean) {
    def and(that: MyBool): MyBool = if (x) that else this

    def or(that: MyBool): MyBool = if (x) this else that

    def negate: MyBool = MyBool(!x)

    def not(x: MyBool): MyBool = x.negate

    def xor(x: MyBool, y: MyBool): MyBool = (x or y) and not(x or y)
  }

}
