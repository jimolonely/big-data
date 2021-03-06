
# scala集合基本介绍

1. scala同时支持可变和不可变集合，不可变集合可安全的并发访问
2. 2个主要的包：
    * 不可变：scala.collection.immutable
    * 可变：scala.collection.mutable
3. scala默认采用不可变集合，对于**几乎**所有集合类，scala都提供了2个版本

不可变：集合本身内存地址不可变

# 集合的继承关系图

注意scala和java集合体系的不同。

```shell script
                                          +----------+
                             +------------+ Iterable +-------------+
                             |            +-----+----+             |
                             |                  |                  |
                         +---v---+              |             +----v--+
    +-------------+------+  Set  +---------+    |        +----+  Map  +--+----------+
    |             |      +-------+         |    |        |    +-------+  |          |
+---v-----+   +---v-----+  +------+   +----v--+ |      +-v-----+  +------v--+   +---v---+
| HashSet |   |SortedSet|  |BitSet|   |ListSet| |      |HashMap|  |SortedMap|   |ListMap|
+---------+   +---+-----+  +------+   +-------+ |      +-------+  +----+----+   +-------+
                  |                             |                      |
              +---v----+                        |                +-----v--+
              |TreeSet |                        |                | TreeMap|
              +--------+                     +--v+               +--------+
                             +---------------+Seq+------------------+
                             |               +---+                  |
                       +-----v-----+                           +----v----+
                       |IndexedSeq |                           |LinearSeq|
    +--------------+---------------+                    +----------+-----+
    |              |                                    |          |
 +--v---+        +-v---+                              +-v---+   +--v--+
 |Vector|        |Array|                              | List|   |Queue|
 +------+        +-----+                              +-----+   +-----+
```

# 定长数组

```scala
    // 方式1
    val ints = new Array[Int](4)
    println(ints.length) // 4
    ints(3) = 9
    for (i <- ints) {
      println(i)
    }

    // 方式2
    // Array[Int]
    val arr01 = Array(1, 2)
    arr01.foreach(println(_))
    // Array[Any]
    val arr02 = Array(1, 3, "hello")
    arr02.foreach(println)
```

# 变长数组

```scala
    val arr01 = ArrayBuffer[Int](4, 5, 6, 1)
    arr01.append(90, 88)
    arr01.append(1)
    arr01(1) = 10
    arr01.foreach(println)
    println("==============")
    val arr02 = ArrayBuffer[Any](1, 2.3, "he")
    arr02.foreach(println)

    // 定长与变长互转
    val arr03 = arr01.toArray
    val arr04 = arr03.toBuffer
    arr04.foreach(println)
```

# 多维数组

```scala
    val arr = Array.ofDim[Int](3, 4)
    arr(1)(1) = 3
    for (a1 <- arr) {
      for (elem <- a1) {
        print(elem + ",")
      }
      println()
```
看下底层：
```java
  public void main(String[] args) {
    int[][] arr = (int[][])scala.Array$.MODULE$.ofDim(3, 4, scala.reflect.ClassTag$.MODULE$.Int());
    arr[1][1] = 3;
    scala.Predef$.MODULE$.refArrayOps((Object[])arr).foreach((Function1)new ColDemo04$$anonfun$main$1());
  }
```

# scala数组与java数组互换

### scala转java
```scala
    val arr = ArrayBuffer("1", "2", "3")
    import scala.collection.JavaConversions.bufferAsJavaList

    val builder = new ProcessBuilder(arr)
    val list = builder.command()
    println(list) // [1, 2, 3]
```
注意这个bufferAsJavaList，实际上是个隐式转换：
```scala
  implicit def bufferAsJavaList[A](b: mutable.Buffer[A]): ju.List[A] = b match {
    case JListWrapper(wrapped) => wrapped
    case _ => new MutableBufferWrapper(b)
  }
```

### java转scala

```scala
    import scala.collection.JavaConversions.asScalaBuffer
    import scala.collection.mutable

    val scalaArr: mutable.Buffer[String] = list
    println(scalaArr) // ArrayBuffer(1, 2, 3)
```

