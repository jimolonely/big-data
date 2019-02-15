package com.jimo.highorderfunc

class SalaryRaiser {

  private def promotion(salaries: List[Double], func: Double => Double): List[Double] =
    salaries.map(func)

  def smallPromotion(salaries: List[Double]): List[Double] =
    promotion(salaries, s => s * 1.1)

  def bigPromotion(salaries: List[Double]): List[Double] =
    promotion(salaries, s => s * s)
}
