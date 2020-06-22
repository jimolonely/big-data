package com.jimo.scala.oop;

public class JavaOverrideDemo {
    public static void main(String[] args) {
        Sub sub = new Sub();
        System.out.println(sub.s);

        Super sub2 = new Sub();
        System.out.println(sub2.s);

        System.out.println(((Super) sub).s);
    }
}

class Super {
    String s = "super";
}

class Sub extends Super {
    String s = "sub";
}
