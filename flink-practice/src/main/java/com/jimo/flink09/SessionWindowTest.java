package com.jimo.flink09;

import com.jimo.flink06.StockPrice;
import com.jimo.flink06.StockSource;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.EventTimeSessionWindows;
import org.apache.flink.streaming.api.windowing.assigners.SessionWindowTimeGapExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;

public class SessionWindowTest {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        final DataStreamSource<StockPrice> source = env.addSource(new StockSource());
        final WindowedStream<StockPrice, String, TimeWindow> fixGapWindow = source
                .keyBy(s -> s.name)
                .window(EventTimeSessionWindows.withGap(Time.seconds(30)));

        WindowedStream<StockPrice, String, TimeWindow> dynamicGapWindow = source
                .keyBy(s -> s.name)
                .window(EventTimeSessionWindows.withDynamicGap(new SessionWindowTimeGapExtractor<StockPrice>() {
                    @Override
                    public long extract(StockPrice element) {
                        return element.volume;
                    }
                }));
        env.execute();
    }
}
