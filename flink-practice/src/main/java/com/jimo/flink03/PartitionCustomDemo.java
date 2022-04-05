package com.jimo.flink03;

import org.apache.flink.api.common.functions.Partitioner;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class PartitionCustomDemo {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

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

        stream.print();

        env.execute();
    }
}
