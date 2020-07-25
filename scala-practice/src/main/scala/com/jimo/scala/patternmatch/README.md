
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

# 对象匹配

什么才算对象匹配？

1. case中对象的unapply方法返回Some集合则为匹配成功
2. 返回none则为失败

```scala
object Square {
  def unapply(z: Double): Option[Double] = Some(math.sqrt(z))

  def apply(z: Double): Double = z * z
}

val number: Double = 49.0
number match {
  case Square(n) => println(n) // 7.0
  case _ => println("不匹配")
}
```
再看：
```scala
val n = Square(10)
n match {
  case Square(n) => println(n) // 10.0
  case _ => println("不匹配")
}
```
再看看: 中间是调用了apply方法的
```scala
object Square2 {
  def unapply(z: Double): Option[Double] = Some(z + 1)

  def apply(z: Double): Double = z * z
}

val n1 = Square2(10)
n1 match {
  case Square2(n) => println(n) // 101.0 = 10*10+1
  case _ => println("不匹配")
}
```

再看多个参数的匹配 `unapplySeq`
```scala
object Name {
  def unapplySeq(s: String): Option[Seq[String]] = {
    if (s.contains(",")) Some(s.split(","))
    else None
  }
}

val names = "jimo,hehe,lily"

names match {
  case Name(a, b, c) => println(s"${a}--${b}--${c}")
  case Name(a, b) => println(s"${a}==${b}")
  case _ => println("不匹配")
} // jimo--hehe--lily
```

# 样例类

是被 case 修饰的类

* 为了模式匹配而优化
* 构造器每一个参数都为val，除非被显示声明为var
* 样例类的伴生对象提供apply方法，这样我们不用new
* 提供unapply方法让模式匹配可以工作

```scala
def main(args: Array[String]): Unit = {
    for (amt <- Array(Dollar(100.0), Currency(1000, "RMB"), NoAmount)) {
      val res = amt match {
        case Dollar(v) => "$" + v
        case Currency(v, u) => v + u
        case NoAmount => "没钱"
      }
      println(amt + ":" + res)
    }
}

abstract class Amount

case class Dollar(value: Double) extends Amount

case class Currency(value: Double, unit: String) extends Amount

case object NoAmount extends Amount

Dollar(100.0):$100.0
Currency(1000.0,RMB):1000.0RMB
NoAmount:没钱
```

样例类的copy方法

```scala
val c1 = Currency(19.9, "RMB")
val c2 = c1.copy()
val c3 = c1.copy(value = 29.9)
val c4 = c1.copy(unit = "英镑")
println(c2)
println(c3)
println(c4)

Currency(19.9,RMB)
Currency(29.9,RMB)
Currency(19.9,英镑)
```

查看其编译后的源码可知生成了copy方法：
```java
  
  public double value() {
    return this.value;
  }
  
  public String unit() {
    return this.unit;
  }
  
  public Currency copy(double value, String unit) {
    return new Currency(value, unit);
  }
  
  public double copy$default$1() {
    return value();
  }
  
  public String copy$default$2() {
    return unit();
  }
```

# case中的中置表达式

```scala
List(1, 3, 4, 6) match {
  case first :: second :: rest => println(first + " " + second + " " + rest.length)
  case _ => println("匹配不到")
} // 1 3 2

List(1) match {
  case first :: second :: rest => println(first + " " + second + " " + rest.length)
  case _ => println("匹配不到")
} // 匹配不到
```

# 匹配嵌套结构

准备上下文：
```scala
abstract class Item

case class Book(desc: String, price: Double) extends Item

// 打包销售,打折
// item里的价格 - discount就是最后的价格
case class Bundle(desc: String, discount: Double, item: Item*) extends Item


// 书的金额: (((100+25)-50) + 20+30) - 10 = 115
val sale = Bundle("书籍", 10, Book("漫画", 20), Book("小说", 30),
  Bundle("文学作品", 50, Book("《围城》", 100), Book("《活着》", 25)))
```

然后看以下4个示例

```scala
// 1.拿到漫画
val res = sale match {
  case Bundle(_, _, Book(desc, _), _*) => desc
}
println(res) // 漫画



// 2.通过@语法将嵌套的值绑定到变量
val res2 = sale match {
  case Bundle(_, _, art@Book(_, _), rest@_*) => (art, rest)
}
println(res2) // (Book(漫画,20.0),WrappedArray(Book(小说,30.0), Bundle(文学作品,50.0,WrappedArray(Book(《围城》,100.0), Book(《活着》,25.0)))))



// 3.不使用_*绑定剩余item到rest，返回的就不是WrappedArray了,所以如果有多个元素就会匹配不上
val res3 = sale match {
  case Bundle(_, _, art@Book(_, _), _, rest) => (art, rest)
}
println(res3) // (Book(漫画,20.0),Bundle(文学作品,50.0,WrappedArray(Book(《围城》,100.0), Book(《活着》,25.0))))
println(res3._1) // Book(漫画,20.0)
println(res3._2) // Bundle(文学作品,50.0,WrappedArray(Book(《围城》,100.0), Book(《活着》,25.0)))



// 4.递归计算书的总价：
// 4.1.如果是Book直接返回
// 4.2.如果是Bundle，递归计算
def price(it: Item): Double = {
  it match {
    case Book(_, p) => p
    case Bundle(_, discount, its@_*) => its.map(price).sum - discount
  }
}
println("总价为：" + price(sale)) // 总价为：115.0
```

# sealed class（密封类）

被sealed声明的类，只能在当前源文件引用。



