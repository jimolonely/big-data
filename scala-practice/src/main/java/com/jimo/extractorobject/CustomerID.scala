package com.jimo.extractorobject

import scala.util.Random

object CustomerID {

  def apply(name: String): String = {
    val id = s"$name--${Random.nextLong}"
    println(id)
    id
  }

  def unapply(customerID: String): Option[String] = {
    println("unapply")
    val stringArray: Array[String] = customerID.split("--")
    if (stringArray.tail.nonEmpty) Some(stringArray.head) else None
  }

  def main(args: Array[String]): Unit = {
    // apply
    val customer2ID = CustomerID("Jimo")
    // unapply
    val CustomerID(name) = customer2ID
    println(name)

    // match errror
    val CustomerID(name2) = "-dskfjsd"
    println(name2)
  }
}
