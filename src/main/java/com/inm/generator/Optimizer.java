package com.inm.generator;

import com.inm.analyzer.CompilationExecutor;
import com.inm.analyzer.ExecutionContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Optimizer {

    private static final ExecutionContext context = CompilationExecutor.context;

    public static void optimize() {
        List<String> instructions = context.threeAddressCode().getInstructions();

        instructions = constantFolding(instructions);
        instructions = constantPropagation(instructions);
        instructions = deadCodeElimination(instructions);
        instructions = strengthReduction(instructions);

        context.threeAddressCode().setInstructions(instructions);
    }

    private static List<String> constantFolding(List<String> inst) {
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
                    case "/" -> a / b;
                    default -> 0;
                };
                result.add(parts[0] + " = " + val);
            } else {
                result.add(line);
            }
        }
        return result;
    }

    private static List<String> constantPropagation(List<String> inst) {
        Map<String, String> constants = new HashMap<>();
        List<String> result = new ArrayList<>();
        for (String line : inst) {
            if (line.matches("\\w+ = -?\\d+")) {
                String[] parts = line.split(" = ");
                constants.put(parts[0].trim(), parts[1].trim());
            }

            for (Map.Entry<String, String> e : constants.entrySet()) {
                line = line.replaceAll("\\b" + e.getKey() + "\\b", e.getValue());
            }
            result.add(line);
        }
        return result;
    }

    private static List<String> deadCodeElimination(List<String> inst) {
        List<String> result = new ArrayList<>();
        boolean dead = false;
        for (String line : inst) {
            if (line.endsWith(":")) dead = false;
            if (!dead) result.add(line);
            if (line.startsWith("GOTO ")) dead = true;
        }
        return result;
    }

    private static List<String> strengthReduction(List<String> inst) {
        List<String> result = new ArrayList<>();
        for (String line : inst) {
            if (line.matches("\\w+ = \\w+ \\* \\d+")) {
                String[] parts = line.split(" ");
                int factor = Integer.parseInt(parts[4]);
                if ((factor & (factor - 1)) == 0) { // potência de 2
                    int shift = (int)(Math.log(factor) / Math.log(2));
                    line = parts[0] + " = " + parts[2] + " << " + shift;
                }
            }
            result.add(line);
        }
        return result;
    }
}
