# 对于Flink，我是认真的

## 如何设置Flink Web UI端口

1. 通过 `Configuration` 配置
2. 增肌 `flink-runtime-web`依赖

## max和maxBy有什么区别？

maxBy()与max()的区别在于，
maxBy()同时保留其他字段的数值，即maxBy()返回数据流中最大的整个元素，包括其他字段。

比如：下面按照第1个元素分组，求第3个元素的最大值。
```java
DataStreamSource<Tuple3<Integer, Integer, Integer>> source =
        env.fromElements(
                Tuple3.of(0, 8, 0), Tuple3.of(0, 6, 1), 
                Tuple3.of(1, 4, 3), Tuple3.of(1, 5, 2));

SingleOutputStreamOperator<Tuple3<Integer, Integer, Integer>> max = 
        source.keyBy(t -> t.f0)
        .max(2);
```
max的结果是只关心最大值，中间元素只取了遇到的第一个值，后面就不变了。
```shell
6> (0,8,0)
6> (0,8,1)
6> (1,4,3)
6> (1,4,3)
```
改成 `maxBy`后的结果如下：
```java
6> (0,8,0)
6> (0,6,1)
6> (1,4,3)
6> (1,4,3)
```
取的是一条完整的记录。

## union和connect有什么区别？

1. connect()只能连接两个数据流，union()可以连接多个数据流。
2. connect()所连接的两个数据流的数据类型可以不一致，union()所连接的两个或多个数据流的数据类型必须一致。
3. 两个DataStream经过connect()之后被转化为ConnectedStreams，ConnectedStreams会对两个流的数据应用不同的处理方法，且两个流之间可以共享状态。



