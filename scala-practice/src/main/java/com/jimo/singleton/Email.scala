package com.jimo.singleton

class Email(val username: String, val domainName: String) {
}

object Email {
  def fromString(emailString: String): Option[Email] = {
    emailString.split("@") match {
      case Array(a, b) => Some(new Email(a, b))
      case _ => None
    }
  }

  def main(args: Array[String]): Unit = {
    val e = Email.fromString("jimo@hehe.com")
    e match {
      case Some(e) => println(
        s"""Registered an email
           |Username: ${e.username}
           |Domain Name: ${e.domainName}
         """
      )
      case None => println("Error: could not parse email")
    }
  }
}
