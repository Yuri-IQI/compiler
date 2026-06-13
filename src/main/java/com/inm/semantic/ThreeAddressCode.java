package com.inm.semantic;

import java.util.ArrayList;
import java.util.List;

public class ThreeAddressCode {
    private List<String> instructions = new ArrayList<>();
    private int tempCounter = 0;
    private int labelCounter = 0;

    public String newTemp() {
        return "t" + (tempCounter++);
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
}
