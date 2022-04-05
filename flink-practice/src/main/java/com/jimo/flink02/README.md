# 常见算子转换操作

* map
* filter
* flatMap: map和filter的一般形式
* keyBy: 分组操作，返回 `KeyedStream`
* 聚合： `sum, max, min, maxBy, minBy`
* reduce: keyBy后更常见的收缩操作
* 数据联合：union, connect

## 测试下connect

```java
StreamExecutionEnvironment env =
        StreamExecutionEnvironment.getExecutionEnvironment();

DataStreamSource<Integer> intStream = env.fromElements(1, 2, 3, 4, 5);
DataStreamSource<String> strStream = env.fromElements("a", "b", "c");
ConnectedStreams<Integer, String> connectedStreams = intStream.connect(strStream);

SingleOutputStreamOperator<String> map = 
        connectedStreams.map(new CoMapFunction<Integer, String, String>() {
    @Override
    public String map1(Integer value) throws Exception {
        return value + "";
    }

    @Override
    public String map2(String value) throws Exception {
        return value;
    }
});

map.print();
env.execute();
```
我们可以看到如下结果：

```shell
6> 2
8> 4
4> b
5> 1
7> 3
1> 5
3> a
5> c
```
看起来挺乱的，那是因为默认使用了CPU核数的并发：
```java
StreamExecutionEnvironment.createLocalEnvironment(1)
```
当我们改为1个并发时会得到下面的结果: 顺序交叉执行，就好理解多了。
```shell
1
a
2
b
3
c
4
5
```

