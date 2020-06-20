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



