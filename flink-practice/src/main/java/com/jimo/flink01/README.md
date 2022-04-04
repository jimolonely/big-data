# Hello World也不容易

首先我引入依赖：
```java
        <dependency>
            <groupId>org.apache.flink</groupId>
            <artifactId>flink-streaming-java_2.12</artifactId>
            <version>${flink-version}</version>
            <scope>provided</scope>
        </dependency>
```
然后随手写了个`HelloWorld`: 

* 关键点在于手里没有数据源，就手写个迭代器不断产生数据

```java
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // source
        DataStreamSource<String> stream = env.addSource(
                new FromIteratorFunction<>(new MyStringIterator()));

        // transform
        SingleOutputStreamOperator<Tuple2<String, Integer>> sum = stream.flatMap((String line, Collector<Tuple2<String, Integer>> collector) -> {
            String[] words = line.split("\\s+");
            for (String word : words) {
                collector.collect(new Tuple2<>(word, 1));
            }
        })
        .returns(Types.TUPLE(Types.STRING, Types.INT))
        .keyBy(k -> k.f0)
        .timeWindow(Time.seconds(5))
        .sum(1);

        // sink
        sum.print();

        // execute
        env.execute("hello world flink");
    }

    private static class MyStringIterator implements Iterator<String> {

        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public String next() {
            String WORDS = "dfhajw567dsasfgadjh";
            Random r = new Random();
            try {
                TimeUnit.SECONDS.sleep(r.nextInt(5));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            StringBuilder buf = new StringBuilder();
            for (int i = 0; i < r.nextInt(WORDS.length() - 1); i++) {
                buf.append(WORDS.charAt(r.nextInt(WORDS.length() - 1))).append(" ");
            }
            return buf.toString();
        }
    }
```

## 需要能够序列化

写了个迭代器，运行报错：

```java
Exception in thread "main" org.apache.flink.api.common.InvalidProgramException: com.jimo.flink01.Main$MyStringIterator@629f0666 is not serializable. The object probably contains or references non serializable fields.
	at org.apache.flink.api.java.ClosureCleaner.clean(ClosureCleaner.java:164)
	at org.apache.flink.api.java.ClosureCleaner.clean(ClosureCleaner.java:132)
	at org.apache.flink.api.java.ClosureCleaner.clean(ClosureCleaner.java:69)
	at org.apache.flink.streaming.api.environment.StreamExecutionEnvironment.clean(StreamExecutionEnvironment.java:1863)
	at org.apache.flink.streaming.api.environment.StreamExecutionEnvironment.addSource(StreamExecutionEnvironment.java:1573)
	at org.apache.flink.streaming.api.environment.StreamExecutionEnvironment.addSource(StreamExecutionEnvironment.java:1536)
	at org.apache.flink.streaming.api.environment.StreamExecutionEnvironment.addSource(StreamExecutionEnvironment.java:1522)
	at com.jimo.flink01.Main.main(Main.java:20)
Caused by: java.io.NotSerializableException: com.jimo.flink01.Main$MyStringIterator
	at java.io.ObjectOutputStream.writeObject0(ObjectOutputStream.java:1184)
	at java.io.ObjectOutputStream.writeObject(ObjectOutputStream.java:348)
	at org.apache.flink.util.InstantiationUtil.serializeObject(InstantiationUtil.java:624)
	at org.apache.flink.api.java.ClosureCleaner.clean(ClosureCleaner.java:143)
```

无法序列化嘛，我懂，加个接口。

```java
private static class MyStringIterator 
        implements Iterator<String>, Serializable {...}
```

## 还要return

