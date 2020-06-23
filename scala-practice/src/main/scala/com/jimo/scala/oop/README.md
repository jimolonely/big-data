

scala面向对象总结。

1. Java是面向对象语言，但存在着非面向对象内容：基本类型、null，静态方法等；
2. scala是天生面向对象语言，一切皆对象

语法总结：

1. scala类默认是public的，不必声明
2. 一个源文件可以有多个类的声明

# 类的修饰符

查看简单示例：
```scala 
object Demo01 {
  def main(args: Array[String]): Unit = {
    var p1 = new Person
    p1.name = "jimo"
    p1.age = 18
    println(p1.name, p1.age)
  }
}

class Person {
  var name: String = _
  var age: Int = 0
}
```
查看编译后的class文件：

```java
public class Person {
  private String name;
  
  public String name() {
    return this.name;
  }
  
  public void name_$eq(String x$1) {
    this.name = x$1;
  }
  
  private int age = 0;
  
  public int age() {
    return this.age;
  }
  
  public void age_$eq(int x$1) {
    this.age = x$1;
  }
}
```
也就是说，scala已经生成了get、set方法：

1. get方法和属性名一样
2. set方法为：属性_$eq()
3. 并且是public的


# 创建对象

对象的内存分配

方法调用机制

## 构造器

Java里构造器重点就是通过方法表示；可以重载；有一个默认无参构造，自己声明了就没有了。

scala构造器分类：主构造器，辅助构造器（形参个数不同）

主构造器：
```scala
class 类名(形参列表) { // 主构造器 
  def this(形参列表1){} // 辅助构造器
  def this(形参列表2){} // 辅助构造器
}
```

实例：
```scala
  def main(args: Array[String]): Unit = {
    val jimo = new Student("jimo", 18)
    jimo.hello()
  }

class Student(name: String, ageIn: Int) {

  var age: Int = ageIn
  println("hello 1")

  def hello(): Unit = {
    println("hello,I am " + name + ",age = " + age)
  }

  println("hello 2")

  age += 10
}
// hello 1
// hello 2
// hello,I am jimo,age = 28
```
scala类里可以写任何语句，这是和java不同的地方，那原理是什么呢？
通过编译后的class文件可以看出：
```java
public class Student {
  private final String name;
  
  private int age;
  
  public Student(String name, int ageIn) {
    this.age = ageIn;
    Predef$.MODULE$.println("hello 1");
    Predef$.MODULE$.println("hello 2");
    age_$eq(age() + 10);
  }
  
  public int age() {
    return this.age;
  }
  
  public void age_$eq(int x$1) {
    this.age = x$1;
  }
  
  public void hello() {
    Predef$.MODULE$.println((new StringBuilder()).append("hello,I am ").append(this.name).append(",age = ").append(BoxesRunTime.boxToInteger(age())).toString());
  }
}
```
这些语句实际上是放在构造方法里执行的。

### 构造器的重载

辅构造器必须调用主构造器(间接调用也可以)
```scala
class Student(name: String, ageIn: Int) {

  def this(name: String) {
    this(name, 0)
  }

  def this(age: Int) {
    this(null, age)
  }

  def this() {
    this("jimo")
  }
}
```
### 私有化主构造器

用于不让使用者new 一个实例
```scala
class Teacher private() {
  def this(name: String) {
    this()
  }
}
```

### 属性

先看以下声明：

```scala
class P1(name: String) {
  println(name)
}

class P2(val name: String) {
  println(name)
}

class P3(var name: String) {
  println(name)
}
```
生成的不同在于：是否会变成成员变量;
val和var变量代表是否可读
```java
public class P1 {
  public P1(String name) {
    Predef$.MODULE$.println(name);
  }
}

public class P2 {
  private final String name;
  
  public String name() {
    return this.name;
  }
  
  public P2(String name) {
    Predef$.MODULE$.println(name);
  }
}

public class P3 {
  private String name;
  
  public String name() {
    return this.name;
  }
  
  public void name_$eq(String x$1) {
    this.name = x$1;
  }
  
  public P3(String name) {
    Predef$.MODULE$.println(name());
  }
}
```

### Bean属性

由于javaBeans规范了getX()和setX()等规范方法，所以很多框架采用(mybatis)，由于需要反射，所以
需要应用规范。

```scala
class P4 {
  var name: String = _
  @BeanProperty
  var age: Int = 0
}

val p = new P4()
p.setAge(19)
println(p.getAge)
```

### 对象创建流程

1. 加载类信息（属性、方法）
2. 在堆中给对象开辟空间
3. 调用主构造器对属性进行初始化
4. 使用辅助构造器进行初始化

```scala
class P5 {
  var name: String = "hehe"

  def this(name: String) {
    this()
    println(this.name)
    this.name = name
    println(this.name)
  }
}

new P5("jimo")
输出：
hehe
jimo
```

