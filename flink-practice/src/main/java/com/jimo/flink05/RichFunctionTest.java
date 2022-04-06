package com.jimo.flink05;

import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

public class RichFunctionTest {

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
            System.out.printf("开始了: %s,%s\n", getRuntimeContext().getTaskName(), getRuntimeContext().getIndexOfThisSubtask());
        }

        @Override
        public void flatMap(Integer value, Collector<Integer> out) throws Exception {
            out.collect(value * 2);
        }

        @Override
        public void close() throws Exception {
            super.close();
            System.out.printf("结束了: %s,%s\n", getRuntimeContext().getTaskName(), getRuntimeContext().getIndexOfThisSubtask());
        }
    }
}
