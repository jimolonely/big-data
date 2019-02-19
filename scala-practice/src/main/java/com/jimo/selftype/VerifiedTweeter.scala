package com.jimo.selftype

trait User {
  def username: String
}

trait Tweeter {
  // self type
  this: User =>

  def tweet(text: String): Unit = println(s"$username: $text")
}

class VerifiedTweeter(val username_ : String) extends Tweeter with User {
  def username = s"real $username_"
}

object Test {
  def main(args: Array[String]): Unit = {
    val realJimo = new VerifiedTweeter("jimo")
    realJimo.tweet("heheheheheh")
  }
}
