package com.jimo.scala.col

import scala.collection.{immutable, mutable}

object ColDemo10 {
  def main(args: Array[String]): Unit = {
    val m1 = new mutable.HashMap[String, Int]()
    m1.put("k1", 10)
    m1.put("k2", 20)
    m1.put("k3", 30)
    println(m1) // Map(k2 -> 20, k1 -> 10, k3 -> 30)
    println(m1.get("k2")) // Some(20)

    // 不可变
    val m2 = immutable.Map("a" -> 11, "b" -> 22, "c" -> 33)
    println(m2) // Map(a -> 11, b -> 22, c -> 33)
    println(m2("b"))
    for (e <- m2) {
      print(s"key=${e._1},value=${e._2},")
    } // key=a,value=11,key=b,value=22,key=c,value=33,

    // 二元组的方式构建
    val m3 = mutable.Map(("name", "jimo"), ("age", 18))
    println(m3)
    println(m3.get("height"))
    //    println(m3("height"))

    // 取值
    println(m3.getOrElse("name", "none"))

    // 添加元素
    val m4 = mutable.Map((0, 2))
    m4 += (1 -> 2)
    m4 += (2 -> 3)
    println(m4) // Map(2 -> 3, 1 -> 2, 0 -> 2)

    // 添加多个元素
    m4 += (3 -> 4, 5 -> 6)
    println(m4) // Map(2 -> 3, 5 -> 6, 1 -> 2, 3 -> 4, 0 -> 2)

    // 删除元素
    m4 -= (1, 2, 100)
    println(m4) // Map(5 -> 6, 3 -> 4, 0 -> 2)

    // 遍历
    for ((k, v) <- m4) {
      println(k, v)
    }
    for (elem <- m4.values) {
      println(elem)
    }
    for (elem <- m4.keys) {
      println(elem)
    }
    for (t <- m4) {
      println(t._1, t._2)
    }
  }
}
