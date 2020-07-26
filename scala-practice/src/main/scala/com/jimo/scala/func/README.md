

# 作为参数的函数

```scala
def plus(x: Int): Int = x + 1

println(Array(1, 2, 3, 4).map(plus).mkString(",")) // 2,3,4,5
```

# 偏函数

给定一个列表 `List(1,2,3,"abc")`, 返回所有数字+1，且过滤掉非数字。

方法一：模式匹配，不够优美
```scala
val list = List(1, 2, 3, "abc")

// 模式匹配方式
val list1 = list.map {
  case x: Int => x + 1
  case _ =>
}
println(list1) // List(2, 3, 4, ())
```

偏函数定义：

* 对符合某个条件，而不是所有情况进行逻辑操作，使用偏函数
* 将包含在大括号内的一组case语句封装为函数，成为偏函数
* 偏函数是scala中的一个特质 PartialFunction
* map不支持偏函数，用collect

```scala
val addOne = new PartialFunction[Any, Int] {
  def isDefinedAt(any: Any): Boolean = if (any.isInstanceOf[Int]) true else false

  def apply(any: Any): Int = any.asInstanceOf[Int] + 1
}
// map不能调偏函数
val list2 = list.collect(addOne)
println(list2) // List(2, 3, 4)
```

偏函数的简化形式

```scala
def addOne2: PartialFunction[Any, Int] = {
  case i: Int => i + 1
}
println(list.collect(addOne2)) // List(2, 3, 4)

// 最终：
println(list.collect { case i: Int => i + 1 })
```

# 匿名函数

```scala
val triple = (x: Double) => 3 * x
println(triple(3.5)) // 10.5
println(triple) // <function1>
```

# 高阶函数

接收函数作为参数的函数。

```scala
// 以函数作为参数
def test(f: Double => Double, n: Double) = {
  f(n)
}

def sum(n: Double) = {
  n + n
}

println(test(sum, 10.6)) // 21.2
```

高阶函数可以返回函数类型：
```scala
// 返回函数类型
val minusXy = (x: Int) => (y: Int) => x - y
println(minusXy(100)) // <function1>
println(minusXy(100)(30)) // 70
```

这里使用到了闭包：内部函数访问了外部函数的变量

# 参数类型推断

* 可以省略能推断的参数类型
* 当只有单个参数时，可以省略括号
* 如果变量只在 `=>` 右边只出现一次，可以用 `_` 来代替

```scala
val list = List(1, 2, 3, 4)

// 正常写法
println(list.map((x: Int) => x + 1))
// 类型推断省略
println(list.map((x) => x + 1))
// 一个参数可替换
println(list.map(_ + 1))

// 求和
println(list.reduce(_ + _))
```

# 闭包

是一个函数与其相关引用环境（变量）组成的一个整体。

```scala
def makeSuffix(suffix: String) = {
  (name: String) => {
    if (name.endsWith(suffix)) {
      name
    } else {
      name + suffix
    }
  }
}

val jpg = makeSuffix(".jpg")
println(jpg("a.jpg")) // a.jpg
println(jpg("b")) // b.jpg
```

闭包的好处：

* 传统方法我们也可以实现，只是每次都会传递 suffix参数，有点冗余和不优美。

# 函数柯里化

1. 接受多个参数的函数都可转化为接受单个参数的函数，转换过程叫柯里化
2. 柯里化就是证明了函数只需要一个参数而已
3. 柯里化就是以函数为主体思想发展的必然结果

```scala
// 普通
def mul1(x: Int, y: Int) = x * y
println(mul1(10, 9))

// 闭包
def mul2(x: Int) = (y: Int) => x * y
println(mul2(10)(9))

// 柯里化
def mul3(x: Int)(y: Int) = x * y
println(mul3(10)(9))
```

最佳实践

比较2个字符串在忽略大小写时是否相等：

可以分为2步：

1. 全部大写或小写
2. 比较大小

```scala
// 普通方式
def eq(s1: String)(s2: String): Boolean = {
  s1.toLowerCase == s2.toLowerCase
}
println(eq("hello")("HellO"))

// 高级的方式
implicit class AddEqToString(s: String) {
  def myEq(ss: String)(f: (String, String) => Boolean): Boolean = {
    f(s.toLowerCase, ss.toLowerCase)
  }
}

def equal(s1: String, s2: String): Boolean = {
  s1.equals(s2)
}

println("hello".myEq("HellO")(equal))
```

# 抽象控制

就是满足下面条件的函数：

1. 参数是函数
2. 函数参数没有输入值也没有返回值

正常写法：
```scala
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
```

去掉括号，就变成了控制抽象,看起来就像传进去一个代码块

```scala
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
```

有什么用呢？可以模拟一个 do while语句

```scala
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
```

