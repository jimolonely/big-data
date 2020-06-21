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



