
# 基本介绍

scala中的match是强大版switch语句。

入门案例：
```scala
    val op = "*"
    val n1 = 10
    val n2 = 20

    val res = op match {
      case "+" => n1 + n2
      case "-" => n1 - n2
      case "*" => n1 * n2
      case "/" => n1 / n2
      case _ => 0
    }
    println(res) // 200
```

# 注意事项

1. 如果没有匹配且没写 `case _`, 则会抛 `MatchError`
2. 每个case不需要break，会自动中断
3. match的类型很丰富

# 守卫

想要匹配某个范围的数据，可以增加条件守卫。

```scala
    for (i <- (0 to 10)) {
      i match {
        case 0 => println("start")
        case _ if i % 2 == 0 => println("偶数")
        case _ => println("其他数")
      }
    }
```

# 模式中的变量

```scala
    val c = 'H'
    c match {
      case '+' => println("ok")
      case myChar => println("Char:" + myChar)
      case _ => println("default")
    }
```

# 类型匹配

```scala
def typeMatch(res: Any): Unit = {
  res match {
    case a: Int => println("整数")
    case _: Map[String, Int] => println("map[String,Int]")
    case _: Array[String] => println("Array[String]")
    case _: Array[Int] => println("Array[Int]")
    case _: BigInt => println("BigInt")
    case _ => println("啥也不是")
  }
}

typeMatch(3)
typeMatch("hehe")
typeMatch(Map(("name", 18)))
typeMatch(Array(1, 2, 3))

整数
啥也不是
map[String,Int]
Array[Int]
```

注意：编译器会预检查类型，要是不匹配，会报错。

# 匹配数组

```scala
for (arr <- Array(Array(0), Array(1, 0), Array(0, 1, 0),
  Array(1, 1, 0), Array(1, 1, 0, 1), Array("hh", 123))) {
  val res = arr match {
    // 精确匹配Array(0)
    case Array(0) => "0"
    // 匹配2个元素的数组
    case Array(x, y) => s"(${x},${y})"
    case Array(0, _*) => "以0开头的数组"
    case _ => "默认"
  }
  println(res)
}

0
(1,0)
以0开头的数组
默认
默认
(hh,123)
```

# 匹配列表

```scala
for (list <- Array(List(0), List(1, 0), List(0, 0, 0), List(1, 0, 0))) {
  val res = list match {
    case 0 :: Nil => "0"
    case x :: y :: Nil => s"[${x},${y}]" // 2个元素
    case 0 :: tail => "0..." // 0开头的
    case _ => "其他"
  }
  println(res)
}

0
[1,0]
0...
其他
```

# 匹配元组

```scala
for (pair <- Array((0, 1), (1, 1), (1, 0, 2))) {
  val res = pair match {
    case (0, _) => "0..."
    case (y, 0) => y
    case _ => "其他"
  }
  println(res)
}

0...
其他
其他
```
