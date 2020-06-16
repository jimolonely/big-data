

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



