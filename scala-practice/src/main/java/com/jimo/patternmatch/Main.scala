package com.jimo.patternmatch

import scala.util.Random

object Main {

  def main(args: Array[String]): Unit = {
    val x = Random.nextInt(10)

    val s = x match {
      case 0 => "zero"
      case 1 => "one"
      case 2 => "two"
      case _ => "many"
    }
    println(s)

    val sms = SMS("12345", "are you ok")
    val voice = VoiceRecording("Jimo", "xx.org")
    println(showNotification(sms))
    println(showNotification(voice))
  }

  def showNotification(notification: Notification): String = {
    notification match {
      case Email(sender, title, _) =>
        s"you got an email from $sender with title:$title"
      case SMS(number, msg) =>
        s"you got a msg from $number! Message: $msg"
      case VoiceRecording(name, link) =>
        s"you received a Voice Recording from $name,click link: $link"
    }
  }
}
