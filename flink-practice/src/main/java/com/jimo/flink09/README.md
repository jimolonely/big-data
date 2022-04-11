
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

滑动窗口以一个步长（Slide）不断向前滑动，窗口的Size固定。
Slide小于Size时，相邻窗口会重叠，一个元素会被分配到多个窗口；Slide大于Size时，有些元素可能被丢掉。

```java
final DataStreamSource<StockPrice> source = env.addSource(new StockSource());
final WindowedStream<StockPrice, String, TimeWindow> window = source
        .keyBy(s -> s.name)
        .window(SlidingEventTimeWindows.of(Time.seconds(10), Time.seconds(5)));

final WindowedStream<StockPrice, String, TimeWindow> window2 = source
        .keyBy(s -> s.name)
        .window(SlidingProcessingTimeWindows.of(Time.seconds(10), Time.seconds(5)));
```

## 会话窗口

会话窗口模式下，两个窗口之间有一个间隙，称为Session Gap。
当一个窗口在大于Session Gap的时间内没有接收到新数据时，窗口将关闭。
在这种模式下，窗口的Size是可变的，每个窗口的开始和结束时间并不是确定的。
我们可以设置定长的Session Gap，也可以使用SessionWindowTimeGapExtractor动态地确定Session Gap的值。

```java
final DataStreamSource<StockPrice> source = env.addSource(new StockSource());
final WindowedStream<StockPrice, String, TimeWindow> fixGapWindow = source
        .keyBy(s -> s.name)
        .window(EventTimeSessionWindows.withGap(Time.seconds(30)));

WindowedStream<StockPrice, String, TimeWindow> dynamicGapWindow = source
        .keyBy(s -> s.name)
        .window(EventTimeSessionWindows.withDynamicGap(new SessionWindowTimeGapExtractor<StockPrice>() {
            @Override
            public long extract(StockPrice element) {
                return element.volume;
            }
        }));
```

## 全局窗口

全局窗口的 assigner 将拥有相同 key 的所有数据分发到一个全局窗口。 
这样的窗口模式仅在你指定了自定义的 trigger 时有用。 
否则，计算不会发生，因为全局窗口没有天然的终点去触发其中积累的数据。

```java
final DataStreamSource<StockPrice> source = env.addSource(new StockSource());
source.keyBy(s -> s.name).window(GlobalWindows.create());
```




