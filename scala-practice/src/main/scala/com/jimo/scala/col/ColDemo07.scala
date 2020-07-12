package com.jimo.scala.col

object ColDemo07 {
  def main(args: Array[String]): Unit = {
    val list1 = List(1, 2, "AA")
    println(list1) // List(1,2,AA)

    // 空集合
    val list2 = Nil
    println(list2) // List()

    println(list1(2)) // AA

    // 追加:返回新的集合/列表，与Java不一样
    // 右边追加
    val list3 = list1 :+ 4
    println(list3) // List(1, 2, AA, 4)
    // 左边追加
    val list4 = 33 +: list1
    println(list4) // List(33, 1, 2, AA)

    val list5 = List(3, 4, "jimo")
    val list6 = 4 :: 5 :: list5 :: 3 :: Nil
    // 从右往左，从后往前塞
    println(list6) // List(4, 5, List(3, 4, jimo), 3)
    val list7 = 4 :: 5 :: list5 ::: 3 :: Nil
    // 解构
    println(list7) // List(4, 5, 3, 4, jimo, 3)
  }
}
