package com.inm.generator;

import com.inm.analyzer.ExecutionContext;
import com.inm.helper.Symbol;

public class AssemblyGenerator {

    private final ExecutionContext context;
    private final StringBuilder data    = new StringBuilder();
    private final StringBuilder bss     = new StringBuilder();
    private final StringBuilder code    = new StringBuilder();
    private final StringBuilder helpers = new StringBuilder();

    private boolean needsPrintInt  = false;
    private boolean needsPrintStr  = false;
    private boolean needsReadInt   = false;
    private int     stringLitCount = 0;

    // Literais de string descobertos durante a tradução
    private final StringBuilder strLiterals = new StringBuilder();

    public AssemblyGenerator(ExecutionContext context) {
        this.context = context;
    }

    public String generate() {
        generateDataSection();
        generateBssSection();
        generateCodeSection();
        generateHelpers();

        String result = data + "\n" + bss + "\n" + code + "\n" + helpers;

        System.out.println("\n--- Assembly x86 Gerado (NASM Linux i386) ---");
        System.out.println(result);
        System.out.println("---------------------------------------------");
        return result;
    }

    // ------------------------------------------------------------------ //

    private void generateDataSection() {
        data.append("bits 32\n\n");
        data.append("section .data\n");

        // variáveis declaradas
        for (Symbol s : context.symbolTable().getAllSymbols()) {
            switch (s.type().toUpperCase()) {
                case "INTEGER" -> data.append("    ").append(s.name()).append(" dw 0\n");
                case "BOOLEAN" -> data.append("    ").append(s.name()).append(" db 0\n");
                case "STRING"  -> data.append("    ").append(s.name()).append(" db 256 dup(0)\n");
            }
        }

        // buffer auxiliar para conversão int→string no print
        data.append("    _buf  db 12 dup(0)\n");
        data.append("    _nl   db 10\n");        // newline
    }

    private void generateBssSection() {
        bss.append("section .bss\n");
        bss.append("    _ibuf resb 12\n");       // buffer para leitura de inteiro
    }

    private void generateCodeSection() {
        code.append("section .text\n");
        code.append("global _start\n\n");
        code.append("_start:\n");

        for (String inst : context.threeAddressCode().getInstructions()) {
            code.append("    ; ").append(inst).append("\n");
            translateInstruction(inst);
        }

        // exit(0)
        code.append("\n    ; exit\n");
        code.append("    mov eax, 1\n");
        code.append("    xor ebx, ebx\n");
        code.append("    int 0x80\n");

        // adiciona literais de string coletados durante a tradução
        if (strLiterals.length() > 0) {
            data.append(strLiterals);
        }
    }

    // ------------------------------------------------------------------ //

