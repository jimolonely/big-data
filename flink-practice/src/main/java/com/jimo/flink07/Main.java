package com.jimo.flink07;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkGenerator;
import org.apache.flink.api.common.eventtime.WatermarkOutput;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.apache.flink.streaming.api.watermark.Watermark;

public class Main {

    public static void main(String[] args) {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        final DataStreamSource<MyEventData> stream = env.addSource(new MyEventDataSource());

        env.getConfig().setAutoWatermarkInterval(5000L);
        stream.assignTimestampsAndWatermarks(
                WatermarkStrategy
                        // 水印设置
                        .forGenerator(s -> new MyPeriodicGenerator())
                        // event time设置
                        .withTimestampAssigner((SerializableTimestampAssigner<MyEventData>) (myEventData, l) -> myEventData.eventTime)
        );
    }

    private static class MyDataGenerator implements WatermarkGenerator<MyEventData> {

        @Override
        public void onEvent(MyEventData event, long eventTimestamp, WatermarkOutput output) {
            if (event.hasWatermark) {
                output.emitWatermark(new org.apache.flink.api.common.eventtime.Watermark(event.watermarkTime));
            }
        }

        @Override
        public void onPeriodicEmit(WatermarkOutput output) {
            // 这里什么都不做
        }
    }

    private static class MyPeriodicGenerator implements WatermarkGenerator<MyEventData> {

        private long currentMaxTimestamp;

        @Override
        public void onEvent(MyEventData event, long eventTimestamp, WatermarkOutput output) {
            // 每个元素进来后都会调用这个方法
            currentMaxTimestamp = Math.max(currentMaxTimestamp, eventTimestamp);
        }

        @Override
        public void onPeriodicEmit(WatermarkOutput output) {
            // 固定周期调用,设置水印比最大值慢1分钟
            output.emitWatermark(new org.apache.flink.api.common.eventtime.Watermark(currentMaxTimestamp - 60 * 1000));
        }
    }

    private static class MyEventData {
        public double data;
        public long eventTime;
        public boolean hasWatermark;
        public long watermarkTime;
    }

    private static class MyEventDataSource extends RichSourceFunction<MyEventData> {

        @Override
        public void run(SourceContext<MyEventData> ctx) throws Exception {
            while (true) {
                MyEventData data = getNext();
                // 设置事件时间
                ctx.collectWithTimestamp(data, data.eventTime);

                // 设置Watermark
                if (data.hasWatermark) {
                    ctx.emitWatermark(new Watermark(data.watermarkTime));
                }
            }
        }

        private MyEventData getNext() {
            return new MyEventData();
        }

        @Override
        public void cancel() {
        }
    }
}
