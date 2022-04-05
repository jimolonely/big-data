package com.jimo.flink02;

import org.apache.flink.streaming.api.datastream.ConnectedStreams;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoMapFunction;

public class UnionConnect {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env =
//                StreamExecutionEnvironment.createLocalEnvironment(1);
                StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<Integer> intStream = env.fromElements(1, 2, 3, 4, 5);
        DataStreamSource<String> strStream = env.fromElements("a", "b", "c");
        ConnectedStreams<Integer, String> connectedStreams = intStream.connect(strStream);

        SingleOutputStreamOperator<String> map = connectedStreams.map(new CoMapFunction<Integer, String, String>() {
            @Override
            public String map1(Integer value) throws Exception {
                return value + "";
            }

            @Override
            public String map2(String value) throws Exception {
                return value;
            }
        });

        map.print();
        env.execute();
    }
}
