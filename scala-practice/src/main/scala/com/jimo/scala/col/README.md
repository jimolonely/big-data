
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


