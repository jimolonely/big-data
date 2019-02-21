package com.jimo

import org.apache.spark.sql.SparkSession

object SimpleApp {

  def main(args: Array[String]): Unit = {
    val logFile = "/home/jack/workspace/Git/big-data/spark-practice/src/main/scala/com/jimo/SimpleApp.scala"
    val builder = SparkSession.builder()
    //    builder.master("local")
    val spark = builder.appName("Jimo App").getOrCreate()
    //    val conf = new SparkConf().setAppName("Jimo App")
    //    conf.setMaster("local")
    //    val spark = new SparkContext(conf)
    val data = spark.read.textFile(logFile)
    val numA = data.filter(line => line.contains("a")).count()
    val numB = data.filter(line => line.contains("b")).count()
    println(s"line with a: $numA; line with b: $numB")
    spark.stop()

    //    val conf = new SparkConf().setAppName("mySpark")
    //    //setMaster("local") 本机的spark就用local，远端的就写ip
    //    //如果是打成jar包运行则需要去掉 setMaster("local")因为在参数中会指定。
    //    conf.setMaster("spark://localhost:7077")
    //    val sc = new SparkContext(conf)
    //    val rdd = sc.parallelize(List(1, 2, 3, 4, 5, 6)).map(_ * 3)
    //    val mappedRDD = rdd.filter(_ > 10).collect()
    //    //对集合求和
    //    println(rdd.reduce(_ + _))
    //    //输出大于10的元素
    //    for (arg <- mappedRDD)
    //      print(arg + " ")
    //    println()
    //    println("math is work")
  }
}
