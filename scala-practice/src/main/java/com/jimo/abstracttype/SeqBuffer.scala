package com.jimo.abstracttype

abstract class SeqBuffer extends Buffer {
  type U
  type T <: Seq[U]

  def length: Int = element.length
}
