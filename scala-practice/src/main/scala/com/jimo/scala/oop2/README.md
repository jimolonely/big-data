
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


