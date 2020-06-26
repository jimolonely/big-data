
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



