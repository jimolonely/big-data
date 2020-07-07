package com.jimo.scala.jimplicit

object ImplicitDemo044 {
  def main(args: Array[String]): Unit = {
    implicit class MysqlToDB(mysql: Mysql) {
      def close(): Unit = {
        println("close")
      }
    }

    val mysql = new Mysql
    mysql.insert()
    mysql.close()
  }
}
