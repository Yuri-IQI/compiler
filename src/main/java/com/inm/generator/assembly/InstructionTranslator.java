package com.inm.generator.assembly;

import com.inm.semantic.SymbolTable;
import com.inm.generator.ThreeAddressCode;

public class InstructionTranslator {
    private final Writer writer;
    private final InstructionEmitter emit;
    private final SymbolTable symbolTable;
    private final ThreeAddressCode tac;

    private int stringLitCount = 0;

    boolean needsPrintInt = false;
    boolean needsPrintBool = false;
    boolean needsPrintStr = false;
    boolean needsReadInt = false;
    boolean needsReadBool = false;
    boolean needsReadStr = false;

    public InstructionTranslator(Writer writer, SymbolTable symbolTable, ThreeAddressCode tac) {
        this.writer = writer;
        this.emit = new InstructionEmitter(writer);
        this.symbolTable = symbolTable;
        this.tac = tac;
    }

    public String getOperandType(String operand) {
        if (operand.startsWith("t")) {
            return tac.getTempType(operand);
        }

        String pureName = operand.replace(SymbolTable.prefix, "");
        return symbolTable.getType(pureName, 0, 0);
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

        if (inst.startsWith("READ_INTEGER ")) {
            translateReadInt(inst.substring(13).trim());
            return;
        }
        if (inst.startsWith("READ_BOOLEAN ")) {
            translateReadBool(inst.substring(13).trim());
            return;
        }
        if (inst.startsWith("READ_STRING ")) {
            translateReadStr(inst.substring(12).trim());
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

        if (inst.matches("(v_)?\\w+ = \".*\"")) {
            translateStringAssign(inst);
            return;
        }

        if (inst.matches("\\w+ = [+-]?\\d+")) {
            String[] p = inst.split(" = ");
            emit.storeWord(p[0], p[1]);
            return;
        }

        if (inst.matches("(?i)\\w+ = (TRUE|FALSE)")) {
            String[] p = inst.split(" = ");
            emit.storeByte(p[0], emit.boolToInt(p[1]));
            return;
        }

        if (inst.matches("\\w+ = (\".*\"|\\w+) CONCAT \\w+")) {
            translateStringConcat(inst);
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

    private void translateStringConcat(String inst) {
        String dest = inst.split(" = ")[0].trim();
        String resto = inst.split(" = ")[1].trim();
        String str1 = resto.split(" CONCAT ")[0].trim();
        String str2 = resto.split(" CONCAT ")[1].trim();

        String src1Label = str1;
        String src2Label = str2;

        if (str1.startsWith("\"")) {
            src1Label = "_lit_concat_" + stringLitCount++;
            String conteudo = str1.substring(1, str1.length() - 1);
            writer.strLit(src1Label + " db '" + conteudo + "', 0");
        }

        if (str2.startsWith("\"")) {
            src2Label = "_lit_concat_" + stringLitCount++;
            String conteudo = str2.substring(1, str2.length() - 1);
            writer.strLit(src2Label + " db '" + conteudo + "', 0");
        }

        String loop1 = "concat1_" + stringLitCount;
        String loop2 = "concat2_" + stringLitCount++;

        writer.code("lea esi, " + src1Label);
        writer.code("lea edi, " + dest);

        writer.label(loop1 + ":");
        writer.code("mov al, [esi]");
        writer.code("test al, al");
        writer.code("jz " + loop2);
        writer.code("mov [edi], al");
        writer.code("inc esi");
        writer.code("inc edi");
        writer.code("jmp " + loop1);

        writer.label(loop2 + ":");
        writer.code("lea esi, " + src2Label);

        String loop2Body = "concat2_body_" + stringLitCount;
        writer.label(loop2Body + ":");
        writer.code("mov al, [esi]");
        writer.code("mov [edi], al");
        writer.code("inc esi");
        writer.code("inc edi");
        writer.code("test al, al");
        writer.code("jnz " + loop2Body);
    }

    private void translateIfFalse(String inst) {
        String[] p = inst.split(" ");
        String expr = p[1].trim();
        String exec = p[3].trim();

        if (InstructionEmitter.isBoolLiteral(expr)) {
            writer.code("mov eax, " + emit.boolToInt(expr));
            writer.code("cmp eax, 0");
            writer.code("je " + exec);
            return;
        }

        String type = getOperandType(expr);

        if ("BOOLEAN".equalsIgnoreCase(type)) {
            writer.code("movzx eax, byte ptr [" + expr + "]");
        } else {
            writer.code("movsx eax, word ptr [" + expr + "]");
        }

        writer.code("cmp eax, 0");
        writer.code("je " + exec);
    }

    private void translateReadInt(String var) {
        needsReadInt = true;
        writer.code("call _read_int");
        writer.code("mov word ptr [" + var + "], ax");
    }

    private void translateReadBool(String var) {
        needsReadBool = true;
        writer.code("call _read_bool");
        writer.code("mov byte ptr [" + var + "], al");
    }

    private void translateReadStr(String var) {
        needsReadStr = true;
        writer.code("lea ecx, " + var);
        writer.code("call _read_str");
    }

    private void translateWrite(String inst) {
        if (inst.contains("\"")) {
            needsPrintStr = true;
            String literal = inst.substring(6).trim();
            String content = literal.substring(1, literal.length() - 1);
            String lbl = "_str" + stringLitCount++;
            writer.strLit(lbl + " db '" + content + "', 0");
            writer.code("mov ecx, offset " + lbl);
            writer.code("call _print_str");
            return;
        }

        String var = inst.substring(6).trim();

        if (InstructionEmitter.isBoolLiteral(var)) {
            needsPrintBool = true;
            writer.code("push " + emit.boolToInt(var));
            writer.code("call _print_bool");
            return;
        }

        if (emit.isNumericLiteral(var)) {
            needsPrintInt = true;
            writer.code("push " + var);
            writer.code("call _print_int");
            return;
        }

        String type = getOperandType(var);
        if (type != null && type.equalsIgnoreCase("STRING")) {
            needsPrintStr = true;
            writer.code("mov ecx, offset " + var);
            writer.code("call _print_str");
        } else if (type != null && type.equalsIgnoreCase("BOOLEAN")) {
            needsPrintBool = true;
            writer.code("movzx eax, byte ptr [" + var + "]");
            writer.code("push eax");
            writer.code("call _print_bool");

        } else {
            needsPrintInt = true;
            emit.loadPrintable("eax", type, var);
            writer.code("push eax");
            writer.code("call _print_int");
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

        String type = getOperandType(dest);
        if ("BOOLEAN".equalsIgnoreCase(type)) {
            emit.loadByte("al", source);
            emit.storeByte(dest, "al");
        } else if ("STRING".equalsIgnoreCase(type)) {
            translateStringCopy(dest, source);
        } else {
            emit.loadWord("ax", source);
            emit.storeWord(dest, "ax");
        }
    }

    private void translateStringCopy(String dest, String source) {
        String loop = "copy_" + stringLitCount++;

        writer.code("lea esi, " + source);
        writer.code("lea edi, " + dest);

        writer.label(loop + ":");
        writer.code("mov al, [esi]");
        writer.code("mov [edi], al");
        writer.code("inc esi");
        writer.code("inc edi");
        writer.code("test al, al");
        writer.code("jnz " + loop);
    }

    private void translateStringAssign(String inst) {
        String[] p = inst.split(" = ", 2);

        String dest = p[0].trim();

        String text = p[1].trim();
        text = text.substring(1, text.length() - 1);

        String lbl = "_lit_" + stringLitCount++;

        writer.strLit(lbl + " db '" + text + "', 0");

        writer.code("lea esi, " + lbl);
        writer.code("lea edi, " + dest);

        String loop = "copy_" + stringLitCount;

        writer.label(loop + ":");
        writer.code("mov al, [esi]");
        writer.code("mov [edi], al");
        writer.code("inc esi");
        writer.code("inc edi");
        writer.code("test al, al");
        writer.code("jnz " + loop);
    }
}