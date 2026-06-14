package com.inm.generator.assembly;

import com.inm.compilation.CompilationContext;
import com.inm.helper.Symbol;

public class AssemblyGenerator {
    private final CompilationContext context;
    private final Writer writer;
    private final InstructionTranslator translator;
    private final HelperInstructions helper;

    public AssemblyGenerator(CompilationContext context) {
        this.context = context;
        this.writer = new Writer();
        this.translator = new InstructionTranslator(writer, context.symbolTable(), context.threeAddressCode());
        this.helper = new HelperInstructions(writer, translator);
    }

    public String generate() {
        generateHeader();
        generateDataSection();
        generateBssSection();
        generateCodeSection();

        String result = writer.build();

        System.out.println("\n--- Assembly x86 Gerado (MASM, Sintaxe Intel Clássica) ---");
        System.out.println(result);
        System.out.println("-----------------------------------------------------------");
        return result;
    }

    private void generateHeader() {
        writer.data("; ============================================");
        writer.data("; Programa : " + context.programName());
        writer.data("; Alvo     : x86 32 bits (MASM, Windows i386)");
        writer.data("; ============================================");
        writer.data("");
        writer.data(".386");
        writer.data(".model flat, stdcall");
        writer.data("option casemap:none");
        writer.data("");
        writer.data("include windows.inc");
        writer.data("include kernel32.inc");
        writer.data("includelib kernel32.lib");
        writer.data("");
    }

    private void generateDataSection() {
        writer.data(".data");

        for (Symbol s : context.symbolTable().getAllSymbols()) {
            switch (s.type().toUpperCase()) {
                case "INTEGER" -> writer.data(s.name() + " dw 0", 1);
                case "BOOLEAN" -> writer.data(s.name() + " db 0", 1);
                case "STRING" -> writer.data(s.name() + " db 256 dup(0)", 1);
            }
        }

        for (String inst : context.threeAddressCode().getInstructions()) {
            if (inst.matches("t\\d+ = .*")) {
                String temp = inst.split(" = ")[0].trim();
                String bufferStr = writer.getDataBuffer().toString();

                if (!bufferStr.contains(" " + temp + " ") && !bufferStr.contains("\t" + temp + " ")) {
                    String tempType = context.threeAddressCode().getTempType(temp);
                    if (tempType == null) {
                        tempType = "INTEGER";
                    }

                    switch (tempType.toUpperCase()) {
                        case "STRING" -> writer.data(temp + " db 256 dup(0)", 1);
                        case "BOOLEAN" -> writer.data(temp + " db 0", 1);
                        default -> writer.data(temp + " dw 0", 1);
                    }
                }
            }
        }

        writer.data("_buf db 14 dup(0)", 1);
        writer.data("_s_true db 'true', 13, 10, 0", 1);
        writer.data("_s_false db 'false', 13, 10, 0", 1);
        writer.data("_nl db 13, 10", 1);
        writer.data("_hOut dd ?", 1);
        writer.data("_hIn dd ?", 1);
        writer.data("_written dd ?", 1);
        writer.data("_read dd ?", 1);
        writer.data("");
    }

    private void generateBssSection() {
        writer.bss(".data?");
        writer.bss("_ibuf db 12 dup(?)", 1);
    }

    private void generateCodeSection() {
        writer.directive(".code");
        writer.blank();
        writer.label("start:");

        writer.code("invoke GetStdHandle, STD_OUTPUT_HANDLE");
        writer.code("mov dword ptr [_hOut], eax");
        writer.blank();

        writer.code("invoke GetStdHandle, STD_INPUT_HANDLE");
        writer.code("mov dword ptr [_hIn], eax");
        writer.blank();

        for (String inst : context.threeAddressCode().getInstructions()) {
            writer.comment(inst);
            translator.translate(inst);
            writer.blank();
        }

        writer.code("invoke Sleep, 50");
        writer.code("invoke ExitProcess, 0");

        writer.flushStringLiterals();
        helper.generateHelpers();

        writer.blank();
        writer.directive("end start");
    }
}
