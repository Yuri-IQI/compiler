package com.inm.generator;

import com.inm.compilation.CompilationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Optimizer {

    private final CompilationContext context;

    public Optimizer(CompilationContext context) {
        this.context = context;
    }

    public void optimize() {
        List<String> instructions = context.threeAddressCode().getInstructions();

        instructions = constantFolding(instructions);
        instructions = constantPropagation(instructions);
        instructions = deadCodeElimination(instructions);
        instructions = strengthReduction(instructions);

        context.threeAddressCode().setInstructions(instructions);
    }

    private List<String> constantFolding(List<String> inst) {
        List<String> result = new ArrayList<>();
        for (String line : inst) {
            if (line.matches("\\w+ = -?\\d+ [+\\-*/] -?\\d+")) {
                String[] parts = line.split(" ");
                int a = Integer.parseInt(parts[2]);
                int b = Integer.parseInt(parts[4]);
                String op = parts[3];
                int val = switch (op) {
                    case "+" -> a + b;
                    case "-" -> a - b;
                    case "*" -> a * b;
                    case "/" -> b != 0 ? a / b : 0;
                    default -> 0;
                };
                result.add(parts[0] + " = " + val);
            } else {
                result.add(line);
            }
        }
        return result;
    }

    private List<String> constantPropagation(List<String> inst) {
        Map<String, String> constants = new HashMap<>();
        List<String> result = new ArrayList<>();

        for (String line : inst) {
            if (line.endsWith(":") || inst.contains("goto")) {
                constants.clear();
            }

            if (line.matches("\\w+ = -?\\d+")) {
                String[] parts = line.split(" = ");
                constants.put(parts[0].trim(), parts[1].trim());
            }

            if (line.contains(" = ")) {
                String[] sides = line.split(" = ", 2);
                String left = sides[0];
                String right = sides[1];

                for (Map.Entry<String, String> e : constants.entrySet()) {
                    right = right.replaceAll("\\b" + e.getKey() + "\\b", e.getValue());
                }

                result.add(left + " = " + right);
            } else {
                for (Map.Entry<String, String> e : constants.entrySet()) {
                    line = line.replaceAll("\\b" + e.getKey() + "\\b", e.getValue());
                }
                result.add(line);
            }
        }

        return result;
    }

    private List<String> deadCodeElimination(List<String> inst) {
        List<String> result = new ArrayList<>();
        boolean dead = false;
        for (String line : inst) {
            if (line.endsWith(":")) dead = false;
            if (!dead) result.add(line);
            if (line.startsWith("goto ")) dead = true;
        }
        return result;
    }

    private List<String> strengthReduction(List<String> inst) {
        List<String> result = new ArrayList<>();
        for (String line : inst) {
            if (line.matches("\\w+ = \\w+ \\* \\d+")) {
                String[] parts = line.split(" ");
                int factor = Integer.parseInt(parts[4]);
                if (factor > 0 && (factor & (factor - 1)) == 0) {
                    int shift = (int)(Math.log(factor) / Math.log(2));
                    line = parts[0] + " = " + parts[2] + " << " + shift;
                }
            }
            result.add(line);
        }
        return result;
    }
}