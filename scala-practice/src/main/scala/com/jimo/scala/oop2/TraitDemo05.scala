package com.jimo.scala.oop2

object TraitDemo05 {
  def main(args: Array[String]): Unit = {
    val mysql = new Mysql5 with Op5 {
      override var opType: String = "mysql-insert"
    }
    println(mysql.opType)
    val mysql1 = new Mysql5 with DB5
    println(mysql1.opType)
  }
}

trait Op5 {
  var opType: String
}

trait DB5 extends Op5 {
  override var opType: String = "db-insert"
}

class Mysql5 {}
