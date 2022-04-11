

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