# 继承

scala里重写父类非抽象方法必须加 `override` 修饰。

### 构造器的调用

```scala
class J {
  var name: String = _
  println("J...")
}

class JI extends J {
  def this(name: String) {
    this
    this.name = name
    println("JI...")
  }
}

new JI()
println("=========")
new JI("jimo")
```
结果：
```shell
J...
=========
J...
JI...
```

**scala里：子类的主类和辅助构造器不能直接调用父类的构造器；**
只有通过类声明时直接调用。

```scala
class K(name: String) {
  println("K")
}

class KI(name: String) extends K(name = name) {
  println("KI")

  def this() {
    this(null)
    println("KI:this")
  }
}

new KI()
println("=========")
new KI("jimo")
```

结果：
```shell script
K
KI
KI:this
=========
K
KI
```

# 类型检查与转换

classOf[T]

```scala
    println(classOf[String])
    println(classOf[P])
    val s = "s"
    println(s.getClass.getName)
```
isInstanceOf
```scala
    val s = "s"
    println(s.isInstanceOf[String])
```
asInstanceOf
```scala
class P {
}

class PI extends P {
}

val pi = new PI
val p: P = pi.asInstanceOf[P]
println(p.isInstanceOf[P], p.isInstanceOf[PI])
```

输出：
```scala
class java.lang.String
class com.jimo.scala.oop.P
java.lang.String
true
(true,true) // p既是子类，也是父类
```

# 字段隐藏与覆写

### java的字段隐藏

java中没有字段的覆写，只有方法的覆写。

但是可以模拟覆写的效果，原因是隐藏机制。

```java
public class JavaOverrideDemo {
    public static void main(String[] args) {
        Sub sub = new Sub();
        System.out.println(sub.s);

        Super sub2 = new Sub();
        System.out.println(sub2.s);

        System.out.println(((Super) sub).s);
    }
}

class Super {
    String s = "super";
}

class Sub extends Super {
    String s = "sub";
}
/*
sub
super
super
*/
```

### scala字段覆写

```scala
object ScalaOverrideDemo {
  def main(args: Array[String]): Unit = {
    val a: AA = new BB
    val b: BB = new BB
    println("a.age=" + a.age)
    println("b.age=" + b.age)
  }
}

class AA {
  val age: Int = 10
}

class BB extends AA {
  override val age: Int = 20
}
/*
a.age=20
b.age=20
*/
```

原理很简单，看看生成的class文件：
```java
public class AA {
  private final int age = 10;
  
  public int age() {
    return this.age;
  }
}

public class BB extends AA {
  private final int age = 20;
  
  public int age() {
    return this.age;
  }
}
```
也就是java的方法重写。

**val字段只可以重写父类的val字段，或者 同名的无参方法**

> 为啥不能重写 `var` 修饰的字段？
> 因为，如果重写父类的var变量，那么就会生成 `field_$eq`的设值方法，
> 也就是说子类会继承这个方法，常量拥有set方法显然不行。

```scala
object ScalaOverrideDemo {
  def main(args: Array[String]): Unit = {
    val a: AA = new BB
    val b: BB = new BB
    println("a.name=" + b.name)
    println("b.name=" + b.name)
  }
}

class AA {
  def name(): String = {
    "AA"
  }
}

class BB extends AA {
  override val name: String = "BB"
}
/*
a.name=BB
b.name=BB
*/
```

那什么时候可以重写 `var` 修饰的字段呢？

在抽象类中的字段：

```scala
abstract class CC {
  var height: Int
}

class DD extends CC {
  override var height: Int = 20
}
```
原理很简单，字段在抽象类里压根就不生成，只有抽象方法：
```java
public abstract class CC {
  public abstract int height();
  
  public abstract void height_$eq(int paramInt);
}

public class DD extends CC {
  private int height = 20;
  
  public int height() {
    return this.height;
  }
  
  public void height_$eq(int x$1) {
    this.height = x$1;
  }
}
```

# 抽象类

1. abstract关键字修饰class
2. 抽象方法不能用abstract关键字修饰
3. 方法也可实现，字段也可初始化

# scala类层级关系

1. 所有类都是AnyRef的子类，类似java的Object
2. AnyVal和AnyRef都是Any的子类，Any是根节点
3. Any中定义了isInstanceOd, asInstanceOf以及hash
4. Null类型的唯一实例就是null对象，可以将null赋给任何引用，但不能赋给值类型的变量
5. Nothing类没有实例，对于泛型有用处，例如：空列表Nil和类型是List[Nothing],他是List[T]的子类型

```scala
    var n: Long = null
    println(n)
```
```shell script
Error:(5, 19) an expression of type Null is ineligible for implicit conversion
    var n: Long = null
Error:(5, 19) type mismatch;
 found   : Null(null)
 required: Long
    var n: Long = null
```




