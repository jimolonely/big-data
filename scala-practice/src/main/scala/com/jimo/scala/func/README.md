

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


