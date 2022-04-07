
# 3种时间语义

设置时间语义：

```java
env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
```

时间语义枚举类型：
```java
public enum TimeCharacteristic {

    /**
     * Processing time for operators means that the operator uses the system clock of the machine to
     * determine the current time of the data stream. Processing-time windows trigger based on
     * wall-clock time and include whatever elements happen to have arrived at the operator at that
     * point in time.
     *
     * <p>Using processing time for window operations results in general in quite non-deterministic
     * results, because the contents of the windows depends on the speed in which elements arrive.
     * It is, however, the cheapest method of forming windows and the method that introduces the
     * least latency.
     */
    ProcessingTime,

    /**
     * Ingestion time means that the time of each individual element in the stream is determined
     * when the element enters the Flink streaming data flow. Operations like windows group the
     * elements based on that time, meaning that processing speed within the streaming dataflow does
     * not affect windowing, but only the speed at which sources receive elements.
     *
     * <p>Ingestion time is often a good compromise between processing time and event time. It does
     * not need any special manual form of watermark generation, and events are typically not too
     * much out-or-order when they arrive at operators; in fact, out-of-orderness can only be
     * introduced by streaming shuffles or split/join/union operations. The fact that elements are
     * not very much out-of-order means that the latency increase is moderate, compared to event
     * time.
     */
    IngestionTime,

    /**
     * Event time means that the time of each individual element in the stream (also called event)
     * is determined by the event's individual custom timestamp. These timestamps either exist in
     * the elements from before they entered the Flink streaming dataflow, or are user-assigned at
     * the sources. The big implication of this is that it allows for elements to arrive in the
     * sources and in all operators out of order, meaning that elements with earlier timestamps may
     * arrive after elements with later timestamps.
     *
     * <p>Operators that window or order data with respect to event time must buffer data until they
     * can be sure that all timestamps for a certain time interval have been received. This is
     * handled by the so called "time watermarks".
     *
     * <p>Operations based on event time are very predictable - the result of windowing operations
     * is typically identical no matter when the window is executed and how fast the streams
     * operate. At the same time, the buffering and tracking of event time is also costlier than
     * operating with processing time, and typically also introduces more latency. The amount of
     * extra cost depends mostly on how much out of order the elements arrive, i.e., how long the
     * time span between the arrival of early and late elements is. With respect to the "time
     * watermarks", this means that the cost typically depends on how early or late the watermarks
     * can be generated for their timestamp.
     *
     * <p>In relation to {@link #IngestionTime}, the event time is similar, but refers the the
     * event's original time, rather than the time assigned at the data source. Practically, that
     * means that event time has generally more meaning, but also that it takes longer to determine
     * that all elements for a certain time have arrived.
     */
    EventTime
}
```

## Event Time和Watermark

Event Time指的是数据流中每个元素或者每个事件自带的时间属性，一般是事件发生的时间。

Watermark意味着在一个时间窗口下，Flink会等待一个有限的时间，这在一定程度上降低了计算结果的绝对准确性，而且增加了系统的延迟。

## Processing Time

Processing Time指使用算子的当前节点的操作系统时间, Processing Time在时间窗口下的计算会有不确定性。
Processing Time只依赖当前节点的操作系统时间，不需要依赖Watermark，无须缓存.

## Ingestion Time

Ingestion Time是事件到达Flink Source的时间。

一个事件在整个处理过程从头至尾都使用这个时间，而且下游算子不受上游算子处理速度的影响，计算结果相对准确一些，但计算成本比Processing Time稍高。

# Event Time详解

首先，读一下那段注释：

