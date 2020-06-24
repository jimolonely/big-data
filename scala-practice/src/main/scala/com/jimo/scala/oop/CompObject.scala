package com.jimo.scala.oop

object CompObject {

  def main(args: Array[String]): Unit = {
    println(CCC.name)
  }
}

/**
 * 当类名和object名称一样，且放在同一个scala文件中
 * 1.object CCC 为伴生对象
 * 2.class CCC 为伴生类
 * 3.将静态内容写在伴生对象里，普通属性写在伴生类
 */
object CCC {
  var name: String = "jimo"
}

class CCC {
  var sex: Boolean = true
}
