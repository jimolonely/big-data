package com.jimo.generic

class Stack[A] {
  private var elems: List[A] = Nil

  def push(x: A): Unit = {
    elems = x :: elems
  }

  def peek: A = elems.head

  def pop(): A = {
    val currentTop = peek
    elems = elems.tail
    currentTop
  }
}
