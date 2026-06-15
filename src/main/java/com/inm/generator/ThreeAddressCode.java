package com.inm.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThreeAddressCode {
    private final Map<String, String> tempTypes = new HashMap<>();
    private List<String> instructions = new ArrayList<>();
    private int tempCounter = 0;
    private int labelCounter = 0;

    public String newTemp(String type) {
        String temp = "t" + (tempCounter++);
        tempTypes.put(temp, type);
        return temp;
    }

    public String newLabel() {
        return "L" + (labelCounter++);
    }

    public void emit(String instruction) {
        instructions.add(instruction);
    }

    public String getCode() {
        return String.join("\n", instructions);
    }

    public List<String> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<String> instructions) {
        this.instructions = instructions;
    }

    public boolean hasLabel(String label) {
        return instructions.contains(label + ":");
    }

    public String getTempType(String temp) {
        return tempTypes.get(temp);
    }
}
