package com.ivonhoe.parser.json;

/**
 * Created by ivonhoe on 15-1-29.
 */
public class JsonString extends JsonValue {

    private final String string;

    public JsonString(String string) {
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
