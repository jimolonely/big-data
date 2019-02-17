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

    val info = Seq("12345", "jimo@jj.com")
    val email = Email("jimo@jj.com", "hehe", "like you")
    println(showImportantNotification(email, info))
    println(showImportantNotification(sms, info))

    println(goIdle(Phone("huawei")))
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

  def showImportantNotification(notification: Notification, info: Seq[String]): String = {
    notification match {
      case Email(email, _, _) if info.contains(email) =>
        "got important email!"
      case SMS(caller, _) if info.contains(caller) =>
        "got important sms"
      case other => showNotification(other)
    }
  }

  def goIdle(device: Device) = device match {
    case p: Phone => p.screenOff
    case c: Computer => c.screenSaveOn
  }

  sealed abstract class Furniture

  case class Table() extends Furniture

  case class Chair() extends Furniture

  def findPlaceToSit(piece: Furniture): String = piece match {
    case t: Table => "center"
    case c: Chair => "wall"
  }
}
