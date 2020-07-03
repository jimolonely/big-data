
# 单例对象

### Java版

Java中的单例模式：饿汉式，懒汉式。

优化之后的版本：

```java
public class Singleton {

    private Singleton() {
    }

    private static class SingletonInstance {
        private static final Singleton INSTANCE = new Singleton();
    }

    public static Singleton getInstance() {
        return SingletonInstance.INSTANCE;
    }
}
```

### scala版本

scala直接用object就是单例的

```scala
  def main(args: Array[String]): Unit = {
    println(SSingleton.name)
    println(SSingleton.hello())
  }

object SSingleton {
  var name: String = "hello"

  def hello(): Unit = {}
}
```
但底层如何实现呢？虚拟机里没有object的，编译查看class文件：

```java
public final class SSingleton {
  public static void hello() {
    SSingleton$.MODULE$.hello();
  }
  
  public static void name_$eq(String paramString) {
    SSingleton$.MODULE$.name_$eq(paramString);
  }
  
  public static String name() {
    return SSingleton$.MODULE$.name();
  }
}

public final class SSingleton$ {
  public static final SSingleton$ MODULE$;
  
  private String name;
  
  public String name() {
    return this.name;
  }
  
  public void name_$eq(String x$1) {
    this.name = x$1;
  }
  
  public void hello() {}
  
  private SSingleton$() {
    MODULE$ = this;
    this.name = "hello";
  }
}
```

# 特质与接口

scala里没有接口，使用特质来代替接口的概念。

多个类具有相同的特征，就可以抽象出来。

trait可看作对继承的一种补充

## trait的使用

* 没有父类：class 类名 extends 特质1 with 特质2 with ...
* 有父类：class 类名 extends 父类 with 特质1 with 特质2 with ...

```scala
trait Move {
  def fly(): Unit
}

abstract class Plane {}

class Airliner extends Plane with Move {
  override def fly(): Unit = {
    println("客机使用翅膀飞行")
  }
}

class Helicopter extends Move {
  override def fly(): Unit = {
    println("直升机使用螺旋桨飞行")
  }
}
```

查看编译后的class文件：可以看到变成了接口
```java
public interface Move {
  void fly();
}

public class Helicopter implements Move {
  public void fly() {
    Predef$.MODULE$.println(");
  }
}

public class Airliner extends Plane implements Move {
  public void fly() {
    Predef$.MODULE$.println(");
  }
}
```

## trait里实现方法

trait既可以定义抽象方法，也可以实现默认方法。

```scala
trait TA {
  def hello(): Unit = {
    println("TA hello()")
  }

  def go(): Unit
}

class TAI extends TA {
  override def go(): Unit = {
    println("TAI go()")
  }
}
```

看看怎么实现的：

```java
public interface TA {
  void hello();
  
  void go();
}
public abstract class TA$class {
  public static void $init$(TA $this) {}
  
  public static void hello(TA $this) {
    Predef$.MODULE$.println("TA hello()");
  }
}

public class TAI implements TA {
  public void hello() {
    TA$class.hello(this);
  }
  
  public TAI() {
    TA$class.$init$(this);
  }
  
  public void go() {
    Predef$.MODULE$.println("TAI go()");
  }
}
```

可以看出同时生成了一个接口，还有一个抽象类，抽象类负责实现默认方法，
然后被调用。

## java中的接口都可以当作trait使用

经过上面本质的分析，trait就是接口和抽象类的组合，所以当然可以使用java的接口。

```scala
trait Serializable extends Any with java.io.Serializable
```

## trait的动态混入（mixin）

* 不继承特质，但又想使用特质的功能：可以在创建对象时(抽象类也可以)混入特质

```scala
  def main(args: Array[String]): Unit = {
    val b = new MysqlDB with TB
    b.insert(1)
  }

trait TB {
  def insert(id: Int): Unit = {
    println("插入数据=" + id)
  }
}

class MysqlDB {}
```
看看如何实现：
```java
  public void main(String[] args) {
    MysqlDB b = new TraitDemo03$$anon$1();
    ((TB)b).insert(1);
  }
  
  public final class TraitDemo03$$anon$1 extends MysqlDB implements TB {
    public void insert(int id) {
      TB$class.insert(this, id);
    }
    
    public TraitDemo03$$anon$1() {
      TB$class.$init$(this);
    }
  }
```
看来是生成了一个匿名类。

* 动态混入的优点：可以在不影响原始类的情况下加功能，耦合性很低

```scala
new BB with TB {
  override def haha(): Unit = {}
}

trait TB {
  def insert(id: Int): Unit = {
    println("插入数据=" + id)
  }
}

abstract class BB {
  def haha(): Unit
}
```

## 叠加特质

看一个多层次的继承

