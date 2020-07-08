
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

