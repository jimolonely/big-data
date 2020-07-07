package com.jimo.scala.jimplicit

object ImplicitDemo02 {
  def main(args: Array[String]): Unit = {
    implicit def addDelFunc(mysql: Mysql): DB = {
      new DB
    }

    val mysql = new Mysql
    mysql.insert()
    mysql.delete()
  }
}

class Mysql {
  def insert(): Unit = {
    println("insert")
  }
}

class DB {
  def delete(): Unit = {
    println("delete")
  }
}
