package com.jimo.flink01;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.typeutils.ResultTypeQueryable;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.FromIteratorFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // source
        DataStreamSource<String> stream = env.addSource(
                new MyFromIteratorFunction(new MyStringIterator()));

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

    private static class MyStringIterator implements Iterator<String>, Serializable {

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

    private static class MyFromIteratorFunction extends FromIteratorFunction<String> implements ResultTypeQueryable<String> {

        public MyFromIteratorFunction(Iterator<String> iterator) {
            super(iterator);
        }

        @Override
        public TypeInformation<String> getProducedType() {
            return Types.STRING;
        }
    }
}