# 元组

最多22个

```scala
case class Tuple22[+T1, +T2, +T3, +T4, +T5, +T6, +T7, +T8, +T9, +T10, +T11, +T12, +T13, +T14, +T15, +T16, +T17, +T18, +T19, +T20, +T21, +T22](_1: T1, _2: T2, _3: T3, _4: T4, _5: T5, _6: T6, _7: T7, _8: T8, _9: T9, _10: T10, _11: T11, _12: T12, _13: T13, _14: T14, _15: T15, _16: T16, _17: T17, _18: T18, _19: T19, _20: T20, _21: T21, _22: T22)
  extends Product22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22]
{
  override def toString() = "(" + _1 + "," + _2 + "," + _3 + "," + _4 + "," + _5 + "," + _6 + "," + _7 + "," + _8 + "," + _9 + "," + _10 + "," + _11 +
    "," + _12 + "," + _13 + "," + _14 + "," + _15 + "," + _16 + "," + _17 + "," + _18 + "," + _19 + "," + _20 + "," + _21 + "," + _22 + ")"
  
}
```
例子：
```scala
    val t4 = (1, 2, 3, "hehe")
    println(t4, t4._1, t4._4) // ((1,2,3,hehe),1,hehe)
```
编译后：
```java
  public void main(String[] args) {
    Tuple4 t4 = new Tuple4(BoxesRunTime.boxToInteger(1), BoxesRunTime.boxToInteger(2), BoxesRunTime.boxToInteger(3), "hehe");
    scala.Predef$.MODULE$.println(new Tuple3(t4, t4._1(), t4._4()));
  }
```
更多遍历方式：
```scala
    println(t4.productElement(3))

    for (elem <- t4.productIterator) {
      println(elem)
    }
```

# List

scala的List就是个object，是可变的。

```scala
  def main(args: Array[String]): Unit = {
    val list1 = List(1, 2, "AA")
    println(list1) // List(1,2,AA)

    // 空集合
    val list2 = Nil
    println(list2) // List()
  }
```
注意空列表也是List
```scala
case object Nil extends List[Nothing]{}
```
取值：
```scala
println(list1(2)) // AA
```
追加元素：
```scala
    // 追加:返回新的集合/列表，与Java不一样
    // 右边追加
    val list3 = list1 :+ 4
    println(list3) // List(1, 2, AA, 4)
    // 左边追加
    val list4 = 33 +: list1
    println(list4) // List(33, 1, 2, AA)
```

### 追加元素的符号 ::

```scala
    val list5 = List(3, 4, "jimo")
    val list6 = 4 :: 5 :: list5 :: 3 :: Nil
    // 从右往左，从后往前塞
    println(list6) // List(4, 5, List(3, 4, jimo), 3)
    val list7 = 4 :: 5 :: list5 ::: 3 :: Nil
    // 解构
    println(list7) // List(4, 5, 3, 4, jimo, 3)
```

# 数组练习

### 1.生成随机数数组

```scala
  def mkArr(n: Int): Array[Int] = {
    val a = new Array[Int](n)
    val random = new Random()
    for (i <- a) yield random.nextInt(n)
  }
```

### 2.交换相邻的2个元素

```scala
输入
5 9 3 10 1 1 2 0 0 7 8 
输出
9 5 10 3 1 1 0 2 7 0 8 
```
主要是util的使用
```scala
  def revert(arr: Array[Int]): Unit = {
    for (i <- 0 until(arr.length - 1, 2)) {
      val t = arr(i)
      arr(i) = arr(i + 1)
      arr(i + 1) = t
    }
  }
```

# 队列

插入数据
```scala
    val q = new mutable.Queue[Int]
    q += 1
    q += 1
    q ++= List(2, 3, 4)
    println(q) // Queue(1, 1, 2, 3, 4)

    val q2 = new mutable.Queue[Any]()
    q2 += 10
    q2 += List(1, 2, 3)
    println(q2) // Queue(10, List(1, 2, 3))
```
`+=`的特殊运算符，本质上还是 `append`
```scala
def +=(elem: A): this.type = { appendElem(elem); this }
```

