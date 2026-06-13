package com.inm.generator.assembly;

import com.inm.semantic.SymbolTable;

public class InstructionTranslator {
    private final Writer writer;
    private final InstructionEmitter emit;
    private final SymbolTable symbolTable;

    private int stringLitCount = 0;

    boolean needsPrintInt = false;
    boolean needsPrintStr = false;
    boolean needsReadInt = false;

    public InstructionTranslator(Writer writer, SymbolTable symbolTable) {
        this.writer = writer;
        this.emit = new InstructionEmitter(writer);
        this.symbolTable = symbolTable;
    }

    public void translate(String inst) {
        if (inst.endsWith(":")) {
            writer.label(inst);
            return;
        }

        if (inst.startsWith("goto ")) {
            writer.code("jmp " + inst.substring(5).trim());
            return;
        }

        if (inst.startsWith("ifFalse ")) {
            translateIfFalse(inst);
            return;
        }

        if (inst.startsWith("READ ")) {
            translateRead(inst);
            return;
        }

        if (inst.startsWith("WRITE ")) {
            translateWrite(inst);
            return;
        }

        if (inst.matches("\\w+ = ~\\w+")) {
            translateNeg(inst);
            return;
        }

        if (inst.matches("\\w+ = -?\\d+")) {
            String[] p = inst.split(" = ");
            emit.storeWord(p[0], p[1]);
            return;
        }

        if (inst.matches("(?i)\\w+ = (TRUE|FALSE)")) {
            String[] p = inst.split(" = ");
            emit.storeByte(p[0], emit.boolToInt(p[1]));
            return;
        }

        if (inst.matches("\\w+ = \\w+ [+\\-] \\w+")) {
            translateAddSub(inst);
            return;
        }

        if (inst.matches("\\w+ = \\w+ \\* \\w+")) {
            translateMul(inst);
            return;
        }

        if (inst.matches("\\w+ = \\w+ / \\w+")) {
            translateDiv(inst);
            return;
        }

        if (inst.matches("\\w+ = \\w+ << \\d+")) {
            translateShift(inst);
            return;
        }

        if (inst.matches("\\w+ = \\w+ (OR|AND) \\w+")) {
            translateLogical(inst);
            return;
        }

        if (inst.matches("\\w+ = \\w+ (==|<>|<=|>=|<|>) \\w+")) {
            translateRelational(inst);
            return;
        }

        if (inst.matches("v_\\w+ = \\w+")) {
            translateCopy(inst);
            return;
        }

        writer.comment("[NÃO TRADUZIDO] " + inst);
    }

    private void translateIfFalse(String inst) {
        String[] p = inst.split(" ");
        String expr = p[1].trim();
        String exec = p[3].trim();

        if (emit.isBoolLiteral(expr)) {
            writer.code("mov eax, " + emit.boolToInt(expr));
            writer.code("cmp eax, 0");
            writer.code("je " + exec);
            return;
        }

        if (expr.startsWith(SymbolTable.prefix)) {
            String pureName = expr.replace(SymbolTable.prefix, "");
            String type = symbolTable.getType(pureName, 0, 0);
            if (type != null && type.equalsIgnoreCase("BOOLEAN")) {
                writer.code("movzx eax, byte [" + expr + "]");
            } else {
                writer.code("mov ax, word [" + expr + "]");
            }
        } else {
            writer.code("movzx eax, byte [" + expr + "]");
        }

        writer.code("cmp eax, 0");
        writer.code("je " + exec);
    }

    private void translateRead(String inst) {
        needsReadInt = true;
        String var = inst.substring(5).trim();
        writer.code("call _read_int");
        emit.storeWord(var, "ax");
    }

