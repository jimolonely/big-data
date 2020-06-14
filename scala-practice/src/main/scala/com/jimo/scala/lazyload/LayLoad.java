package com.jimo.scala.lazyload;

/**
 * Java懒加载
 */
public class LayLoad {

    private String prop;

    public String getProp() {
        if (prop == null) {
            prop = initProp();
        }
        return prop;
    }

    private String initProp() {
        return "jimo";
    }
}
