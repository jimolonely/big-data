package com.jimo.flink09;

import com.jimo.flink06.StockPrice;
import com.jimo.flink06.StockSource;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.SlidingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;

public class SlideWindowTest {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        final DataStreamSource<StockPrice> source = env.addSource(new StockSource());
        final WindowedStream<StockPrice, String, TimeWindow> window = source
                .keyBy(s -> s.name)
                .window(SlidingEventTimeWindows.of(Time.seconds(10), Time.seconds(5)));

        final WindowedStream<StockPrice, String, TimeWindow> window2 = source
                .keyBy(s -> s.name)
                .window(SlidingProcessingTimeWindows.of(Time.seconds(10), Time.seconds(5)));

        env.execute();
    }
}
