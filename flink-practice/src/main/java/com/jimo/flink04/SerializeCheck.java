package com.jimo.flink04;

import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.typeinfo.TypeInformation;

public class SerializeCheck {

    public static void main(String[] args) {
        System.out.println(
                TypeInformation.of(Integer.class)
                        .createSerializer(new ExecutionConfig())
        );

        System.out.println(
                TypeInformation.of(A.class)
                        .createSerializer(new ExecutionConfig())
        );
    }

    private static class A {
        private A() {
        }
    }
}
