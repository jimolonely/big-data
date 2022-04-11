package com.jimo.flink09;

import com.jimo.flink06.StockPrice;
import com.jimo.flink06.StockSource;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.GlobalWindows;

public class GlobalWindowTest {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        final DataStreamSource<StockPrice> source = env.addSource(new StockSource());
        source.keyBy(s -> s.name).window(GlobalWindows.create());

        env.execute();
    }
}
