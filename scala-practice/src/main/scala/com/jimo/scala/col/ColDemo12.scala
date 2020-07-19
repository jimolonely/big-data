package com.jimo.scala.col

import scala.collection.mutable.ArrayBuffer

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

    println(nums.scanLeft(1)(_ + _)) // List(1, 2, 4, 7, 11, 16, 22, 29)
    println(nums.scanLeft(28)(_ - _)) // List(28, 27, 25, 22, 18, 13, 7, 0)
    println(nums.scanRight(1)(_ + _)) // List(29, 28, 26, 23, 19, 14, 8, 1)
    println(nums.scanRight(4)(_ - _)) // List(0, 1, 1, 2, 2, 3, 3, 4)

    // 1.
    val s = "AAAAAAAAAAAAAABBBBBBBBBBSDDDDDDDDDD"
    val buf = ArrayBuffer[Char]()

    println(s.foldLeft(buf)((b, c) => {
      b.append(c);
      b
    }))
    println(buf)
    // ArrayBuffer(A, A, A, A, A, A, A, A, A, A, A, A, A, A, B, B, B, B, B, B, B, B, B, B, S, D, D, D, D, D, D, D, D, D, D)

    // 2. Map(A -> 14, B -> 10, S -> 1, D -> 10)
    println(s.foldLeft(Map[Char, Int]())((map, c) => map + (c -> (map.getOrElse(c, 0) + 1))))

    val list1 = List(1, 2, 3, 8)
    val list2 = List(4, 5, 6)
    println(list1.zip(list2)) // List((1,4), (2,5), (3,6))

    val list3 = List(2, 3, 4, 5)
    val it = list3.iterator
    while (it.hasNext) {
      println(it.next())
    }

    def numAdd(n: BigInt): Stream[BigInt] = n #:: numAdd(n + 1)

    val stream1 = numAdd(1)
    println(stream1) // Stream(1, ?)
    // 我希望再取一个数据
    println(stream1.tail) // Stream(2, ?)
    println(stream1) // Stream(1, 2, ?)
    println(stream1.head) // 1
    println(stream1.take(2)) // Stream(1, ?)

    val viewSeq = (1 to 10).view.map(_ * 2).filter(_ % 2 == 0)
    println(viewSeq) // SeqViewMF(...)
    viewSeq.foreach(println)
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
