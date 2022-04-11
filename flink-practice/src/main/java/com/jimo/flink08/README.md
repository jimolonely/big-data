
# 强大的Process Function

了解一下ProcessFunction系列函数。它们是Flink中最底层的API，提供了对数据流进行更细粒度操作的权限。

如果想获取数据流中Watermark的时间戳，或者使用定时器，需要使用ProcessFunction系列函数。
Flink SQL是基于这些函数实现的，一些需要高度个性化的业务场景也需要使用这些函数。

包括

* KeyedProcessFunction
* ProcessFunction
* CoProcessFunction
* KeyedCoProcessFunction
* ProcessJoinFunction
* ProcessWindowFunction等多种函数

# Flink中的Timer定时器

以一个例子说明：在一段时间内监控股票价格增加，然后通知。

```java
public static void main(String[] args) throws Exception {
    final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

    final DataStreamSource<StockPrice> source = env.addSource(new StockSource());

    final SingleOutputStreamOperator<String> infoStream = source
        .keyBy(s -> s.name)
        .process(new StockIncreaseInfoFunction(2000));

    infoStream.print();

    env.execute();
}

/**
 * 监控持续涨价的股票并发出通知
 */
private static class StockIncreaseInfoFunction extends KeyedProcessFunction<String, StockPrice, String> {

    /**
     * 统计时间间隔
     */
    private final long intervalMills;
    private ValueState<Double> lastPrice;
    private ValueState<Long> currentTimer;

    public StockIncreaseInfoFunction(long intervalMills) {
        this.intervalMills = intervalMills;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        lastPrice = getRuntimeContext().getState(new ValueStateDescriptor<>("lastPrice", Types.DOUBLE));
        currentTimer = getRuntimeContext().getState(new ValueStateDescriptor<>("timer", Types.LONG));
    }

    @Override
    public void processElement(StockPrice stock, Context ctx, Collector<String> out) throws Exception {
        if (lastPrice.value() == null) {
            // 第一次不做处理
        } else {
            final Double prevPrice = lastPrice.value();
            long currTimerTimestamp = currentTimer.value() == null ? 0 : currentTimer.value();

            if (stock.price < prevPrice) {
                // 如果流入的股票价格降低了，那么删除该Timer，否则保留Timer
                ctx.timerService().deleteProcessingTimeTimer(currTimerTimestamp);
                currentTimer.clear();
            } else if (stock.price >= prevPrice && currTimerTimestamp == 0) {
                // 如果新流入的股票价格升高，且还没有Timer,那么注册一个Timer
                // Timer的时间 = 当前时间+时间间隔
                long timerTs = (ctx.timestamp() == null ? System.currentTimeMillis() : ctx.timestamp()) + intervalMills;
                ctx.timerService().registerProcessingTimeTimer(timerTs);
                currentTimer.update(timerTs);
            }
        }
        lastPrice.update(stock.price);
    }

    @Override
    public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> out) throws Exception {
        // 当定时器触发时调用,这里发起通知
        out.collect(String.format("您的股票%s在%sms时间内持续上涨，请关注异动！", ctx.getCurrentKey(), intervalMills));
        currentTimer.clear();
    }
}
```
结果输出（调整了数据输入都是增加的价格）
```shell
2> 您的股票股票5在2000ms时间内持续上涨，请关注异动！
6> 您的股票股票0在2000ms时间内持续上涨，请关注异动！
4> 您的股票股票4在2000ms时间内持续上涨，请关注异动！
4> 您的股票股票1在2000ms时间内持续上涨，请关注异动！
6> 您的股票股票3在2000ms时间内持续上涨，请关注异动！
1> 您的股票股票8在2000ms时间内持续上涨，请关注异动！
1> 您的股票股票9在2000ms时间内持续上涨，请关注异动！
```

# 旁路输出

[https://nightlies.apache.org/flink/flink-docs-master/zh/docs/dev/datastream/side_output/](https://nightlies.apache.org/flink/flink-docs-master/zh/docs/dev/datastream/side_output/)

ProcessFunction的另一大特色功能是可以将一部分数据发送到另外一个数据流中，而且输出的两个数据流数据类型可以不一样。这个功能被称为旁路输出（SideOutput）。

下面旁路输出Tag的类型定义为`String`,输出高交易量的数据。

```java
public static void main(String[] args) throws Exception {
    final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

    // 这需要是一个匿名的内部类，以便我们分析类型
    OutputTag<String> highVolumeOutput = new OutputTag<String>("high-volume-tag") {
    };

    final DataStreamSource<StockPrice> source = env.addSource(new StockSource());
    final DataStream<String> sideOutput = source.keyBy(s -> s.name)
            .process(new HighVolumeOutputFunction(highVolumeOutput))
            .getSideOutput(highVolumeOutput);

    sideOutput.print();

    env.execute();
}

private static class HighVolumeOutputFunction extends KeyedProcessFunction<String, StockPrice, String> {

    private OutputTag<String> highVolumeOutput;

    public HighVolumeOutputFunction(OutputTag<String> highVolumeOutput) {
        this.highVolumeOutput = highVolumeOutput;
    }

    @Override
    public void processElement(StockPrice stock, Context ctx, Collector<String> out) throws Exception {
        if (stock.volume > 80) {
            ctx.output(highVolumeOutput, "高交易量：" + stock.volume);
        } else {
            out.collect("普通交易量");
        }
    }
}
```




