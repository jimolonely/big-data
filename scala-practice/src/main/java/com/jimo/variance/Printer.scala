package com.jimo.variance

abstract class Printer[-A] {
  def print(value: A): Unit
}

class AnimalPrinter extends Printer[Animal] {
  override def print(value: Animal): Unit =
    println("the name of animal is: " + value.name)
}

class CatPrinter extends Printer[Cat] {
  override def print(value: Cat): Unit =
    println("the cat's name is: " + value.name)
}