> Event time means that the time of each individual element in the stream (also called event) is determined by the event's individual custom timestamp. These timestamps either exist in the elements from before they entered the Flink streaming dataflow, or are user-assigned at the sources. The big implication of this is that it allows for elements to arrive in the sources and in all operators out of order, meaning that elements with earlier timestamps may arrive after elements with later timestamps.
Operators that window or order data with respect to event time must buffer data until they can be sure that all timestamps for a certain time interval have been received. This is handled by the so called "time watermarks".
Operations based on event time are very predictable - the result of windowing operations is typically identical no matter when the window is executed and how fast the streams operate. At the same time, the buffering and tracking of event time is also costlier than operating with processing time, and typically also introduces more latency. The amount of extra cost depends mostly on how much out of order the elements arrive, i.e., how long the time span between the arrival of early and late elements is. With respect to the "time watermarks", this means that the cost typically depends on how early or late the watermarks can be generated for their timestamp.
In relation to IngestionTime, the event time is similar, but refers the the event's original time, rather than the time assigned at the data source. Practically, that means that event time has generally more meaning, but also that it takes longer to determine that all elements for a certain

明白了之后，就可以开始使用了。

如果我们要使用Event Time语义，以下两项配置缺一不可：

1. 使用一个时间戳为数据流中每个事件的Event Time赋值； 
2. 生成Watermark。

使用Event Time时间语义，需要设置EventTime和Watermark，具体有2种方法：

### 1. 在Source阶段指定

如下，继承 `RichSourceFunction`, 通过 `SourceContext`设置.

```java
private static class MyEventData {
    public double data;
    public long eventTime;
    public boolean hasWatermark;
    public long watermarkTime;
}

private static class MyEventDataSource extends RichSourceFunction<MyEventData> {

    @Override
    public void run(SourceContext<MyEventData> ctx) throws Exception {
        while (true) {
            MyEventData data = getNext();
            // 设置事件时间
            ctx.collectWithTimestamp(data, data.eventTime);

            // 设置Watermark
            if (data.hasWatermark) {
                ctx.emitWatermark(new Watermark(data.watermarkTime));
            }
        }
    }

    private MyEventData getNext() {
        return new MyEventData();
    }

    @Override
    public void cancel() {
    }
}
```

### 2.在流上设置

我们不改 `Source`的情况也可以修改：

核心在于 `WatermarkStrategy`,下面手动实现了一个周期性的水印策略。

```java
public static void main(String[] args) {
    final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

    final DataStreamSource<MyEventData> stream = env.addSource(new MyEventDataSource());

    env.getConfig().setAutoWatermarkInterval(5000L);
    stream.assignTimestampsAndWatermarks(
            WatermarkStrategy
                    // 水印设置
                    .forGenerator(s -> new MyPeriodicGenerator())
                    // event time设置
                    .withTimestampAssigner((SerializableTimestampAssigner<MyEventData>) (myEventData, l) -> myEventData.eventTime)
    );
}

private static class MyPeriodicGenerator implements WatermarkGenerator<MyEventData> {

    private long currentMaxTimestamp;

    @Override
    public void onEvent(MyEventData event, long eventTimestamp, WatermarkOutput output) {
        // 每个元素进来后都会调用这个方法
        currentMaxTimestamp = Math.max(currentMaxTimestamp, eventTimestamp);
    }

    @Override
    public void onPeriodicEmit(WatermarkOutput output) {
        // 固定周期调用,设置水印比最大值慢1分钟
        output.emitWatermark(new org.apache.flink.api.common.eventtime.Watermark(currentMaxTimestamp - 60 * 1000));
    }
}
```

当然，也可以根据数据去生成水印，就和上面数据源一样的操作了：

```java
private static class MyDataGenerator implements WatermarkGenerator<MyEventData> {

    @Override
    public void onEvent(MyEventData event, long eventTimestamp, WatermarkOutput output) {
        if (event.hasWatermark) {
            output.emitWatermark(new org.apache.flink.api.common.eventtime.Watermark(event.watermarkTime));
        }
    }

    @Override
    public void onPeriodicEmit(WatermarkOutput output) {
        // 这里什么都不做
    }
}
```










