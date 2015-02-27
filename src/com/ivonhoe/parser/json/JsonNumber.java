package com.ivonhoe.parser.json;

/**
 * Created by ivonhoe on 15-2-27.
 */
public class JsonNumber extends JsonValue {

    private final String string;

    public JsonNumber(String string) {
        if (string == null) {
            throw new NullPointerException("string is null");
        }
        this.string = string;
    }

    @Override
    public void write() {

    }

    @Override
    public String toString() {
        return string;
    }
}
