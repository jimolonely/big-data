package com.jimo.defaultparam

object Main {

  def log(msg: String = "", level: String = "INFO"): Unit = println(s"$level: $msg")

  def main(args: Array[String]): Unit = {
    log("hehe")
    log("hehe", "DEBUG")
    log(level = "DEBUG")
  }
}
