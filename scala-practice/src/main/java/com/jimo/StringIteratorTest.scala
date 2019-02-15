package com.jimo

object StringIteratorTest {

  class RichStringIter extends StringIterator("scala") with RichIterator

  def main(args: Array[String]): Unit = {
    val richStringIter = new RichStringIter
    richStringIter foreach println
  }
}
