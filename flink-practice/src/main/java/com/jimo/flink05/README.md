# RichFunction有什么不同

查看 `RichFunction`接口，增加了下面的方法：

* open
* close
* getRuntimeContext

测试一下：

```java
public static void main(String[] args) throws Exception {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

    DataStreamSource<Integer> source = env.fromElements(1, 2, 3, 4, 5);

    SingleOutputStreamOperator<Integer> stream = source.flatMap(new MyRichFunction());

    stream.print();

    env.execute();
}

private static class MyRichFunction extends RichFlatMapFunction<Integer, Integer> {

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        System.out.printf("开始了: %s,%s\n", getRuntimeContext().getTaskName(), 
                getRuntimeContext().getIndexOfThisSubtask());
    }

    @Override
    public void flatMap(Integer value, Collector<Integer> out) throws Exception {
        out.collect(value * 2);
    }

    @Override
    public void close() throws Exception {
        super.close();
        System.out.printf("结束了: %s,%s\n", getRuntimeContext().getTaskName(), 
                getRuntimeContext().getIndexOfThisSubtask());
    }
}
```
结果如下，看来每个子任务都会调用 `open,close`
```shell
开始了: Flat Map -> Sink: Print to Std. Out,7
开始了: Flat Map -> Sink: Print to Std. Out,4
开始了: Flat Map -> Sink: Print to Std. Out,5
开始了: Flat Map -> Sink: Print to Std. Out,1
开始了: Flat Map -> Sink: Print to Std. Out,0
开始了: Flat Map -> Sink: Print to Std. Out,3
开始了: Flat Map -> Sink: Print to Std. Out,6
开始了: Flat Map -> Sink: Print to Std. Out,2
7> 2
2> 8
1> 6
8> 4
3> 10
结束了: Flat Map -> Sink: Print to Std. Out,4
结束了: Flat Map -> Sink: Print to Std. Out,1
结束了: Flat Map -> Sink: Print to Std. Out,3
结束了: Flat Map -> Sink: Print to Std. Out,6
结束了: Flat Map -> Sink: Print to Std. Out,5
结束了: Flat Map -> Sink: Print to Std. Out,7
结束了: Flat Map -> Sink: Print to Std. Out,2
结束了: Flat Map -> Sink: Print to Std. Out,0
```



