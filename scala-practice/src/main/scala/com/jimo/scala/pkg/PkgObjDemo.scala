package com.jimo.scala.pkg {

  package object web {
    def search(): Unit = {
      println("search")
    }
  }
  package web {

    object Web {
      def main(args: Array[String]): Unit = {
        search()
      }
    }

  }

}