删除数据：出队入队
```scala
    q.dequeue()
    println(q) // Queue(1, 2, 3, 4)
    q.enqueue(6, 7, 8)
    println(q) // Queue(1, 2, 3, 4, 6, 7, 8)
```
返回队列的头和尾
```scala
    println(q.head, q) // (1,Queue(1, 2, 3, 4, 6, 7, 8))
    println(q.last, q) // (8,Queue(1, 2, 3, 4, 6, 7, 8))
    // 返回除第一个以外的后元素
    println(q.tail) // Queue(2, 3, 4, 6, 7, 8)
    println(q.tail.tail) // Queue(3, 4, 6, 7, 8)
```

# Map

scala中 不可变Map是有序的，可变map是无序的

```scala
    val m1 = new mutable.HashMap[String, Int]()
    m1.put("k1", 10)
    m1.put("k2", 20)
    m1.put("k3", 30)
    println(m1) // Map(k2 -> 20, k1 -> 10, k3 -> 30)
    println(m1.get("k2")) // Some(20)

    // 不可变
    val m2 = immutable.Map("a" -> 11, "b" -> 22, "c" -> 33)
    println(m2) // Map(a -> 11, b -> 22, c -> 33)
    println(m2("b"))
    for (e <- m2) {
      print(s"key=${e._1},value=${e._2},")
    } // key=a,value=11,key=b,value=22,key=c,value=33,
```

```scala
    // 二元组的方式构建
    val m3 = mutable.Map(("name", "jimo"), ("age", 18))
    println(m3)
```
取数抛异常：
```scala
    println(m3.get("height"))
    println(m3("height"))
```
get方法不会
```scala
None
Exception in thread "main" java.util.NoSuchElementException: key not found: height
	at scala.collection.MapLike$class.default(MapLike.scala:228)
	at scala.collection.AbstractMap.default(Map.scala:59)
	at scala.collection.mutable.HashMap.apply(HashMap.scala:65)
	at com.jimo.scala.col.ColDemo10$.main(ColDemo10.scala:27)
	at com.jimo.scala.col.ColDemo10.main(ColDemo10.scala)
```

增加删除元素：
```scala
    // 添加元素
    val m4 = mutable.Map((0, 2))
    m4 += (1 -> 2)
    m4 += (2 -> 3)
    println(m4) // Map(2 -> 3, 1 -> 2, 0 -> 2)

    // 添加多个元素
    m4 += (3 -> 4, 5 -> 6)
    println(m4) // Map(2 -> 3, 5 -> 6, 1 -> 2, 3 -> 4, 0 -> 2)

    // 删除元素
    m4 -= (1, 2, 100)
    println(m4) // Map(5 -> 6, 3 -> 4, 0 -> 2)
```

遍历：
```scala
    for ((k, v) <- m4) {
      println(k, v)
    }
    for (elem <- m4.values) {
      println(elem)
    }
    for (elem <- m4.keys) {
      println(elem)
    }
    for (t <- m4) {
      println(t._1, t._2)
    }
```

# Set

创建
```scala
    // 创建
    val s1 = new mutable.HashSet[Int]()
    s1.add(1)
    s1.add(2)
    s1.add(2)
    s1.add(1)
    println(s1)

    val s2 = mutable.Set(1, 2, 3, 4, 3, 2, 3)
    println(s2) // Set(1, 2, 3, 4)
    // 默认不可变
    val s3 = Set(1, 2, 3, 4, 3, 2, 3)
    println(s3) // Set(1, 2, 3, 4)
```
添加
```scala
    // 添加元素
    s2 += 4
    s2 += 5
    println(s2) // Set(1, 5, 2, 3, 4)
    // 返回新的
    val s4 = s2.+(6)
    println(s4) // Set(1, 5, 2, 6, 3, 4)
```
删除
```scala
    // 删除
    s2 -= 4
    s2.remove(3)
    println(s2) // Set(1, 5, 2)
```
遍历
```scala
    // 遍历
    for (elem <- s2) {
      println(elem)
    }
```
集合的交并差
```scala
    // 交集
    println(s1 & s2) // Set(1, 2)
    // 差集
    println(s1 &~ s2) // Set()
    // 并集
    println(s1 ++ s2) // Set(1, 5, 2)
```