    private void translateInstruction(String inst) {

        // Rótulos: L_1:
        if (inst.endsWith(":")) {
            code.append(inst).append("\n");
            return;
        }

        // goto L_1
        if (inst.startsWith("goto ")) {
            code.append("    jmp ").append(inst.substring(5).trim()).append("\n");
            return;
        }

        // ifFalse t0 goto L_1
        if (inst.startsWith("ifFalse ")) {
            String[] p = inst.split(" ");
            code.append("    movsx eax, word [").append(p[1]).append("]\n");
            code.append("    cmp eax, 0\n");
            code.append("    je ").append(p[3]).append("\n");
            return;
        }

        // READ variavel
        if (inst.startsWith("READ ")) {
            needsReadInt = true;
            String var = inst.substring(5).trim();
            code.append("    call _read_int\n");
            code.append("    mov [").append(var).append("], ax\n");
            return;
        }

        // WRITE "string literal"
        if (inst.startsWith("WRITE ") && inst.contains("\"")) {
            needsPrintStr = true;
            String literal = inst.substring(6).trim();
            // remove aspas
            String content = literal.substring(1, literal.length() - 1);
            String label = "_str" + stringLitCount++;
            strLiterals.append("    ").append(label)
                    .append(" db `").append(content).append("`, 10\n");
            strLiterals.append("    ").append(label)
                    .append("_len equ $ - ").append(label).append("\n");
            code.append("    mov ecx, ").append(label).append("\n");
            code.append("    mov edx, ").append(label).append("_len\n");
            code.append("    call _print_str\n");
            return;
        }

        // WRITE variavel
        if (inst.startsWith("WRITE ")) {
            needsPrintInt = true;
            String var = inst.substring(6).trim();
            code.append("    movsx eax, word [").append(var).append("]\n");
            code.append("    call _print_int\n");
            return;
        }

        // t0 = ~t1
        if (inst.matches("\\w+ = ~\\w+")) {
            String[] p = inst.split(" = ~");
            code.append("    mov al, [").append(p[1]).append("]\n");
            code.append("    xor al, 1\n");
            code.append("    mov [").append(p[0]).append("], al\n");
            return;
        }

        // x = 5
        if (inst.matches("\\w+ = -?\\d+")) {
            String[] p = inst.split(" = ");
            code.append("    mov word [").append(p[0]).append("], ").append(p[1]).append("\n");
            return;
        }

        // x = TRUE | FALSE
        if (inst.matches("\\w+ = (TRUE|FALSE)")) {
            String[] p = inst.split(" = ");
            String val = p[1].equalsIgnoreCase("TRUE") ? "1" : "0";
            code.append("    mov byte [").append(p[0]).append("], ").append(val).append("\n");
            return;
        }

        // t0 = a + b  |  t0 = a - b
        if (inst.matches("\\w+ = \\w+ [+\\-] \\w+")) {
            String[] p = inst.split(" ");
            String op = p[3].equals("+") ? "add" : "sub";
            code.append("    movsx eax, word [").append(p[2]).append("]\n");
            code.append("    movsx ebx, word [").append(p[4]).append("]\n");
            code.append("    ").append(op).append(" eax, ebx\n");
            code.append("    mov [").append(p[0]).append("], ax\n");
            return;
        }

        // t0 = a * b
        if (inst.matches("\\w+ = \\w+ \\* \\w+")) {
            String[] p = inst.split(" ");
            code.append("    movsx eax, word [").append(p[2]).append("]\n");
            code.append("    movsx ebx, word [").append(p[4]).append("]\n");
            code.append("    imul eax, ebx\n");
            code.append("    mov [").append(p[0]).append("], ax\n");
            return;
        }

        // t0 = a / b
        if (inst.matches("\\w+ = \\w+ / \\w+")) {
            String[] p = inst.split(" ");
            code.append("    movsx eax, word [").append(p[2]).append("]\n");
            code.append("    cdq\n");
            code.append("    movsx ebx, word [").append(p[4]).append("]\n");
            code.append("    idiv ebx\n");
            code.append("    mov [").append(p[0]).append("], ax\n");
            return;
        }

        // t0 = a << n  (strength reduction)
        if (inst.matches("\\w+ = \\w+ << \\d+")) {
            String[] p = inst.split(" ");
            code.append("    movsx eax, word [").append(p[2]).append("]\n");
            code.append("    shl eax, ").append(p[4]).append("\n");
            code.append("    mov [").append(p[0]).append("], ax\n");
            return;
        }

        // t0 = a OR b  |  t0 = a AND b
        if (inst.matches("\\w+ = \\w+ (OR|AND) \\w+")) {
            String[] p = inst.split(" ");
            String op = p[3].equalsIgnoreCase("OR") ? "or" : "and";
            code.append("    mov al, [").append(p[2]).append("]\n");
            code.append("    ").append(op).append(" al, [").append(p[4]).append("]\n");
            code.append("    mov [").append(p[0]).append("], al\n");
            return;
        }

        // t0 = a == b  (e demais relacionais → resultado booleano)
        if (inst.matches("\\w+ = \\w+ (==|<>|<=|>=|<|>) \\w+")) {
            String[] p = inst.split(" ");
            String jmp = switch (p[3]) {
                case "==" -> "je";
                case "<>" -> "jne";
                case "<"  -> "jl";
                case "<=" -> "jle";
                case ">"  -> "jg";
                case ">=" -> "jge";
                default   -> "je";
            };
            String labelTrue = ".cmp_t_" + p[0];
            String labelEnd  = ".cmp_e_" + p[0];
            code.append("    movsx eax, word [").append(p[2]).append("]\n");
            code.append("    movsx ebx, word [").append(p[4]).append("]\n");
            code.append("    cmp eax, ebx\n");
            code.append("    ").append(jmp).append(" ").append(labelTrue).append("\n");
            code.append("    mov byte [").append(p[0]).append("], 0\n");
            code.append("    jmp ").append(labelEnd).append("\n");
            code.append(labelTrue).append(":\n");
            code.append("    mov byte [").append(p[0]).append("], 1\n");
            code.append(labelEnd).append(":\n");
            return;
        }

        // x = y  (cópia simples — deve vir depois dos relacionais)
        if (inst.matches("\\w+ = \\w+")) {
            String[] p = inst.split(" = ");
            code.append("    movsx eax, word [").append(p[1]).append("]\n");
            code.append("    mov [").append(p[0]).append("], ax\n");
            return;
        }

        code.append("    ; [NÃO TRADUZIDO] ").append(inst).append("\n");
    }

