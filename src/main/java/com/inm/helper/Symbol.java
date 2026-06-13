package com.inm.helper;

public record Symbol(
        String name,
        String type,
        int offset
) {
    public int size() {
        return switch (type.toUpperCase()) {
            case "INTEGER" -> 2;
            case "BOOLEAN" -> 1;
            case "STRING" -> 256;
            default -> 0;
        };
    }
}