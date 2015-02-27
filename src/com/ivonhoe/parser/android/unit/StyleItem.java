package com.ivonhoe.parser.android.unit;

/**
 * Created by ivonhoe on 15-1-23.
 */
public class StyleItem {
    private String name;

    private String value;

    public StyleItem(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "[" + "name:" + name + ",value:" + value + "]";
    }
}
