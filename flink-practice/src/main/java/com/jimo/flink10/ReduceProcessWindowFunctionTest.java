package com.jimo.flink10;

import com.jimo.flink06.StockPrice;
import com.jimo.flink06.StockSource;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

public class ReduceProcessWindowFunctionTest {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

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

        env.execute();
    }
}
