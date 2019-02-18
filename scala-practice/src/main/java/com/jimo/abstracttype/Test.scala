package com.jimo.abstracttype

object Test {

  abstract class Buffer[+T] {
    val element: T
  }

  abstract class SeqBuffer[U, +T <: Seq[U]] extends Buffer[T] {
    def length = element.length
  }

  def newIntSeqBuf(e1: Int, e2: Int): SeqBuffer[Int, Seq[Int]] =
    new SeqBuffer[Int, List[Int]] {
      override val element = List(e1, e2)
    }

  def main(args: Array[String]): Unit = {
    val buf = newIntSeqBuf(5, 6)
    println(buf.length)
    println(buf.element)
  }
}