    private void translateWrite(String inst) {
        if (inst.contains("\"")) {
            needsPrintStr = true;
            String literal = inst.substring(6).trim();
            String content = literal.substring(1, literal.length() - 1);
            String lbl = "_str" + stringLitCount++;
            writer.strLit(lbl + " db '" + content + "', 10");
            writer.strLit(lbl + "_len equ $ - " + lbl);
            writer.code("mov ecx, " + lbl);
            writer.code("mov edx, " + lbl + "_len");
            writer.code("call _print_str");
            return;
        }

        String var = inst.substring(6).trim();

        if (emit.isNumericLiteral(var) || emit.isBoolLiteral(var)) {
            needsPrintInt = true;
            emit.loadPrintable("eax", null, var);
            writer.code("push eax");
            writer.code("call _print_int");
            writer.code("add esp, 4");
            return;
        }

        String pureName = var.replace(SymbolTable.prefix, "");
        String type = symbolTable.getType(pureName, 0, 0);

        if (type != null && type.equalsIgnoreCase("STRING")) {
            needsPrintStr = true;
            writer.code("mov ecx, " + var);
            writer.code("mov edx, 256");
            writer.code("call _print_str");
        } else {
            needsPrintInt = true;
            emit.loadPrintable("eax", type, var);
            writer.code("push eax");
            writer.code("call _print_int");
            writer.code("add esp, 4");
        }
    }

    private void translateNeg(String inst) {
        String[] p = inst.split(" = ~");
        emit.loadByte("al", p[1]);
        writer.code("xor al, 1");
        emit.storeByte(p[0], "al");
    }

    private void translateAddSub(String inst) {
        String[] p = inst.split(" ");
        String op = p[3].equals("+") ? "add" : "sub";
        emit.loadWord("ax", p[2]);
        emit.opWord(op, p[4]);
        emit.storeWord(p[0], "ax");
    }

    private void translateMul(String inst) {
        String[] p = inst.split(" ");
        emit.loadWord("ax", p[2]);
        emit.mulWord(p[4]);
        emit.storeWord(p[0], "ax");
    }

    private void translateDiv(String inst) {
        String[] p = inst.split(" ");
        emit.loadWord("ax", p[2]);
        writer.code("cwd");
        emit.divWord(p[4]);
        emit.storeWord(p[0], "ax");
    }

    private void translateShift(String inst) {
        String[] p = inst.split(" ");
        emit.loadWord("ax", p[2]);
        writer.code("shl ax, " + p[4]);
        emit.storeWord(p[0], "ax");
    }

    private void translateLogical(String inst) {
        String[] p = inst.split(" ");
        String op = p[3].equalsIgnoreCase("OR") ? "or" : "and";
        emit.loadByte("al", p[2]);
        emit.opByte(op, p[4]);
        emit.storeByte(p[0], "al");
    }

    private void translateRelational(String inst) {
        String[] p = inst.split(" ");
        String jmp = switch (p[3]) {
            case "==" -> "je";
            case "<>" -> "jne";
            case "<" -> "jl";
            case "<=" -> "jle";
            case ">" -> "jg";
            case ">=" -> "jge";
            default -> "je";
        };
        String lblTrue = "cmp_t_" + p[0];
        String lblEnd = "cmp_e_" + p[0];
        emit.loadWord("ax", p[2]);
        emit.opWord("cmp", p[4]);
        writer.code(jmp + " " + lblTrue);
        emit.storeByte(p[0], "0");
        writer.code("jmp " + lblEnd);
        writer.label(lblTrue + ":");
        emit.storeByte(p[0], "1");
        writer.label(lblEnd + ":");
    }

    private void translateCopy(String inst) {
        String[] p = inst.split(" = ");
        String dest = p[0].trim();
        String source = p[1].trim();
        String pureName = dest.replace(SymbolTable.prefix, "");
        String type = symbolTable.getType(pureName, 0, 0);

        if (type != null && type.equalsIgnoreCase("BOOLEAN")) {
            emit.loadByte("al", source);
            emit.storeByte(dest, "al");
        } else {
            emit.loadWord("ax", source);
            emit.storeWord(dest, "ax");
        }
    }
}