    // ------------------------------------------------------------------ //

    private void generateHelpers() {

        // _print_int: imprime inteiro em eax via syscall write
        if (needsPrintInt) {
            helpers.append("\n_print_int:\n");
            helpers.append("    ; converte eax para string em _buf e imprime\n");
            helpers.append("    mov ecx, _buf + 11\n");
            helpers.append("    mov byte [ecx], 10\n");   // newline no fim
            helpers.append("    mov ebx, 10\n");
            helpers.append("    test eax, eax\n");
            helpers.append("    jns .pos\n");
            helpers.append("    neg eax\n");
            helpers.append(".pos:\n");
            helpers.append("    dec ecx\n");
            helpers.append("    xor edx, edx\n");
            helpers.append("    div ebx\n");
            helpers.append("    add dl, '0'\n");
            helpers.append("    mov [ecx], dl\n");
            helpers.append("    test eax, eax\n");
            helpers.append("    jnz .pos\n");
            helpers.append("    ; syscall write(1, ecx, _buf+12-ecx)\n");
            helpers.append("    mov edx, _buf + 12\n");
            helpers.append("    sub edx, ecx\n");
            helpers.append("    mov ebx, 1\n");
            helpers.append("    mov eax, 4\n");
            helpers.append("    int 0x80\n");
            helpers.append("    ret\n");
        }

        // _print_str: imprime string apontada por ecx com tamanho em edx
        if (needsPrintStr) {
            helpers.append("\n_print_str:\n");
            helpers.append("    mov eax, 4\n");    // sys_write
            helpers.append("    mov ebx, 1\n");    // stdout
            helpers.append("    int 0x80\n");
            helpers.append("    ret\n");
        }

        // _read_int: lê inteiro do stdin, retorna em ax
        if (needsReadInt) {
            helpers.append("\n_read_int:\n");
            helpers.append("    ; syscall read(0, _ibuf, 12)\n");
            helpers.append("    mov eax, 3\n");    // sys_read
            helpers.append("    mov ebx, 0\n");    // stdin
            helpers.append("    mov ecx, _ibuf\n");
            helpers.append("    mov edx, 12\n");
            helpers.append("    int 0x80\n");
            helpers.append("    ; converte string para inteiro em eax\n");
            helpers.append("    mov esi, _ibuf\n");
            helpers.append("    xor eax, eax\n");
            helpers.append("    xor ecx, ecx\n");
            helpers.append(".rloop:\n");
            helpers.append("    mov cl, [esi]\n");
            helpers.append("    cmp cl, 10\n");    // newline
            helpers.append("    je .rdone\n");
            helpers.append("    cmp cl, 0\n");
            helpers.append("    je .rdone\n");
            helpers.append("    sub cl, '0'\n");
            helpers.append("    imul eax, 10\n");
            helpers.append("    add eax, ecx\n");
            helpers.append("    inc esi\n");
            helpers.append("    jmp .rloop\n");
            helpers.append(".rdone:\n");
            helpers.append("    ret\n");
        }
    }
}