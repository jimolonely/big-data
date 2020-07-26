package com.jimo.scala.func

object FuncDemo05 {
  def main(args: Array[String]): Unit = {
    def makeSuffix(suffix: String) = {
      (name: String) => {
        if (name.endsWith(suffix)) {
          name
        } else {
          name + suffix
        }
      }
    }

    val jpg = makeSuffix(".jpg")
    println(jpg("a.jpg")) // a.jpg
    println(jpg("b")) // b.jpg
  }
}
