package com.jimo.scala.col

import scala.collection.mutable.ArrayBuffer


object ColDemo05 {
  def main(args: Array[String]): Unit = {
    val c = new C5
    B5.test(c)

    val arr = ArrayBuffer("1", "2", "3")
    import scala.collection.JavaConversions.bufferAsJavaList

    val builder = new ProcessBuilder(arr)
    val list = builder.command()
    println(list)

    // java to scala
    import scala.collection.JavaConversions.asScalaBuffer
    import scala.collection.mutable

    val scalaArr: mutable.Buffer[String] = list
    println(scalaArr)
  }
}

trait T5 {}

class C5 extends T5 {}

object B5 {
  def test(m: T5): Unit = {
    println(s"b ok：$m")
  }
}
