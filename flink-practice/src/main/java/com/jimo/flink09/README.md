
# 窗口

[https://nightlies.apache.org/flink/flink-docs-master/zh/docs/dev/datastream/operators/windows/](https://nightlies.apache.org/flink/flink-docs-master/zh/docs/dev/datastream/operators/windows/)

窗口主要有两种，一种基于时间（Time-based Window），另一种基于数量（Count-based Window）。

Flink为我们提供了一些内置的WindowAssigner，即滚动窗口、滑动窗口和会话窗口.

## 滚动窗口

滚动窗口模式下，窗口之间不重叠，且窗口长度（Window Size）是固定的。
我们可以用TumblingEventTimeWindows和TumblingProcessingTimeWindows创建一个基于Event Time或ProcessingTime的滚动窗口。

```java
final DataStreamSource<StockPrice> source = env.addSource(new StockSource());
final WindowedStream<StockPrice, String, TimeWindow> tumblingWindowStream = source
        .keyBy(s -> s.name)
        .window(TumblingEventTimeWindows.of(Time.seconds(5)));

final WindowedStream<StockPrice, String, TimeWindow> window = source
        .keyBy(s -> s.name)
        .window(TumblingProcessingTimeWindows.of(Time.seconds(5), Time.seconds(10)));
```

timeWindow()是一种简略写法，它考虑了用户使用何种时间语义：使用timeWindow()时，看我们在执行环境设置了何种时间语义。

```java
env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
source.keyBy(s -> s.name).timeWindow(Time.seconds(5));
```

## 滑动窗口






