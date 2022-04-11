

# 窗口函数

增量和全量计算。

* reduce
* aggregate
* process

## reduce function

求窗口内股票交易量之和。

```java
env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);
DataStreamSource<StockPrice> source = env.addSource(new StockSource());
SingleOutputStreamOperator<StockPrice> sum = source
        .keyBy(s -> s.name)
        .timeWindow(Time.seconds(5))
        .reduce(new ReduceFunction<StockPrice>() {
            @Override
            public StockPrice reduce(StockPrice s1, StockPrice s2) throws Exception {
                return new StockPrice(s1.name, s2.ts, s2.price, s1.volume + s2.volume);
            }
        });
sum.print();
```

## aggregate function

AggregateFunction的输入类型是IN，输出类型是OUT，中间状态数据类型是ACC.

求股票在窗口内的价格平均值。

```java
env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);
DataStreamSource<StockPrice> source = env.addSource(new StockSource());
SingleOutputStreamOperator<Tuple2<String, Double>> avgPrice = source
        .keyBy(s -> s.name)
        .timeWindow(Time.seconds(5))
        .aggregate(new AggregateFunction<StockPrice, Tuple3<String, Double, Integer>, Tuple2<String, Double>>() {
            @Override
            public Tuple3<String, Double, Integer> createAccumulator() {
                return Tuple3.of("", 0d, 0);
            }

            @Override
            public Tuple3<String, Double, Integer> add(StockPrice s, Tuple3<String, Double, Integer> accumulator) {
                return Tuple3.of(s.name, s.price + accumulator.f1, s.volume + accumulator.f2);
            }

            @Override
            public Tuple2<String, Double> getResult(Tuple3<String, Double, Integer> accumulator) {
                return Tuple2.of(accumulator.f0, accumulator.f1 / accumulator.f2);
            }

            @Override
            public Tuple3<String, Double, Integer> merge(Tuple3<String, Double, Integer> a, Tuple3<String, Double, Integer> b) {
                return Tuple3.of(a.f0, a.f1 + b.f1, a.f2 + b.f2);
            }
        });
avgPrice.print();
```
结果
```shell
1> (股票8,0.9450305188612753)
8> (股票6,0.8989819541338013)
4> (股票1,1.0515781528678894)
1> (股票9,1.0078920707456536)
2> (股票5,0.8959581608210693)
6> (股票7,0.9117596726796451)
4> (股票4,0.9505608207370495)
8> (股票2,1.0075298190750013)
6> (股票0,1.1671916538787392)
6> (股票3,1.5375886706284398)
```

## process window function

ProcessWindowFunction要缓存窗口内的全量数据.

```java
class ProcessWindowFunction<IN, OUT, KEY, W extends Window>{}
```
统计交易量频次最高的交易数量.
```java
env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);
DataStreamSource<StockPrice> source = env.addSource(new StockSource());
SingleOutputStreamOperator<Tuple2<String, Integer>> maxVolume = source
        .keyBy(s -> s.name)
        .timeWindow(Time.seconds(5))
        .process(new ProcessWindowFunction<StockPrice, Tuple2<String, Integer>, String, TimeWindow>() {
            @Override
            public void process(String s, Context context, Iterable<StockPrice> elements, Collector<Tuple2<String, Integer>> out) throws Exception {
                // 统计交易量频次最高的交易数量
                Map<Integer, Integer> volumeCount = new HashMap<>(8);
                StockPrice max = null;
                int maxCount = 0;
                for (StockPrice item : elements) {
                    volumeCount.put(item.volume, volumeCount.getOrDefault(item.volume, 0) + 1);
                    if (maxCount < volumeCount.get(item.volume)) {
                        max = item;
                        maxCount = volumeCount.get(item.volume);
                    }
                }
                if (max != null) {
                    out.collect(Tuple2.of(max.name, max.volume));
                }
            }
        });
maxVolume.print();
```
结果
```shell
8> (股票6,53)
4> (股票1,58)
6> (股票7,20)
8> (股票2,51)
1> (股票9,5)
6> (股票0,5)
4> (股票4,33)
1> (股票8,72)
6> (股票3,22)
2> (股票5,8)
```

## 增量聚合的 ProcessWindowFunction

将Reduce的输出交给ProcessWindowFunction作为输入，下面求解窗口内最大价格和窗口开始结束时间。

```java
env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);
DataStreamSource<StockPrice> source = env.addSource(new StockSource());
SingleOutputStreamOperator<Tuple3<Double, Long, Long>> maxPriceWithTime = source
        .keyBy(s -> s.name)
        .timeWindow(Time.seconds(5))
        .reduce(new ReduceFunction<StockPrice>() {
            @Override
            public StockPrice reduce(StockPrice s1, StockPrice s2) throws Exception {
                return s1.price > s2.price ? s1 : s2;
            }
        }, new ProcessWindowFunction<StockPrice, Tuple3<Double, Long, Long>, String, TimeWindow>() {

            @Override
            public void process(String s, Context context, Iterable<StockPrice> elements, Collector<Tuple3<Double, Long, Long>> out) throws Exception {
                final StockPrice max = elements.iterator().next();
                out.collect(Tuple3.of(max.price, context.window().getStart(), context.window().getEnd()));
            }
        });
maxPriceWithTime.print();
```
结果
```shell
1> (75.64519184990137,1649681725000,1649681730000)
2> (90.33051546140071,1649681725000,1649681730000)
1> (98.65336240929017,1649681725000,1649681730000)
6> (95.58319525432499,1649681725000,1649681730000)
4> (99.78737156929158,1649681725000,1649681730000)
8> (98.52193552747589,1649681725000,1649681730000)
4> (92.49721104781192,1649681725000,1649681730000)
6> (90.58396180462613,1649681725000,1649681730000)
8> (82.74862231487495,1649681725000,1649681730000)
6> (96.45225463450456,1649681725000,1649681730000)
```

## Trigger

触发器（Trigger）决定了何时启动窗口处理函数来处理窗口中的数据，以及何时将窗口内的数据清除。

Trigger 接口提供了五个方法来响应不同的事件：

* onElement() 方法在每个元素被加入窗口时调用。
* onEventTime() 方法在注册的 event-time timer 触发时调用。
* onProcessingTime() 方法在注册的 processing-time timer 触发时调用。
* onMerge() 方法与有状态的 trigger 相关。该方法会在两个窗口合并时， 将窗口对应 trigger 的状态进行合并，比如使用会话窗口时。
* 最后，clear() 方法处理在对应窗口被移除时所需的逻辑。

当满足某个条件，Trigger会返回一个TriggerResult类型的结果，TriggerResult是一个枚举类型，它有下面几种情况。

* CONTINUE：什么都不做。
* FIRE：启动计算并将结果发送给下游算子，不清除窗口数据。
* PURGE：清除窗口数据但不执行计算。
* FIRE_AND_PURGE：启动计算，发送结果然后清除窗口数据。

**在自定义Trigger时，如果使用了状态，一定要使用clear()方法将状态数据清除，否则随着窗口越来越多，状态数据会越积越多。**

## Evictor

清除器（Evictor）是在WindowAssigner和Trigger的基础上的一个可选选项，用来清除一些数据。

evictBefore()和evictAfter()分别在窗口处理函数之前和之后被调用.

清除逻辑主要针对全量计算，对于增量计算的ReduceFunction和AggregateFunction，我们没必要使用Evictor。

* CountEvictor
* TimeEvictor


