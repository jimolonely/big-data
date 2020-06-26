package com.jimo.scala.oop2

object TraitDemo03 {
  def main(args: Array[String]): Unit = {
    val b = new MysqlDB with TB
    b.insert(1)

    new BB with TB {
      override def haha(): Unit = {}
    }
  }
}

trait TB {
  def insert(id: Int): Unit = {
    println("插入数据=" + id)
  }
}

class MysqlDB {}

abstract class BB {
  def haha(): Unit
}