package com.jimo

object TypeDemo {
  def main(args: Array[String]): Unit = {
    val list: List[Any] = List(
      "a String",
      123,
      'c',
      true,
      () => "sdfsdf"
    )
    list.foreach(e => println(e))
  }
}
