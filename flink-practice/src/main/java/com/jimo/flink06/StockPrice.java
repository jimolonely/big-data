package com.jimo.flink06;

import lombok.ToString;

@ToString
public class StockPrice {
    public StockPrice() {
    }

    public StockPrice(String name, long ts, double price, int volume) {
        this.name = name;
        this.ts = ts;
        this.price = price;
        this.volume = volume;
    }

    /**
     * 股票名称
     */
    public String name;
    /**
     * 时间戳
     */
    public long ts;
    public double price;
    /**
     * 交易量
     */
    public int volume;
}
