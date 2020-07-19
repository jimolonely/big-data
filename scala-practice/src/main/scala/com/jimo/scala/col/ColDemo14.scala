package com.jimo.scala.col

object ColDemo14 {

  def main(args: Array[String]): Unit = {
    val pair = new Pair[String, Int]("jimo", 18)
    println(pair) // s=18,t=jimo
    val pair2 = pair.swap()
    println(pair2) // s=jimo,t=18

    val p = new Pair[String, String]("jimo", "hehe")
    val p2 = p.swapSame
    println(p2) // s=hehe,t=jimo
  }
}

final class Pair[T, S](t: T, s: S) {

  def swap(): Pair[S, T] = new Pair(s, t)

  override def toString: String = (s"s=${s},t=${t}")

  // S =:= T 表示 S和T的类型要一致
  def swapSame(implicit env: S =:= T) = new Pair(t, s)
}
