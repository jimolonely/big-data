package com.jimo.scala.func

object FuncDemo06 {
  def main(args: Array[String]): Unit = {

    def myRunInThread(f1: () => Unit): Unit = {
      new Thread {
        override def run(): Unit = {
          f1()
        }
      }.start()
    }

    myRunInThread(() => {
      println("开始工作，3秒完成")
      Thread.sleep(3000)
      println("ok")
    })

    def myRunInThread2(f1: => Unit): Unit = {
      new Thread {
        override def run(): Unit = {
          f1
        }
      }.start()
    }

    myRunInThread2({
      println("开始工作，3秒完成")
      Thread.sleep(3000)
      println("ok")
    })

    var x = 10

    @scala.annotation.tailrec
    def until(condition: => Boolean)(block: => Unit): Unit = {
      if (!condition) {
        block
        until(condition)(block)
      }
    }

    until(x == 0) {
      println(x)
      x -= 1
    }
  }
}
