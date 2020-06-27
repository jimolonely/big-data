package com.jimo.scala.oop2

object TraitDemo04 {
  def main(args: Array[String]): Unit = {
    val mysql = new Mysql with DB with File
    mysql.insert(1)
  }
}

class Mysql {}

trait Op {
  println("Op...")

  def insert(id: Int): Unit
}

trait Data extends Op {
  println("Data...")

  override def insert(id: Int): Unit = {
    println("插入数据+" + id)
  }
}

trait DB extends Data {
  println("DB...")

  override def insert(id: Int): Unit = {
    println("向数据库 ")
    super.insert(id)
  }
}

trait File extends Data {
  println("File...")

  override def insert(id: Int): Unit = {
    println("向文件 ")
    super[Data].insert(id)
    //    super.insert(id)
  }
}
