package com.jimo.scala.patternmatch

object MatchDemo04 {
  def main(args: Array[String]): Unit = {
    List(1, 3, 4, 6) match {
      case first :: second :: rest => println(first + " " + second + " " + rest.length)
      case _ => println("匹配不到")
    } // 1 3 2

    List(1) match {
      case first :: second :: rest => println(first + " " + second + " " + rest.length)
      case _ => println("匹配不到")
    } // 匹配不到

    // 书的金额: (((100+25)-50) + 20+30) - 10 = 115
    val sale = Bundle("书籍", 10, Book("漫画", 20), Book("小说", 30),
      Bundle("文学作品", 50, Book("《围城》", 100), Book("《活着》", 25)))

    // 1.拿到漫画
    val res = sale match {
      case Bundle(_, _, Book(desc, _), _*) => desc
    }
    println(res) // 漫画

    // 2.通过@语法将嵌套的值绑定到变量
    val res2 = sale match {
      case Bundle(_, _, art@Book(_, _), rest@_*) => (art, rest)
    }
    println(res2) // (Book(漫画,20.0),WrappedArray(Book(小说,30.0), Bundle(文学作品,50.0,WrappedArray(Book(《围城》,100.0), Book(《活着》,25.0)))))

  }
}

abstract class Item

case class Book(desc: String, price: Double) extends Item

// 打包销售,打折
// item里的价格 - discount就是最后的价格
case class Bundle(desc: String, discount: Double, item: Item*) extends Item
