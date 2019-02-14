package com.jimo

import java.text.DateFormat
import java.util.{Date, Locale}

object HelloWorld {
  def main(args: Array[String]): Unit = {
    println("hello world")

    val now = new java.util.Date
    val df = DateFormat.getDateInstance(DateFormat.LONG, Locale.CHINA)
    println(df format now)

    val addOne = (x: Int) => x + 1

    val add = (x: Int, y: Int) => x + y

    val get = () => 32

  }

  def multiParams(x: Int, y: Int)(multi: Int): Int = (x + y) * multi

  def name: String = System.getProperty("user.name")

}
