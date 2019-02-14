package com.jimo

//class Point(var x: Int = 0, var y: Int = 0) {
class Point {
  private var _x = 0
  private var _y = 0
  private val bound = 100

  def x = _x
  def x_= (newX: Int): Unit = {
    if (newX < bound) _x = newX else printWarning
  }

  def y = _y
  def y_= (newValue: Int): Unit = {
    if (newValue < bound) _y = newValue else printWarning
  }

  private def printWarning = println("WARNING: Out of bounds")
}
