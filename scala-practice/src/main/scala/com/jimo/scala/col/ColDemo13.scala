package com.jimo.scala.col

object ColDemo13 {
  def main(args: Array[String]): Unit = {
    val `var`: Int = 100
    println(`var`)

    // 后置操作符
    val n1 = 2
    val n2 = 3
    println(n1.+(n2)) // 等价于 n1 + n2

    val p = new Person
    p.+(10)
    println(p.age) // 10
    p + 10
    println(p.age) // 20

    val op = new Operate
    println(op ++)
    println(op.++)

    val op2 = new Op2
    !op2 // !!!!
  }

  class Person {
    var age: Int = 0

    def +(n: Int): Unit = {
      age += n
    }
  }

  class Operate {
    def ++ = "123"
  }

}

class Op2 {
  // unary:一元运算符
  def unary_!(): Unit = println("!!!!")
}
