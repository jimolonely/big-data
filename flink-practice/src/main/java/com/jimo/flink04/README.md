# Flink的数据类型

* Types
* TypeTransformation

# 检查Flink使用什么序列化

```java
public static void main(String[] args) {
    System.out.println(
            TypeInformation.of(Integer.class)
                    .createSerializer(new ExecutionConfig())
    );

    System.out.println(
            TypeInformation.of(A.class)
                    .createSerializer(new ExecutionConfig())
    );
}

private static class A {
    private A() {
    }
}
```
结果：A使用了Kryo序列化，因为有私有构造，不是简单的POJO类。
```shell
org.apache.flink.api.common.typeutils.base.IntSerializer@69222c14
org.apache.flink.api.java.typeutils.runtime.kryo.KryoSerializer@ed9d4d
```

# 其他序列化器

* Avro
* Kryo
* Thrift
* Protobuf




