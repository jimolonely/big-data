package com.jimo.scala.col

import scala.collection.mutable

object ColDemo09 {
  def main(args: Array[String]): Unit = {
    val q = new mutable.Queue[Int]
    q += 1
    q += 1
    q ++= List(2, 3, 4)
    println(q) // Queue(1, 1, 2, 3, 4)

    val q2 = new mutable.Queue[Any]()
    q2 += 10
    q2 += List(1, 2, 3)
    println(q2) // Queue(10, List(1, 2, 3))

    // delete
    q.dequeue()
    println(q) // Queue(1, 2, 3, 4)
    q.enqueue(6, 7, 8)
    println(q) // Queue(1, 2, 3, 4, 6, 7, 8)

    println(q.head, q) // (1,Queue(1, 2, 3, 4, 6, 7, 8))
    println(q.last, q) // (8,Queue(1, 2, 3, 4, 6, 7, 8))
    // 返回除第一个以外的后元素
    println(q.tail) // Queue(2, 3, 4, 6, 7, 8)
    println(q.tail.tail) // Queue(3, 4, 6, 7, 8)
  }
}
