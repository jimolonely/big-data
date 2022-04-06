# 股票处理案例

## 准备环境

### 定义实体类

股票价格的精简版

```java
@ToString
public class StockPrice {
    public StockPrice() {
    }

    public StockPrice(String name, long ts, double price, int volume) {
        this.name = name;
        this.ts = ts;
        this.price = price;
        this.volume = volume;
    }

    /**
     * 股票名称
     */
    public String name;
    /**
     * 时间戳
     */
    public long ts;
    public double price;
    /**
     * 交易量
     */
    public int volume;
}
```
### 定义数据源

股票数据的无限产生
```java
/**
 * 股票输入源，随机一些交易
 *
 * @author jimo
 **/
public class StockSource implements SourceFunction<StockPrice> {

    @Override
    public void run(SourceContext<StockPrice> sourceContext) throws Exception {
        Random r = new Random();
        while (true) {
            // 随机等待
            final int waitTime = r.nextInt(1000);
            if (waitTime < 200) {
                TimeUnit.MILLISECONDS.sleep(waitTime);
            }
            sourceContext.collect(new StockPrice("股票" + r.nextInt(10), System.currentTimeMillis(), r.nextDouble() * 100, r.nextInt(100)));
        }
    }

    @Override
    public void cancel() {

    }
}
```

### 程序框架

handle就是后面的业务逻辑

```java
final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

final DataStreamSource<StockPrice> source = env.addSource(new StockSource());

handle(source);

env.execute();
```

## 实时计算某只股票的价格最大值

```java
/**
 * 实时计算某只股票的最大价格
 */
private static void maxPrice(DataStreamSource<StockPrice> source) {
    String targetStockName = "股票5";
    final SingleOutputStreamOperator<StockPrice> maxPrice = source
            .filter(stockPrice -> stockPrice.name.equals(targetStockName))
            .keyBy(s -> s.name)
            // 计算5秒内的价格最大值
            .timeWindow(Time.seconds(5))
            .maxBy("price");
    maxPrice.print();
}
```
结果可以看到**每隔5秒**输出一条股票价格，是这5秒内的最大值：
```shell
2> StockPrice(name=股票5, ts=1649233638624, price=98.39036249209782, volume=46)
2> StockPrice(name=股票5, ts=1649233642814, price=95.11880588599936, volume=88)
2> StockPrice(name=股票5, ts=1649233645937, price=98.48801633623225, volume=36)
2> StockPrice(name=股票5, ts=1649233654534, price=95.84843460716735, volume=93)
2> StockPrice(name=股票5, ts=1649233655753, price=97.80297016767926, volume=64)
```


## 汇率转换

```java
/**
 * 价格汇率转换，假设*6转成美元
 */
private static void exchangeRateChange(DataStreamSource<StockPrice> source) {
    source.map(s -> {
        s.price *= 6;
        return s;
    }).print();
}
```
结果：
```shell
1> StockPrice(name=股票8, ts=1649234361306, price=197.5135913210384, volume=23)
2> StockPrice(name=股票3, ts=1649234361306, price=665.1784903297744, volume=61)
12> StockPrice(name=股票0, ts=1649234361296, price=231.34045958821943, volume=92)
8> StockPrice(name=股票4, ts=1649234361430, price=118.0472729499752, volume=13)
6> StockPrice(name=股票1, ts=1649234361430, price=377.85022764168264, volume=36)
9> StockPrice(name=股票9, ts=1649234361430, price=419.03921936948564, volume=28)
```