# 高阶函数

参数为函数

```scala
  def main(args: Array[String]): Unit = {
    println(test(twoFold, 2.0))
  }

  /**
   * 以函数作为参数的函数
   */
  def test(f: Double => Double, d: Double): Double = {
    f(d)
  }

  def twoFold(d: Double): Double = {
    2 * d
  }
```

## map

将高阶函数应用于集合，函数式编程

```scala
    val l1 = List(1, 2, 3)
    println(l1.map(_ * 2)) // List(2, 4, 6)

    val names = List("Hehe", "Jimo", "Lily")
    print(names.map(_.toUpperCase())) // List(HEHE, JIMO, LILY)
```

## flatmap

集合中的每个元素的子元素映射到某个函数并返回新的集合。

```scala
    val names = List("Hehe", "Jimo", "Lily")
    // List(H, E, H, E, J, I, M, O, L, I, L, Y)
    println(names.flatMap(_.toUpperCase()))
```

## filter

将符合要求的元素放到新集合

过滤以J开始的元素
```scala
    val names = List("Hehe", "Jimo", "Lily")
    println(names.filter(_.startsWith("J"))) // List(Jimo)
```

## reduce

简化版折叠

```scala
    val nums = List(1, 2, 3, 4, 5, 6, 7)
    println(nums.reduce(_ + _)) // 28
    println(nums.sum) // 28
    /**
     * 1-2=-1
     * -1-3=-4
     * ...
     * -19-7=-26
     */
    println(nums.reduceLeft(_ - _)) // -26
    /**
     * 6-7=-1
     * 5-(-1)=6
     * 4-6=-2
     * 3-(-2)=5
     * 2-5=-3
     * 1-(-3)=4
     */
    println(nums.reduceRight(_ - _)) // 4
```

## fold

折叠: 相比于reduce，多了一个初始值

```scala
    val nums = List(1, 2, 3, 4, 5, 6, 7)
    println(nums.fold(1)(_ + _)) // 29
    println(nums.foldLeft(1)(_ + _)) // 29
    println(nums.foldRight(1)(_ + _)) // 29
    println(nums.foldLeft(28)(_ - _)) // 0
    println(nums.foldRight(4)(_ - _)) // 0
```
缩写方式：
```scala
    // foldLeft
    println((1 /: nums) (_ + _)) // 29
    println((28 /: nums) (_ - _)) // 0
    // foldRight
    println((nums :\ 1) (_ + _)) // 29
    println((nums :\ 4) (_ - _)) // 0
```

## scan

扫描：对集合的每个元素做fold操作，但会把中间结果放到一个集合中保存

```scala
    println(nums.scanLeft(1)(_ + _)) // List(1, 2, 4, 7, 11, 16, 22, 29)
    println(nums.scanLeft(28)(_ - _)) // List(28, 27, 25, 22, 18, 13, 7, 0)
    println(nums.scanRight(1)(_ + _)) // List(29, 28, 26, 23, 19, 14, 8, 1)
    println(nums.scanRight(4)(_ - _)) // List(0, 1, 1, 2, 2, 3, 3, 4)
```

## 练习

1. 使用foldLeft把字符放进array里
```scala
    val s = "AAAAAAAAAAAAAABBBBBBBBBBSDDDDDDDDDD"
    val buf = ArrayBuffer[Char]()

    println(s.foldLeft(buf)((b, c) => {
      b.append(c);
      b
    }))
    println(buf)
    // ArrayBuffer(A, A, A, A, A, A, A, A, A, A, A, A, A, A, B, B, B, B, B, B, B, B, B, B, S, D, D, D, D, D, D, D, D, D, D)
```

