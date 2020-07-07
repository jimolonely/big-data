
隐式转换

# 问题

```scala
val num: Int = 3.5 // 转换出错
```
如何使用隐式转换实现？

# 定义

是以 `implicit` 关键字声明的带有**单个参数**的函数，这种函数会**自动应用**，将值从一种类型转为另一种类型。

```scala
implicit def doubleToInt(d: Double): Int = {
  d.toInt
}

val num: Int = 3.5 // 转换ok
```
原理：
```java
  private final int doubleToInt$1(double d) {
    return (int)d;
  }
  
  public void main(String[] args) {
    int num = doubleToInt$1(3.5D);
  }
```

这个 `$1` 是啥意思呢？看下面代码：
```scala
    def f(): Unit = {
      def f(): Unit = {
        println("f")
      }
    }

    def f1(): Unit = {
      def f2(): Unit = {
        println("f2")
      }
    }
```
结果如下：
```java
  private final void f$2() {
    scala.Predef$.MODULE$.println("f");
  }
  private final void f$1() {}

  private final void f2$1() {
    scala.Predef$.MODULE$.println("f2");
  }
  private final void f1$1() {}
```
所以 `$1，$2`就是函数重名时追加的序号。

# 注意事项

1. 隐式函数与函数名无关，只和函数签名有关（参数和返回值）
2. 保证作用域下一个隐式函数

# 通过隐式转换丰富类功能

给Mysql类增加delete函数

```scala
object ImplicitDemo02 {
  def main(args: Array[String]): Unit = {
    implicit def addDelFunc(mysql: Mysql): DB = {
      new DB
    }

    val mysql = new Mysql
    mysql.insert()
    mysql.delete()
  }
}

class Mysql {
  def insert(): Unit = {
    println("insert")
  }
}

class DB {
  def delete(): Unit = {
    println("delete")
  }
}
```
编译后结果：
```java
  private final DB addDelFunc$1(Mysql mysql) {
    return new DB();
  }
  
  public void main(String[] args) {
    Mysql mysql = new Mysql();
    mysql.insert();
    addDelFunc$1(mysql).delete();
  }
```



