package com.jimo.regex

import scala.util.matching.Regex

object Main {

  def main(args: Array[String]): Unit = {
    val numberPattern: Regex = "[0-9]".r
    numberPattern.findFirstMatchIn("asdsafsdfsd") match {
      case Some(_) => println("password ok")
      case None => println("password has no number ")
    }

    val keyValPattern: Regex = "([0-9a-zA-Z-#() ]+): ([0-9a-zA-Z-#() ]+)".r

    val input: String =
      """background-color: #A03300;
        |background-image: url(img/header100.png);
        |background-position: top center;
        |background-repeat: repeat-x;
        |background-size: 2160px 108px;
        |margin: 0;
        |height: 108px;
        |width: 100%;""".stripMargin
    for (patternMatch <- keyValPattern.findAllMatchIn(input)) {
      println(s"key: ${patternMatch.group(1)} value: ${patternMatch.group(2)}")
    }
  }
}
