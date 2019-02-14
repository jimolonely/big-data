package com.jimo

import java.text.DateFormat
import java.util.{Date, Locale}

object HelloWorld {
  def main(args: Array[String]): Unit = {
    println("hello world")

    val now = new Date
    val df = DateFormat.getDateInstance(DateFormat.LONG, Locale.CHINA)
    println(df format now)
  }


}
