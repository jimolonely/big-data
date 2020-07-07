package com.jimo.scala.col;

import java.util.ArrayList;
import java.util.Arrays;

public class JColDemo01 {
    public static void main(String[] args) {
        ArrayList<String> a = new ArrayList<String>();
        a.add("a");
        a.add("b");
        System.out.println(a.hashCode());
        a.add("c");
        System.out.println(a.hashCode());

        int[] arr = new int[2];
        arr[0] = 1;
        arr[1] = 2;
        System.out.println(Arrays.hashCode(arr));
        arr[0] = 3;
        System.out.println(Arrays.hashCode(arr));
    }
}
