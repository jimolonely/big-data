package com.jimo.abstracttype

object Main {
  def main(args: Array[String]): Unit = {

    val buf = newIntSeqBuf(3, 4)
    println("length=" + buf.length)
    println("content=" + buf.element)
  }

  def newIntSeqBuf(elem1: Int, elem2: Int): IntSeqBuffer =
    new IntSeqBuffer {
      override type T = List[U]
      override val element = List(elem1, elem2)
    }
}
