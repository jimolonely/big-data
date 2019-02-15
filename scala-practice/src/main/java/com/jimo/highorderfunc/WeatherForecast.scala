package com.jimo.highorderfunc

case class WeatherForecast(temperatures: Seq[Double]) {
  private def convertCToF(temp: Double) = temp * 1.8 + 32

  // convert method to function by force
  def forecastInFahrenheit: Seq[Double] = temperatures.map(convertCToF)
}
