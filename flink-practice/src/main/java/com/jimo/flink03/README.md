# 数据重分布的方式

## shuffle

基于正态分布的随机策略。

## rebalance和rescale

rebalance 采用轮询将数据均匀分配给各个子任务。

rescale采用就近原则发给下游子任务，所以开销更小，因为传输更少。

## broadcast

数据被复制并广播给下游所有子任务。

## global

将所有数据发送给下游算子的第一个子任务上，可能有严重的性能问题。

## partitionCustom

自定义数据重分布逻辑。

下面是一个例子：
```java
int parallelism = 4;
env.setParallelism(parallelism);
DataStreamSource<Tuple3<Integer, Integer, Integer>> source = env.fromElements(
        Tuple3.of(0, 8, 0), Tuple3.of(0, 6, 1),
        Tuple3.of(1, 4, 3), Tuple3.of(1, 5, 2)
);

DataStream<Tuple3<Integer, Integer, Integer>> stream = source.partitionCustom(new Partitioner<Integer>() {
    @Override
    public int partition(Integer key, int numPartitions) {
        // 分区规则为取余
        return key % parallelism;
    }
}, new KeySelector<Tuple3<Integer, Integer, Integer>, Integer>() {
    @Override
    public Integer getKey(Tuple3<Integer, Integer, Integer> value) throws Exception {
        // 使用第二个值作为key来分区
        return value.f1;
    }
});
```
结果如下：可以看到，开始的数据即分区号，确实按照第二个元素取余
```shell
2> (1,5,2)
3> (0,6,1)
1> (0,8,0)
1> (1,4,3)
```
