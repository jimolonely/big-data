package com.jimo.flink09;

import com.jimo.flink06.StockPrice;
import com.jimo.flink06.StockSource;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;

public class TumblingWindowTest {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        final DataStreamSource<StockPrice> source = env.addSource(new StockSource());
        final WindowedStream<StockPrice, String, TimeWindow> tumblingWindowStream = source
                .keyBy(s -> s.name)
                .window(TumblingEventTimeWindows.of(Time.seconds(5)));

        final WindowedStream<StockPrice, String, TimeWindow> window = source
                .keyBy(s -> s.name)
                .window(TumblingProcessingTimeWindows.of(Time.seconds(5), Time.seconds(10)));

        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        source.keyBy(s -> s.name).timeWindow(Time.seconds(5));

        env.execute();
    }
}
