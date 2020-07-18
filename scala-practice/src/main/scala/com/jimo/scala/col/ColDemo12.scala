package com.jimo.scala.col

object ColDemo12 {
  def main(args: Array[String]): Unit = {
    println(test(twoFold, 2.0))

    val l1 = List(1, 2, 3)
    println(l1.map(_ * 2)) // List(2, 4, 6)

    val names = List("Hehe", "Jimo", "Lily")
    println(names.map(_.toUpperCase())) // List(HEHE, JIMO, LILY)

    // List(H, E, H, E, J, I, M, O, L, I, L, Y)
    println(names.flatMap(_.toUpperCase()))

    println(names.filter(_.startsWith("J"))) // List(Jimo)
    println(names.filter(_.startsWith("J")).map(_.toUpperCase()))

    val nums = List(1, 2, 3, 4, 5, 6, 7)
    println(nums.reduce(_ + _)) // 28
    println(nums.sum) // 28
    /**
     * 1-2=-1
     * -1-3=-4
     * ...
     * -19-7=-26
     */
    println(nums.reduceLeft(_ - _)) // -26
    /**
     * 6-7=-1
     * 5-(-1)=6
     * 4-6=-2
     * 3-(-2)=5
     * 2-5=-3
     * 1-(-3)=4
     */
    println(nums.reduceRight(_ - _)) // 4
    println(nums.max)

    println(nums.fold(1)(_ + _)) // 29
    println(nums.foldLeft(1)(_ + _)) // 29
    println(nums.foldRight(1)(_ + _)) // 29
    println(nums.foldLeft(28)(_ - _)) // 0
    println(nums.foldRight(4)(_ - _)) // 0

    // foldLeft
    println((1 /: nums) (_ + _)) // 29
    println((28 /: nums) (_ - _)) // 0
    // foldRight
    println((nums :\ 1) (_ + _)) // 29
    println((nums :\ 4) (_ - _)) // 0
  }

  /**
   * 以函数作为参数的函数
   */
  def test(f: Double => Double, d: Double): Double = {
    f(d)
  }

  def twoFold(d: Double): Double = {
    2 * d
  }
}
