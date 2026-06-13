package com.inm.generator.assembly;

public class Writer {
    private final StringBuilder data = new StringBuilder();
    private final StringBuilder bss = new StringBuilder();
    private final StringBuilder code = new StringBuilder();
    private final StringBuilder strLiterals = new StringBuilder();

    public void section(StringBuilder sb, String content) {
        sb.append(content).append("\n");
    }

    public void section(StringBuilder sb, String content, int indent) {
        sb.repeat("\t", indent).append(content).append("\n");
    }

    public void directive(String content) {
        section(code, content);
    }

    public void data(String content) {
        section(data, content);
    }

    public void data(String content, int indent) {
        section(data, content, indent);
    }

    public void bss(String content) {
        section(bss, content);
    }

    public void bss(String content, int indent) {
        section(bss, content, indent);
    }

    public void code(String content) {
        section(code, content, 1);
    }

    public void label(String content) {
        section(code, content);
    }

    public void comment(String content) {
        section(code, "; " + content, 1);
    }

    public void strLit(String content) {
        strLiterals.append("\t").append(content).append("\n");
    }

    public void flushStringLiterals() {
        if (!strLiterals.isEmpty()) {
            data.append(strLiterals);
            strLiterals.setLength(0);
        }
    }

    public void blank() {
        section(code, "");
    }

    public StringBuilder getDataBuffer() {
        return data;
    }

    public String build() {
        return data + "\n" + bss + "\n" + code;
    }
}