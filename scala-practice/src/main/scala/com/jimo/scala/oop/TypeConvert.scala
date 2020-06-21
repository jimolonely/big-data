package com.jimo.scala.oop

object TypeConvert {
  def main(args: Array[String]): Unit = {
    println(classOf[String])
    println(classOf[P])
    val s = "s"
    println(s.getClass.getName)
    println(s.isInstanceOf[String])

    val pi = new PI
    val p: P = pi.asInstanceOf[P]
    println(p.isInstanceOf[P], p.isInstanceOf[PI])
  }
}

class P {

}

class PI extends P {

}
