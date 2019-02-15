package com.jimo.caseclass

object Main {
  def main(args: Array[String]): Unit = {
    val msg1 = Message("jimo", "hehe", "i like you")
    println(msg1.body)
    //    msg1.sender = "sss"

    val msg2 = Message("jimo", "hehe", "i like you")
    println(msg1 == msg2)

    val msg3 = msg2.copy(recipient = "jjjj")
    println(msg3.toString)
  }
}
