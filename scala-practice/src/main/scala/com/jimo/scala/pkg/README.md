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



