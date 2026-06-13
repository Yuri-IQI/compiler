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
        this.translator = new InstructionTranslator(writer, context.symbolTable());
        this.helper = new HelperInstructions(writer, translator);
    }

    public String generate() {
        generateDataSection();
        generateBssSection();
        generateCodeSection();

        String result = writer.build();

        System.out.println("\n--- Assembly x86 Gerado (Sintaxe Intel Clássica) ---");
        System.out.println(result);
        System.out.println("----------------------------------------------------");
        return result;
    }

    private void generateDataSection() {
        writer.data("; ============================================");
        writer.data("; Programa  : " + context.programName());
        writer.data("; Gerado por: Compilador INM");
        writer.data("; Alvo      : x86 32 bits (NASM, Linux i386)");
        writer.data("; ============================================");
        writer.data("");
        writer.data("bits 32");
        writer.data("");
        writer.data("section .data");

        for (Symbol s : context.symbolTable().getAllSymbols()) {
            switch (s.type().toUpperCase()) {
                case "INTEGER" -> writer.data(s.name() + " dw 0", 1);
                case "BOOLEAN" -> writer.data(s.name() + " db 0", 1);
                case "STRING" -> writer.data(s.name() + " times 256 db 0", 1);
            }
        }

        for (String inst : context.threeAddressCode().getInstructions()) {
            if (inst.matches("t\\d+ = .*")) {
                String temp = inst.split(" = ")[0].trim();
                if (!writer.getDataBuffer().toString().contains("\t" + temp + " ")) {
                    writer.data(temp + " dw 0", 1);
                }
            }
        }

        writer.data("_buf times 12 db 0", 1);
        writer.data("_nl  db 10", 1);
    }

    private void generateBssSection() {
        writer.bss("section .bss");
        writer.bss("_ibuf resb 12", 1);
    }

    private void generateCodeSection() {
        writer.directive("section .text");
        writer.directive("global _start");
        writer.blank();
        writer.label("_start:");

        for (String inst : context.threeAddressCode().getInstructions()) {
            writer.comment(inst);
            translator.translate(inst);
            writer.blank();
        }

        writer.comment("encerramento: syscall exit(0)");
        writer.code("mov eax, 1");
        writer.code("xor ebx, ebx");
        writer.code("int 0x80");

        writer.flushStringLiterals();
        helper.generateHelpers();
    }
}