
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

