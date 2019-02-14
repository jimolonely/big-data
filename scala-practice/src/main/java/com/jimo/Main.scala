package com.jimo

import scala.collection.mutable.ArrayBuffer

object Main {
  def main(args: Array[String]): Unit = {
    val p = new Point
    p.x = 10
    p.y = 100

    val cat = new Cat("jimo")
    val pets = ArrayBuffer.empty[Pet]
    pets.append(cat)
    pets.append(cat)
    pets.foreach(pet => println(pet.name))

    // tuple
    //    val t = ("jimo", 100): Tuple2(String, Int)
    val t = ("jimo", 100): (String, Int)
    println(t._1)
    println(t._2)

    val (name, age) = t
    println(name)
    println(age)

    val plantDist = List(("Mercury", 57.9), ("Venus", 108.2), ("Earth", 149.6), ("Mars", 227.9), ("Jupiter", 778.3))
    plantDist.foreach(t => {
      t match {
        case ("Mercury", dist) => println(s"Mercury is $dist")
        case p if (p._1 == "Venus") => println(s"Venus is ${p._2}")
        case p if (p._1 == "Earth") => println(s"Earth is ${p._2}")
        case _ => println("too far...")
      }
    })

    val nums = List((2, 5), (5, 6), (9, 2))
    for ((a, b) <- nums) {
      println(a * b)
    }
  }
}
