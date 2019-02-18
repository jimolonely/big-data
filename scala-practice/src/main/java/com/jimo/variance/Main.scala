package com.jimo.variance

object Main {

  def main(args: Array[String]): Unit = {

    val cats: List[Cat] = List(Cat("kate"), Cat("Tom"))
    val dogs: List[Dog] = List(Dog("gog"), Dog("dddd"))
    printAnimalName(cats)
    printAnimalName(dogs)

    val myCat: Cat = Cat("mimi")

    def printMyCat(printer: Printer[Cat]): Unit = printer.print(myCat)

    val catPrinter: CatPrinter = new CatPrinter
    val animalPrinter: AnimalPrinter = new AnimalPrinter
    printMyCat(catPrinter)
    printMyCat(animalPrinter)
  }

  def printAnimalName(animals: List[Animal]): Unit = {
    animals.foreach(a => println(a.name))
  }

}
