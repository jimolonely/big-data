package com.jimo.flink10;

import com.jimo.flink06.StockPrice;
import com.jimo.flink06.StockSource;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;

public class AggregateFunctionTest {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

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

        env.execute();
    }
}
