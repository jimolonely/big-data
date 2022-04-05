package com.jimo.flink02;

import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class MaxMaxBy {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<Tuple3<Integer, Integer, Integer>> source =
                env.fromElements(Tuple3.of(0, 8, 0), Tuple3.of(0, 6, 1), Tuple3.of(1, 4, 3), Tuple3.of(1, 5, 2));

        SingleOutputStreamOperator<Tuple3<Integer, Integer, Integer>> max = source.keyBy(t -> t.f0)
//                .max(2);
                .maxBy(2);

        max.print();
        env.execute();
    }
}
