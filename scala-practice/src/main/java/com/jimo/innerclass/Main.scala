package com.jimo.innerclass

object Main {

  def main(args: Array[String]): Unit = {
    val graph1: Graph = new Graph
    val node1 = graph1.newNode
    val node2 = graph1.newNode
    val node3 = graph1.newNode
    node1.connectTo(node2)
    node3.connectTo(node1)

    //illegal
    //    val graph2:Graph = new Graph
    //    val node4 = graph2.newNode
    //    node1.connectTo(node4)
  }
}
