
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

# 隐式值

也叫隐式变量，将某个形参标记为implicit，那么编译器会在方法省略隐式参数时自动调用。

```scala
  def main(args: Array[String]): Unit = {
    implicit val str: String = "jimo"

    def hello(implicit name: String): Unit = {
      println(s"hello,$name")
    }

    hello("hehe")
    hello
  }
```
编译后结果：

```java
  public void main(String[] args) {
    String str = "jimo";
    hello$1("hehe");
    hello$1(str);
  }
  
  private final void hello$1(String name) {
    (new String[2])[0] = "hello,";
    (new String[2])[1] = "";
    scala.Predef$.MODULE$.println((new StringContext((Seq)scala.Predef$.MODULE$.wrapRefArray((Object[])new String[2]))).s((Seq)scala.Predef$.MODULE$.genericWrapArray(new Object[] { name })));
  }
```

测试：

```scala
    implicit val str: String = "jimo"
    def hi(implicit name: String = "lily"): Unit = {
      println(s"hi, $name")
    }
    hi // 输出jimo
```
隐式值优先

# 隐式类

使用implicit修饰的声明类，在集合中会发挥重要作用。

1. 隐式类构造参数只能有一个
2. 隐式类不能是顶级的，必须定义在类、伴生对象或包对象中
3. 不能是case class

```scala
  def main(args: Array[String]): Unit = {
    implicit class MysqlToDB(mysql: Mysql) {
      def close(): Unit = {
        println("close")
      }
    }

    val mysql = new Mysql
    mysql.insert()
    mysql.close()
  }
```
编译：
```java
  private final ImplicitDemo044$MysqlToDB$2 MysqlToDB$1(Mysql mysql) {
    return new ImplicitDemo044$MysqlToDB$2(mysql);
  }
  
  public void main(String[] args) {
    Mysql mysql = new Mysql();
    mysql.insert();
    MysqlToDB$1(mysql).close();
  }
```

# 隐式转换的时机

1. 当方法参数与目标类型不一致时
2. 当对象调用所在类中不存在方法或成员时，会根据类型去寻找隐式转换

编译器如何查找？

1. 在当前代码作用域查找隐式实体（隐式方法、类、对象）
2. 找不到再继续在隐式参数的类型的作用域查找，包括与该类型相关联的全部伴生模块（尽量避免）
    1. T with A with B with C, 那么A，B，C都会查找
    2. 如果T是参数化类型，比如List[String],那么List的伴生对象和String的也会被搜索
    3. 如果T是一个单例类型p.T，那么p也会被搜索
    4. 如果T是个类型注入S#T，那么S和T都被搜索




