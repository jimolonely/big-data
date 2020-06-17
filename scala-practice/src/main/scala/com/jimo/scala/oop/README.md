

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




