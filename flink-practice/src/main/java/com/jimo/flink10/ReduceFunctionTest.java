package com.jimo.flink10;

import com.jimo.flink06.StockPrice;
import com.jimo.flink06.StockSource;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;

public class ReduceFunctionTest {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);
        DataStreamSource<StockPrice> source = env.addSource(new StockSource());
        SingleOutputStreamOperator<StockPrice> sum = source
                .keyBy(s -> s.name)
                .timeWindow(Time.seconds(5))
                .reduce(new ReduceFunction<StockPrice>() {
                    @Override
                    public StockPrice reduce(StockPrice s1, StockPrice s2) throws Exception {
                        return new StockPrice(s1.name, s2.ts, s2.price, s1.volume + s2.volume);
                    }
                });
        sum.print();

        env.execute();
    }
}
