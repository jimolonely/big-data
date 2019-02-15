package com.jimo.highorderfunc

object Main {

  def main(args: Array[String]): Unit = {
    val salaries = Seq(1000, 20000, 300000)
    val doubleSalary = (x: Int) => x * 2
    val newSalaries = salaries.map(doubleSalary)
    newSalaries.foreach(println)

    val newSalaries2 = salaries.map(x => x * 2)
    newSalaries2.foreach(println)

    val newSalaries3 = salaries.map(_ * 2)
    newSalaries3.foreach(println)

    val f = WeatherForecast(Seq(10.1, 20, 23, 45, 12))
    f.forecastInFahrenheit.foreach(println)


    val domainName = "www.jimo.com"

    def getUrl = urlBuilder(true, domainName)

    val url = getUrl("users", "id=1")
    println(url)

    println(factorial(10))
    println(factorial(3))
  }

  def urlBuilder(ssl: Boolean, domainName: String): (String, String) => String = {
    val schema = if (ssl) "https://" else "http://"
    (endpoint: String, query: String) => s"$schema$domainName/$endpoint?$query"
  }

  def factorial(x: Int): Int = {
    def fact(x: Int, accumulator: Int): Int = {
      if (x <= 1) accumulator
      else fact(x - 1, x * accumulator)
    }

    fact(x, 1)
  }

}
