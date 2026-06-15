package com.inm.generator;

import com.inm.compilation.CompilationContext;
import com.inm.generator.assembly.InstructionEmitter;

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
        List<String> previous;

        do {
            previous = new ArrayList<>(instructions);
            instructions = constantPropagation(instructions);
            instructions = constantFolding(instructions);
            instructions = deadCodeElimination(instructions);
            instructions = strengthReduction(instructions);
        } while (!instructions.equals(previous));

        context.threeAddressCode().setInstructions(instructions);
    }

    private List<String> constantFolding(List<String> inst) {
        List<String> result = new ArrayList<>();
        for (String line : inst) {
            result.add(tryFold(line));
        }
        return result;
    }

    private String tryFold(String line) {
        if (!line.matches("\\w+ = (-?\\d+|TRUE|FALSE) ([+\\-*/]|>=|<=|==|<>|>|<) (-?\\d+|TRUE|FALSE)")) {
            return line;
        }

        String[] parts = line.split(" ");
        String dest = parts[0];
        String left = parts[2];
        String op = parts[3];
        String right = parts[4];

        if (left.matches("-?\\d+") && right.matches("-?\\d+")) {
            int a = Integer.parseInt(left);
            int b = Integer.parseInt(right);
            String val = switch (op) {
                case "+" -> String.valueOf(a + b);
                case "-" -> String.valueOf(a - b);
                case "*" -> String.valueOf(a * b);
                case "/" -> b != 0 ? String.valueOf(a / b) : line;
                case ">" -> a > b ? "TRUE" : "FALSE";
                case "<" -> a < b ? "TRUE" : "FALSE";
                case ">=" -> a >= b ? "TRUE" : "FALSE";
                case "<=" -> a <= b ? "TRUE" : "FALSE";
                case "==" -> a == b ? "TRUE" : "FALSE";
                case "<>" -> a != b ? "TRUE" : "FALSE";
                default -> null;
            };
            if (val != null) return dest + " = " + val;
        }

        if (InstructionEmitter.isBoolLiteral(left) && InstructionEmitter.isBoolLiteral(right)) {
            boolean a = left.equalsIgnoreCase("TRUE");
            boolean b = right.equalsIgnoreCase("TRUE");
            String val = switch (op) {
                case "==" -> a == b ? "TRUE" : "FALSE";
                case "<>" -> a != b ? "TRUE" : "FALSE";
                case "AND" -> a && b ? "TRUE" : "FALSE";
                case "OR" -> a || b ? "TRUE" : "FALSE";
                default -> null;
            };
            if (val != null) return dest + " = " + val;
        }

        return line;
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