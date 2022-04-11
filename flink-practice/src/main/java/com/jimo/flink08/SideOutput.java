package com.jimo.flink08;

import com.jimo.flink06.StockPrice;
import com.jimo.flink06.StockSource;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

public class SideOutput {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        OutputTag<String> highVolumeOutput = new OutputTag<String>("high-volume-tag") {
        };

        final DataStreamSource<StockPrice> source = env.addSource(new StockSource());
        final DataStream<String> sideOutput = source.keyBy(s -> s.name)
                .process(new HighVolumeOutputFunction(highVolumeOutput))
                .getSideOutput(highVolumeOutput);

        sideOutput.print();

        env.execute();
    }

    private static class HighVolumeOutputFunction extends KeyedProcessFunction<String, StockPrice, String> {

        private OutputTag<String> highVolumeOutput;

        public HighVolumeOutputFunction(OutputTag<String> highVolumeOutput) {
            this.highVolumeOutput = highVolumeOutput;
        }

        @Override
        public void processElement(StockPrice stock, Context ctx, Collector<String> out) throws Exception {
            if (stock.volume > 80) {
                ctx.output(highVolumeOutput, "高交易量：" + stock.volume);
            } else {
                out.collect("普通交易量");
            }
        }
    }
}
