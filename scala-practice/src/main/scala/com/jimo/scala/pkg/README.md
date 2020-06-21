# 包的命名规则：
只能包含数字、字母、下划线、小圆点，且不能以数字开头，不能使用关键字

`com.公司.项目.业务模块`

# scala默认引入的包

* scala
* Predef
* java.lang
* ...

# 包使用细节

## package打包形式

以下都会将A类打在 `com.jimo.web`包下。

### 1.常规形式，和java一样

```scala
package com.jimo.web
class A{}
```

### 2.包分离

```scala
package com.jimo
package web

class A{}
```

### 3.包分离形式

```scala
package com.jimo{
  package web {
    class A{}
  }
}
```

## 包可以嵌套使用

如上面第三种情况

## 包嵌套的作用域

* 子包可以直接访问父包内容
* 使用优先级是就近原则，优先本包内，再是父包
* 父包要访问子包内容，需要引入

```scala
package com.jimo {


  object Test {

    import com.jimo.web.Web

    def main(args: Array[String]): Unit = {
      val web = new Web()
    }
  }

  package web {

    class Web {}

  }

}
```

## 包的绝对路径

用于包的冲突引入

```scala
class Web {
  @_root_.scala.beans.BeanProperty
  var url: String = _
}
```

# 包对象

由于虚拟机限制，包里不能写变量、函数，包对象就是为了弥补这个缺陷。

假如要在包 `web` 里声明一个函数，要么在其父包里声明一个包对象：

```scala
package com.jimo.scala.pkg {

  package object web {
    def search(): Unit = {
      println("search")
    }
  }
  package web {

    object Web {
      def main(args: Array[String]): Unit = {
        search()
      }
    }

  }

}
```
包对象的名称要和包名一样，本质实现，查看其class文件：

会在 `com.jimo.scala.pkg.web`包下生成一个package.class和package$.class:
```java
public final class package {
  public static void search() {
    package$.MODULE$.search();
  }
}

public final class package$ {
  public static final package$ MODULE$;
  
  public void search() {
    scala.Predef$.MODULE$.println("search");
  }
  
  private package$() {
    MODULE$ = this;
  }
}
```

注意事项：

* 每个包有且只有一个包对象
* 包对象用来增加包的功能，补充

# 包的可见性

对比java的四种访问权限

1. 属性：默认是public的
2. 方法：默认是public
3. private为私有权限，但在同类和伴生对象可用
    ```scala
    class A {
      private var a: Int = 18
    
      def say(): Unit = {
        println("hello")
      }
    }
    
    object A {
      def main(args: Array[String]): Unit = {
        val a = new A
        println(a.a)
        a.say()
      }
    }
    
    object B {
      def main(args: Array[String]): Unit = {
        val a = new A
        // 不能访问
        // println(a.a)
        a.say()
      }
    }
    ```
4. protected: 比java更严格，只能子类访问，同包不能访问
5. scala没有public关键字，因为默认就是public的

```scala
package com.jimo.scala.pkg

class A {
  private var a: Int = 18

  def say(): Unit = {
    println("hello")
  }

  // 增加包访问权限
  // 1.private依然存在，同类可访问
  // 2.同时扩大了权限，com.jimo包下的所有类都可以访问
  private[jimo] var b = "bbb"
}

object B {
  def main(args: Array[String]): Unit = {
    val a = new A
    // 不能访问
    // println(a.a)
    a.say()
    println(a.b)
  }
}
```

# 包的引入

1. 包的引入可以出现在任何地方，尽量保证作用域小
2. `_` 等价于 java 的 `*`
3. 引入选择器选择引入需要的类：
    ```scala
    import scala.collection.mutable.{HashMap, HashSet}
    ```
4. 引入同名的类可以重命名：
    ```scala
    import java.util.{HashMap => JavaHashMap}
    ```
5. 可以隐藏某个类：
    ```scala
    import java.util.{HashSet => _, _}
    ```
