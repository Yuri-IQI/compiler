package com.inm.semantic;

public class TypedOperand {
    private final String value;
    private final String type;

    public TypedOperand(String value, String type) {
        this.value = value;
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return value + ":" + type;
    }
}
