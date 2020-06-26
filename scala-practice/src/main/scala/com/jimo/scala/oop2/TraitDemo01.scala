package com.jimo.scala.oop2

object TraitDemo01 {
  def main(args: Array[String]): Unit = {

  }
}

trait Move {
  def fly(): Unit
}

abstract class Plane {}

class Airliner extends Plane with Move {
  override def fly(): Unit = {
    println("客机使用翅膀飞行")
  }
}

class Helicopter extends Move {
  override def fly(): Unit = {
    println("直升机使用螺旋桨飞行")
  }
}