2. 统计字符出现的次数

```scala
    // 2. Map(A -> 14, B -> 10, S -> 1, D -> 10)
    println(s.foldLeft(Map[Char, Int]())((map, c) => map + (c -> (map.getOrElse(c, 0) + 1))))
```

## zip
拉链（合并）：如果元素不匹配会丢失，按照最短长度

```scala
    val list1 = List(1, 2, 3, 8)
    val list2 = List(4, 5, 6)
    println(list1.zip(list2)) // List((1,4), (2,5), (3,6))
```

## 迭代器

```scala
    val list3 = List(2, 3, 4, 5)
    val it = list3.iterator
    while (it.hasNext) {
      println(it.next())
    }
```

## stream

```scala
    def numAdd(n: BigInt): Stream[BigInt] = n #:: numAdd(n + 1)

    val stream1 = numAdd(1)
    println(stream1) // Stream(1, ?)
    // 我希望再取一个数据
    println(stream1.tail) // Stream(2, ?)
    println(stream1) // Stream(1, 2, ?)
    println(stream1.head) // 1
    println(stream1.take(2)) // Stream(1, ?)
```

## view

可用于懒加载

```scala
    val viewSeq = (1 to 10).view.map(_ * 2).filter(_ % 2 == 0)
    println(viewSeq) // SeqViewMF(...)
    // 需要时才出结果
    viewSeq.foreach(println)
```

# 并行集合

1. 充分利用多核CPU
2. 主要用到的算法：分治；负载均衡算法(work stealin)

```scala
    // 常规：串行
    (1 to 5).foreach(println)
    // 并行: 随机顺序
    (1 to 5).par.foreach(println)
```

再看个例子：我电脑有12个核,所以并行时全利用上了
```scala
    val thread1 = (1 to 100).map(_ => Thread.currentThread().getName).distinct
    println(thread1) // Vector(main)
    val thread2 = (1 to 100).par.map { _ => Thread.currentThread().getName }.distinct
    println(thread2) // ParVector(ForkJoinPool-1-worker-29, ForkJoinPool-1-worker-13, ForkJoinPool-1-worker-3, ForkJoinPool-1-worker-7, ForkJoinPool-1-worker-25, ForkJoinPool-1-worker-21, ForkJoinPool-1-worker-11, ForkJoinPool-1-worker-9, ForkJoinPool-1-worker-23, ForkJoinPool-1-worker-31, ForkJoinPool-1-worker-17, ForkJoinPool-1-worker-27)
```

# 操作符扩展

使用关键字做变量名
```scala
    val `var`: Int = 100
    println(`var`)
```
后置操作符
```scala
    // 前置操作符
    val n1 = 2
    val n2 = 3
    println(n1.+(n2)) // 等价于 n1 + n2

    val p = new Person
    p.+(10)
    println(p.age) // 10
    p + 10
    println(p.age) // 20

  class Person {
    var age: Int = 0

    def +(n: Int): Unit = {
      age += n
    }
  }
```
前置操作符
```scala
    val op2 = new Op2
    !op2 // !!!!

class Op2 {
  // unary:一元运算符
  def unary_!(): Unit = println("!!!!")
}
```

# 练习

交换对偶泛型
```scala
val pair = new Pair[String, Int]("jimo", 18)
println(pair) // s=18,t=jimo
val pair2 = pair.swap()
println(pair2) // s=jimo,t=18

final class Pair[T, S](t: T, s: S) {

  def swap(): Pair[S, T] = new Pair(s, t)

  override def toString: String = (s"s=${s},t=${t}")
}
```
只有泛型类型一致才交换：
```scala
val p = new Pair[String, String]("jimo", "hehe")
val p2 = p.swapSame
println(p2) // s=hehe,t=jimo

// S =:= T 表示 S和T的类型要一致
def swapSame(implicit env: S =:= T) = new Pair(t, s)
```




