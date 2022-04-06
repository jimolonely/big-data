package com.jimo.flink06;

import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 股票输入源，随机一些交易
 *
 * @author jimo
 * @date 2022/4/6 15:56
 **/
public class StockSource implements SourceFunction<StockPrice> {

    @Override
    public void run(SourceContext<StockPrice> sourceContext) throws Exception {
        Random r = new Random();
        while (true) {
            // 随机等待
            final int waitTime = r.nextInt(1000);
            if (waitTime < 200) {
                TimeUnit.MILLISECONDS.sleep(waitTime);
            }
            sourceContext.collect(new StockPrice("股票" + r.nextInt(10), System.currentTimeMillis(), r.nextDouble() * 100, r.nextInt(100)));
        }
    }

    @Override
    public void cancel() {

    }
}
