package com.inm.generator.assembly;

public class InstructionEmitter {

    private final Writer writer;

    public InstructionEmitter(Writer writer) {
        this.writer = writer;
    }

    public boolean isNumericLiteral(String op) {
        return op.matches("-?\\d+");
    }

    public boolean isBoolLiteral(String op) {
        return op.equalsIgnoreCase("TRUE") || op.equalsIgnoreCase("FALSE");
    }

    public String boolToInt(String op) {
        return op.equalsIgnoreCase("TRUE") ? "1" : "0";
    }

    public void loadWord(String reg, String op) {
        if (isNumericLiteral(op)) {
            writer.code("mov " + reg + ", " + op);
        } else if (isBoolLiteral(op)) {
            writer.code("mov " + reg + ", " + boolToInt(op));
        } else {
            writer.code("mov " + reg + ", word ptr [" + op + "]");
        }
    }

    public void loadPrintable(String reg, String type, String op) {
        if (isNumericLiteral(op)) {
            writer.code("mov " + reg + ", " + op);
        } else if (type != null && type.equalsIgnoreCase("BOOLEAN")) {
            writer.code("movzx " + reg + ", byte ptr [" + op + "]");
        } else {
            writer.code("movsx " + reg + ", word ptr [" + op + "]");
        }
    }

    public void loadByte(String reg, String op) {
        if (isBoolLiteral(op)) {
            writer.code("mov " + reg + ", " + boolToInt(op));
        } else if (isNumericLiteral(op)) {
            writer.code("mov " + reg + ", " + op);
        } else {
            writer.code("mov " + reg + ", byte ptr [" + op + "]");
        }
    }

    public void storeWord(String dest, String val) {
        writer.code("mov word ptr [" + dest + "], " + val);
    }

    public void storeByte(String dest, String val) {
        writer.code("mov byte ptr [" + dest + "], " + val);
    }

    public void opWord(String op, String operand) {
        if (isNumericLiteral(operand)) {
            writer.code(op + " ax, " + operand);
        } else {
            writer.code(op + " ax, word ptr [" + operand + "]");
        }
    }

    public void mulWord(String operand) {
        if (isNumericLiteral(operand)) {
            writer.code("imul ax, ax, " + operand);
        } else {
            writer.code("imul word ptr [" + operand + "]");
        }
    }

    public void divWord(String operand) {
        if (isNumericLiteral(operand)) {
            writer.code("mov bx, " + operand);
            writer.code("idiv bx");
        } else {
            writer.code("idiv word ptr [" + operand + "]");
        }
    }

    public void opByte(String op, String operand) {
        if (isNumericLiteral(operand)) {
            writer.code(op + " al, " + operand);
        } else {
            writer.code(op + " al, byte ptr [" + operand + "]");
        }
    }
}