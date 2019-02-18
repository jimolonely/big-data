package com.jimo.generic

class LowerTypeBound {

  trait Node[+B] {
    def prepend[U >: B](elem: U): Node[U]
  }

  case class ListNode[+B](h: B, t: Node[B]) extends Node[B] {
    override def prepend[U >: B](elem: U): Node[U] = ListNode(elem, this)

    def head: B = h

    def tail: Node[B] = t
  }

  case class Nil[+B]() extends Node[B] {
    override def prepend[U >: B](elem: U): Node[U] = ListNode(elem, this)
  }

  trait Bird

  case class AfricanSwallow() extends Bird

  case class EuropeanSwallow() extends Bird

  def main(args: Array[String]): Unit = {
    val africanSwallow = ListNode[AfricanSwallow](AfricanSwallow(), Nil())
    val birdList: Node[Bird] = africanSwallow
    birdList.prepend(new EuropeanSwallow)
  }
}
