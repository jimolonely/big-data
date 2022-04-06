package com.jimo.flink06;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;

public class Main {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        final DataStreamSource<StockPrice> source = env.addSource(new StockSource());

//        maxPrice(source);
        exchangeRateChange(source);

        env.execute();
    }

    /**
     * 实时计算某只股票的最大价格
     */
    private static void maxPrice(DataStreamSource<StockPrice> source) {
        String targetStockName = "股票5";
        final SingleOutputStreamOperator<StockPrice> maxPrice = source
                .filter(stockPrice -> stockPrice.name.equals(targetStockName))
                .keyBy(s -> s.name)
                // 计算5秒内的价格最大值
                .timeWindow(Time.seconds(5))
                .maxBy("price");
        maxPrice.print();
    }

    /**
     * 价格汇率转换，假设*6转成美元
     */
    private static void exchangeRateChange(DataStreamSource<StockPrice> source) {
        source.map(s -> {
            s.price *= 6;
            return s;
        }).print();
    }
}