```java
Exception in thread "main" org.apache.flink.api.common.functions.InvalidTypesException: The return type of function 'Custom Source' could not be determined automatically, due to type erasure. You can give type information hints by using the returns(...) method on the result of the transformation call, or by letting your function implement the 'ResultTypeQueryable' interface.
	at org.apache.flink.api.dag.Transformation.getOutputType(Transformation.java:441)
	at org.apache.flink.streaming.api.datastream.DataStream.getType(DataStream.java:182)
	at org.apache.flink.streaming.api.datastream.DataStream.flatMap(DataStream.java:618)
	at com.jimo.flink01.Main.main(Main.java:24)
Caused by: org.apache.flink.api.common.functions.InvalidTypesException: Type of TypeVariable 'T' in 'class org.apache.flink.streaming.api.functions.source.FromIteratorFunction' could not be determined. This is most likely a type erasure problem. The type extraction currently supports types with generic variables only in cases where all variables in the return type can be deduced from the input type(s). Otherwise the type has to be specified explicitly using type information.
	at org.apache.flink.api.java.typeutils.TypeExtractor.createTypeInfoWithTypeHierarchy(TypeExtractor.java:995)
	at org.apache.flink.api.java.typeutils.TypeExtractor.privateCreateTypeInfo(TypeExtractor.java:888)
	at org.apache.flink.api.java.typeutils.TypeExtractor.createTypeInfo(TypeExtractor.java:846)
	at org.apache.flink.streaming.api.environment.StreamExecutionEnvironment.getTypeInfo(StreamExecutionEnvironment.java:2118)
	at org.apache.flink.streaming.api.environment.StreamExecutionEnvironment.addSource(StreamExecutionEnvironment.java:1569)
	at org.apache.flink.streaming.api.environment.StreamExecutionEnvironment.addSource(StreamExecutionEnvironment.java:1536)
	at org.apache.flink.streaming.api.environment.StreamExecutionEnvironment.addSource(StreamExecutionEnvironment.java:1522)
	at com.jimo.flink01.Main.main(Main.java:20)
```

这个错告诉我，source源没有指定类型，那我就实现个`ResultTypeQueryable`接口吧：

```java
// source
DataStreamSource<String> stream = env.addSource(
new MyFromIteratorFunction(new MyStringIterator()));


    private static class MyFromIteratorFunction extends FromIteratorFunction<String> implements ResultTypeQueryable<String> {

        public MyFromIteratorFunction(Iterator<String> iterator) {
            super(iterator);
        }

        @Override
        public TypeInformation<String> getProducedType() {
            return Types.STRING;
        }
    }
```

## 执行又出问题

终于，来到执行环节：

```java
Exception in thread "main" java.lang.IllegalStateException: No ExecutorFactory found to execute the application.
	at org.apache.flink.core.execution.DefaultExecutorServiceLoader.getExecutorFactory(DefaultExecutorServiceLoader.java:88)
	at org.apache.flink.streaming.api.environment.StreamExecutionEnvironment.executeAsync(StreamExecutionEnvironment.java:1764)
	at org.apache.flink.streaming.api.environment.StreamExecutionEnvironment.execute(StreamExecutionEnvironment.java:1665)
	at org.apache.flink.streaming.api.environment.LocalStreamEnvironment.execute(LocalStreamEnvironment.java:73)
	at org.apache.flink.streaming.api.environment.StreamExecutionEnvironment.execute(StreamExecutionEnvironment.java:1651)
	at com.jimo.flink01.Main.main(Main.java:44)
```

这个从报错看不出解决办法，原来是缺少了依赖：

```xml
        <dependency>
            <groupId>org.apache.flink</groupId>
            <artifactId>flink-clients_2.12</artifactId>
            <version>${flink-version}</version>
        </dependency>
```

## 终于跑起来

控制台每隔5秒输出一次聚合结果：

```shell
5> (d,1)
6> (a,1)
2> (f,2)
5> (5,1)
1> (w,1)
6> (a,1)
6> (j,1)
2> (6,1)
2> (f,1)
4> (g,1)
5> (s,1)
6> (a,1)
6> (j,2)
```

## 通过界面观察

手动配置端口
```java
Configuration conf = new Configuration();
conf.setInteger(RestOptions.PORT, 8082);
StreamExecutionEnvironment env = StreamExecutionEnvironment
    .createLocalEnvironment(1, conf);
```

```xml
<dependency>
    <groupId>org.apache.flink</groupId>
    <artifactId>flink-runtime-web_2.12</artifactId>
    <version>${flink-version}</version>
    <scope>provided</scope>
</dependency>
```

需要增加 `flink-runtime-web`依赖，否则会出现报错：

```json 
{“errors“:[“Not found.“]}
```

然后访问 [http://localhost:8082](http://localhost:8082) 即可看到页面.

