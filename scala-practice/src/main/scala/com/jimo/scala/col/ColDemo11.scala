package com.jimo.scala.col

import scala.collection.mutable

object ColDemo11 {
  def main(args: Array[String]): Unit = {
    // 创建
    val s1 = new mutable.HashSet[Int]()
    s1.add(1)
    s1.add(2)
    s1.add(2)
    s1.add(1)
    println(s1)

    val s2 = mutable.Set(1, 2, 3, 4, 3, 2, 3)
    println(s2) // Set(1, 2, 3, 4)
    // 默认不可变
    val s3 = Set(1, 2, 3, 4, 3, 2, 3)
    println(s3) // Set(1, 2, 3, 4)

    // 添加元素
    s2 += 4
    s2 += 5
    println(s2) // Set(1, 5, 2, 3, 4)
    // 返回新的
    val s4 = s2.+(6)
    println(s4) // Set(1, 5, 2, 6, 3, 4)

    // 删除
    s2 -= 4
    s2.remove(3)
    println(s2) // Set(1, 5, 2)

    // 遍历
    for (elem <- s2) {
      println(elem)
    }

    // 交集
    println(s1 & s2) // Set(1, 2)
    // 差集
    println(s1 &~ s2) // Set()
    // 并集
    println(s1 ++ s2) // Set(1, 5, 2)
  }
}