```scala
class Mysql {}

trait Op {
  println("Op...")

  def insert(id: Int): Unit
}

trait Data extends Op {
  println("Data...")

  override def insert(id: Int): Unit = {
    println("插入数据+" + id)
  }
}

trait DB extends Data {
  println("DB...")

  override def insert(id: Int): Unit = {
    println("向数据库 ")
    super.insert(id)
  }
}

trait File extends Data {
  println("File...")

  override def insert(id: Int): Unit = {
    println("向文件 ")
    super.insert(id)
  }
}
```

然后看看调用：

```scala
  def main(args: Array[String]): Unit = {
    val mysql = new Mysql with DB with File
//    mysql.insert(1)
  }
```
先来看看构建顺序：

* 构建顺序与声明的顺序一样（从左至右）

看看结果：

```scala 
Op...
Data...
DB...
File...
```

接下来看看执行顺序：

```scala
mysql.insert(1)
```

执行顺序：

* 刚好是相反的，只要查找到一个父类的方法就执行

```scala
向文件 
向数据库 
插入数据+1
```

这里我就很好奇，为什么 insert方法只执行了一遍？

* 解释：scala特质中如果使用了super，并不表示调用方法，而是向前面（左边）继续查找特质，如果找不到，才会去父特质查找

看一下class：
```java
  public void main(String[] args) {
    Mysql mysql = new TraitDemo04$$anon$1();
    ((File)mysql).insert(1);
  }
  
  public final class TraitDemo04$$anon$1 extends Mysql implements DB, File {
    public void insert(int id) {
      File$class.insert(this, id);
    }
    
    public TraitDemo04$$anon$1() {
      Op$class.$init$(this);
      Data$class.$init$(this);
      DB$class.$init$(this);
      File$class.$init$(this);
    }
  }

public abstract class File$class {

  public static void insert(File $this, int id) {
    Predef$.MODULE$.println("向文件 ");
    $this.com$jimo$scala$oop2$File$$super$insert(id);
  }
}
```

其实class也没有透露更多细节，发现mysql实际上是最右边的特质File的实例，当File的insert
执行到 `super.insert`时，会继续找到 DB的insert执行，再执行 `super.insert` 发现左边已经没有
特质了，所以执行父特质 `Data` 的insert方法。 

### 指定父类执行

叠加特质时可以指定调用父类的特质方法

```scala
trait File extends Data {
  println("File...")

  override def insert(id: Int): Unit = {
    println("向文件 ")
    super[Data].insert(id)
    //    super.insert(id)
  }
}
```

这时候发现结果就只剩2句话了：
```scala
向文件 
插入数据+1
```

### 特质的抽象方法实现

注意abstract, 不加会报错

```scala
trait Op {
  println("Op...")

  def insert(id: Int): Unit
}
trait File2 extends Op {
  abstract override def insert(id: Int): Unit = {
    println("File2...")
    super.insert(id)
  }
}
```

### 富接口

特质中既有抽象方法，又有已经实现的方法。

### trait中字段的继承

```scala
object TraitDemo05 {
  def main(args: Array[String]): Unit = {
    val mysql = new Mysql5 with Op5 {
      override var opType: String = "mysql-insert"
    }
    println(mysql.opType)
    val mysql1 = new Mysql5 with DB5
    println(mysql1.opType)
  }
}

trait Op5 {
  var opType: String
}

trait DB5 extends Op5 {
  override var opType: String = "db-insert"
}

class Mysql5 {}
```
实际上是作为了类的一个属性。
```java
 public void main(String[] args) {
    Mysql5 mysql = new TraitDemo05$$anon$2();
    scala.Predef$.MODULE$.println(((Op5)mysql).opType());
    Mysql5 mysql1 = new TraitDemo05$$anon$1();
    scala.Predef$.MODULE$.println(((DB5)mysql1).opType());
  }
  
  public final class TraitDemo05$$anon$2 extends Mysql5 implements Op5 {
    private String opType = "mysql-insert";
    
    public String opType() {
      return this.opType;
    }
    
    public void opType_$eq(String x$1) {
      this.opType = x$1;
    }
  }
  
  public final class TraitDemo05$$anon$1 extends Mysql5 implements DB5 {
    private String opType;
    
    public String opType() {
      return this.opType;
    }
    
    @TraitSetter
    public void opType_$eq(String x$1) {
      this.opType = x$1;
    }
    
    public TraitDemo05$$anon$1() {
      DB5$class.$init$(this);
    }
  }
  
  private TraitDemo05$() {
    MODULE$ = this;
  }
}
```

# 总结

创建对象的方式有几种

* new
* apply
* 动态方式
* 匿名子类

说说apply的方式：通过伴生对象实现,为什么要伴生对象呢？

因为，伴生对象可以访问类的所有属性，包括私有的。

```scala
  def main(args: Array[String]): Unit = {
    val a = AAA.apply()
    println(a)
  }

class AAA {}

object AAA {
  def apply(): AAA = new AAA()
}
```

