
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

# 作为参数的函数

```scala
def plus(x: Int): Int = x + 1

println(Array(1, 2, 3, 4).map(plus).mkString(",")) // 2,3,4,5
```

