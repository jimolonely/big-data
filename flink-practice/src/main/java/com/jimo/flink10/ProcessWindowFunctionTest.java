package com.jimo.flink10;

import com.jimo.flink06.StockPrice;
import com.jimo.flink06.StockSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.util.HashMap;
import java.util.Map;

public class ProcessWindowFunctionTest {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);
        DataStreamSource<StockPrice> source = env.addSource(new StockSource());
        SingleOutputStreamOperator<Tuple2<String, Integer>> maxVolume = source
                .keyBy(s -> s.name)
                .timeWindow(Time.seconds(5))
                .process(new ProcessWindowFunction<StockPrice, Tuple2<String, Integer>, String, TimeWindow>() {
                    @Override
                    public void process(String s, Context context, Iterable<StockPrice> elements, Collector<Tuple2<String, Integer>> out) throws Exception {
                        // 统计交易量频次最高的交易数量
                        Map<Integer, Integer> volumeCount = new HashMap<>(8);
                        StockPrice max = null;
                        int maxCount = 0;
                        for (StockPrice item : elements) {
                            volumeCount.put(item.volume, volumeCount.getOrDefault(item.volume, 0) + 1);
                            if (maxCount < volumeCount.get(item.volume)) {
                                max = item;
                                maxCount = volumeCount.get(item.volume);
                            }
                        }
                        if (max != null) {
                            out.collect(Tuple2.of(max.name, max.volume));
                        }
                    }
                });
        maxVolume.print();

        env.execute();
    }
}
