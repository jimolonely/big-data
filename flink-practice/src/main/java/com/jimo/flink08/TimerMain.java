package com.jimo.flink08;

import com.jimo.flink06.StockPrice;
import com.jimo.flink06.StockSource;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

public class TimerMain {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        final DataStreamSource<StockPrice> source = env.addSource(new StockSource());

        final SingleOutputStreamOperator<String> infoStream = source.keyBy(s -> s.name)
                .process(new StockIncreaseInfoFunction(2000));

        infoStream.print();

        env.execute();
    }

    /**
     * 监控持续涨价的股票并发出通知
     */
    private static class StockIncreaseInfoFunction extends KeyedProcessFunction<String, StockPrice, String> {

        /**
         * 统计时间间隔
         */
        private final long intervalMills;
        private ValueState<Double> lastPrice;
        private ValueState<Long> currentTimer;

        public StockIncreaseInfoFunction(long intervalMills) {
            this.intervalMills = intervalMills;
        }

        @Override
        public void open(Configuration parameters) throws Exception {
            lastPrice = getRuntimeContext().getState(new ValueStateDescriptor<>("lastPrice", Types.DOUBLE));
            currentTimer = getRuntimeContext().getState(new ValueStateDescriptor<>("timer", Types.LONG));
        }

        @Override
        public void processElement(StockPrice stock, Context ctx, Collector<String> out) throws Exception {
            if (lastPrice.value() == null) {
                // 第一次不做处理
            } else {
                final Double prevPrice = lastPrice.value();
                long currTimerTimestamp = currentTimer.value() == null ? 0 : currentTimer.value();

                if (stock.price < prevPrice) {
                    // 如果流入的股票价格降低了，那么删除该Timer，否则保留Timer
                    ctx.timerService().deleteProcessingTimeTimer(currTimerTimestamp);
                    currentTimer.clear();
                } else if (stock.price >= prevPrice && currTimerTimestamp == 0) {
                    // 如果新流入的股票价格升高，且还没有Timer,那么注册一个Timer
                    // Timer的时间 = 当前时间+时间间隔
                    long timerTs = (ctx.timestamp() == null ? System.currentTimeMillis() : ctx.timestamp()) + intervalMills;
                    ctx.timerService().registerProcessingTimeTimer(timerTs);
                    currentTimer.update(timerTs);
                }
            }
            lastPrice.update(stock.price);
        }

        @Override
        public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> out) throws Exception {
            // 当定时器触发时调用,这里发起通知
            out.collect(String.format("您的股票%s在%sms时间内持续上涨，请关注异动！", ctx.getCurrentKey(), intervalMills));
            currentTimer.clear();
        }
    }
}
