package com.jimo.implicitparam

abstract class Monoid[A] {
  def add(a: A, b: A): A

  def unit: A
}

object ImplicitTest {

  implicit val stringMonoid: Monoid[String] = new Monoid[String] {
    override def add(a: String, b: String): String = a concat b

    override def unit: String = ""
  }

  implicit val intMonoid: Monoid[Int] = new Monoid[Int] {
    override def add(a: Int, b: Int): Int = a + b

    override def unit: Int = 0
  }

  def sum[A](xs: List[A])(implicit m: Monoid[A]): A =
    if (xs.isEmpty) m.unit else m.add(xs.head, sum(xs.tail))

  def main(args: Array[String]): Unit = {
    println(sum(List(1, 2, 3)))
    println(sum(List("a", "b", "c")))
  }
}
