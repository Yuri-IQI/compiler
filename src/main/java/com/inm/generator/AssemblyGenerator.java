package com.inm.generator;

import com.inm.analyzer.ExecutionContext;
import com.inm.helper.Symbol;
import com.inm.semantic.SymbolTable;

public class AssemblyGenerator {

    private final ExecutionContext context;
    private final StringBuilder data = new StringBuilder();
    private final StringBuilder bss = new StringBuilder();
    private final StringBuilder code = new StringBuilder();
    private final StringBuilder strLiterals = new StringBuilder();

    private int stringLitCount = 0;
    private boolean needsPrintInt = false;
    private boolean needsPrintStr = false;
    private boolean needsReadInt = false;

    public AssemblyGenerator(ExecutionContext context) {
        this.context = context;
    }

    // ------------------------------------------------------------------ //
    // Utilitários de escrita
    // ------------------------------------------------------------------ //

    private void line(StringBuilder sb, String content, int indent) {
        sb.repeat("\t", indent).append(content).append("\n");
    }

    private void line(StringBuilder sb, String content) {
        line(sb, content, 0);
    }

    private void code(String content) {
        line(code, content, 1);
    }

    private void label(String content) {
        line(code, content, 0);
    }

    private void comment(String content) {
        line(code, "; " + content, 1);
    }

    // ------------------------------------------------------------------ //
    // Utilitários de classificação de operandos
    // ------------------------------------------------------------------ //

    private boolean isNumericLiteral(String op) {
        return op.matches("-?\\d+");
    }

    private boolean isBoolLiteral(String op) {
        return op.equalsIgnoreCase("TRUE") || op.equalsIgnoreCase("FALSE");
    }

    private String boolToInt(String op) {
        return op.equalsIgnoreCase("TRUE") ? "1" : "0";
    }

    // ------------------------------------------------------------------ //
    // Utilitários de load/store
    // ------------------------------------------------------------------ //

    // INTEGER: carrega em registrador de 16 bits
    // literal  → mov ax, 5
    // variável → mov ax, word [x]
    private void loadWord(String reg, String op) {
        if (isNumericLiteral(op)) {
            code("mov " + reg + ", " + op);
        } else if (isBoolLiteral(op)) {
            code("mov " + reg + ", " + boolToInt(op));
        } else {
            code("mov " + reg + ", word [" + op + "]");
        }
    }

    // INTEGER com extensão de sinal para 32 bits
    // usado para empilhar argumento em push eax
    // literal  → mov eax, 5
    // variável → movsx eax, word [x]
    private void loadPrintable(String reg, String type, String op) {
        if (isNumericLiteral(op)) {
            code("mov " + reg + ", " + op);
        } else if (type != null && type.equalsIgnoreCase("BOOLEAN")) {
            code("movzx " + reg + ", byte [" + op + "]");
        } else {
            code("movsx " + reg + ", word [" + op + "]");
        }
    }

    // BOOLEAN: carrega em registrador de 8 bits
    // bool literal → mov al, 1/0
    // numérico     → mov al, n
    // variável     → mov al, byte [x]
    private void loadByte(String reg, String op) {
        if (isBoolLiteral(op)) {
            code("mov " + reg + ", " + boolToInt(op));
        } else if (isNumericLiteral(op)) {
            code("mov " + reg + ", " + op);
        } else {
            code("mov " + reg + ", byte [" + op + "]");
        }
    }

    // INTEGER: armazena registrador 16 bits em memória
    private void storeWord(String dest, String reg) {
        code("mov word [" + dest + "], " + reg);
    }

    // BOOLEAN: armazena registrador 8 bits em memória
    private void storeByte(String dest, String reg) {
        code("mov byte [" + dest + "], " + reg);
    }

    // ------------------------------------------------------------------ //
    // Utilitários de operações aritméticas/lógicas
    // ------------------------------------------------------------------ //

    // Operação word: op ax, operando
    private void opWord(String op, String operand) {
        if (isNumericLiteral(operand)) {
            code(op + " ax, " + operand);
        } else {
            code(op + " ax, word [" + operand + "]");
        }
    }

    // Operação byte: op al, operando
    private void opByte(String op, String operand) {
        if (isNumericLiteral(operand)) {
            code(op + " al, " + operand);
        } else {
            code(op + " al, byte [" + operand + "]");
        }
    }

    // imul: aceita imediato (ax, n) ou memória word [x]
    private void mulWord(String operand) {
        if (isNumericLiteral(operand)) {
            code("imul ax, " + operand);
        } else {
            code("imul word [" + operand + "]");
        }
    }

    // idiv: não aceita imediato → usa bx como auxiliar
    private void divWord(String operand) {
        if (isNumericLiteral(operand)) {
            code("mov bx, " + operand);
            code("idiv bx");
        } else {
            code("idiv word [" + operand + "]");
        }
    }

    // ------------------------------------------------------------------ //
    // Geração principal
    // ------------------------------------------------------------------ //

    public String generate() {
        generateDataSection();
        generateBssSection();
        generateCodeSection();

        String result = data + "\n" + bss + "\n" + code;

        System.out.println("\n--- Assembly x86 Gerado (Sintaxe Intel Clássica) ---");
        System.out.println(result);
        System.out.println("----------------------------------------------------");
        return result;
    }

    // ------------------------------------------------------------------ //
    // Seções
    // ------------------------------------------------------------------ //

    private void generateDataSection() {
        line(data, "; ============================================");
        line(data, "; Programa  : " + context.programName());
        line(data, "; Gerado por: Compilador INM");
        line(data, "; Alvo      : x86 32 bits (NASM, Linux i386)");
        line(data, "; ============================================");
        line(data, "");
        line(data, "bits 32");
        line(data, "");
        line(data, "section .data");

        // Variáveis da tabela de símbolos
        // INTEGER → dw (2 bytes, -32768 a 32767)
        // BOOLEAN → db (1 byte,  0=false, 1=true)
        // STRING  → db 256 bytes reservados
        for (Symbol s : context.symbolTable().getAllSymbols()) {
            switch (s.type().toUpperCase()) {
                case "INTEGER" -> line(data, s.name() + " dw 0", 1);
                case "BOOLEAN" -> line(data, s.name() + " db 0", 1);
                case "STRING" -> line(data, s.name() + " times 256 db 0", 1);
            }
        }

        // Temporários gerados pelo compilador (INTEGER por padrão)
        for (String inst : context.threeAddressCode().getInstructions()) {
            if (inst.matches("t\\d+ = .*")) {
                String temp = inst.split(" = ")[0].trim();
                if (!data.toString().contains("\t" + temp + " ")) {
                    line(data, temp + " dw 0", 1);
                }
            }
        }

        line(data, "_buf times 12 db 0", 1);
        line(data, "_nl  db 10", 1);
    }

    private void generateBssSection() {
        line(bss, "section .bss");
        line(bss, "_ibuf resb 12", 1);
    }

    private void generateCodeSection() {
        line(code, "section .text");
        line(code, "global _start");
        line(code, "");
        label("_start:");

        for (String inst : context.threeAddressCode().getInstructions()) {
            comment(inst);
            translateInstruction(inst);
            line(code, "");
        }

        comment("encerramento: syscall exit(0)");
        code("mov eax, 1");
        code("xor ebx, ebx");
        code("int 0x80");

        if (strLiterals.length() > 0) {
            data.append(strLiterals);
        }

        generateHelpers();
    }

    // ------------------------------------------------------------------ //
    // Tradução de instruções 3AC → Assembly
    // ------------------------------------------------------------------ //

    private void translateInstruction(String inst) {

        // L0:
        if (inst.endsWith(":")) {
            label(inst);
            return;
        }

        // goto L0
        if (inst.startsWith("goto ")) {
            code("jmp " + inst.substring(5).trim());
            return;
        }

        // ifFalse t0 goto L0
        if (inst.startsWith("ifFalse ")) {
            String[] p = inst.split(" ");
            String expr = p[1].trim();
            String exec = p[3].trim();

            if (isBoolLiteral(expr)) {
                String val = boolToInt(expr);
                code("mov eax, " + val);
                code("cmp eax, 0");
                code("je " + exec);
                return;
            }

            if (expr.startsWith(SymbolTable.prefix)) {
                String pureName = expr.replace(SymbolTable.prefix, "");
                String type = context.symbolTable().getType(pureName, 0, 0);

                if (type != null && type.equalsIgnoreCase("BOOLEAN")) {
                    code("movzx eax, byte [" + expr + "]");
                    code("cmp eax, 0");
                } else {
                    code("mov ax, word [" + expr + "]");
                    code("cmp ax, 0");
                }
            } else {
                code("movzx eax, byte [" + expr + "]");
                code("cmp eax, 0");
            }

            code("je " + exec);
            return;
        }

        // READ x
        if (inst.startsWith("READ ")) {
            needsReadInt = true;
            String var = inst.substring(5).trim();
            code("call _read_int");
            storeWord(var, "ax");
            return;
        }

        // WRITE "literal"
        if (inst.startsWith("WRITE ") && inst.contains("\"")) {
            needsPrintStr = true;
            String literal = inst.substring(6).trim();
            String content = literal.substring(1, literal.length() - 1);
            String lbl = "_str" + stringLitCount++;
            line(strLiterals, lbl + " db '" + content + "', 10", 1);
            line(strLiterals, lbl + "_len equ $ - " + lbl, 1);
            code("mov ecx, " + lbl);
            code("mov edx, " + lbl + "_len");
            code("call _print_str");
            return;
        }

        if (inst.matches("(?i)v_\\w+ = \".*\"")) {
            String[] p = inst.split(" = ");
            String dest = p[0].trim();
            String content = p[1].substring(1, p[1].length() - 1);

            String strLabel = "_litstr" + stringLitCount++;
            line(strLiterals, strLabel + " db '" + content + "', 10, 0", 1);

            comment(inst);
            code("mov esi, " + strLabel);
            code("mov edi, " + dest);
            String loopLabel = ".copy_" + strLabel;
            label(loopLabel + ":");
            code("mov al, byte [esi]");
            code("mov byte [edi], al");
            code("inc esi");
            code("inc edi");
            code("test al, al");
            code("jnz " + loopLabel);
            return;
        }

        // WRITE x
        if (inst.startsWith("WRITE ")) {
            String var = inst.substring(6).trim();

            if (isNumericLiteral(var) || isBoolLiteral(var)) {
                needsPrintInt = true;
                loadPrintable("eax", null, var);
                code("push eax");
                code("call _print_int");
                code("add esp, 4");
                return;
            }

            String pureName = var.replace(SymbolTable.prefix, "");
            String type = context.symbolTable().getType(pureName, 0, 0);

            if (type != null && type.equalsIgnoreCase("string")) {
                needsPrintStr = true;
                code("mov ecx, " + var);
                code("mov edx, 256");
                code("call _print_str");
            } else {
                needsPrintInt = true;
                loadPrintable("eax", type, var);
                code("push eax");
                code("call _print_int");
                code("add esp, 4");
            }
            return;
        }

        // t0 = ~t1
        if (inst.matches("\\w+ = ~\\w+")) {
            String[] p = inst.split(" = ~");
            loadByte("al", p[1]);
            code("xor al, 1");
            storeByte(p[0], "al");
            return;
        }

        // x = 5
        if (inst.matches("\\w+ = -?\\d+")) {
            String[] p = inst.split(" = ");
            storeWord(p[0], p[1]);
            return;
        }

        // x = TRUE | FALSE
        if (inst.matches("(?i)\\w+ = (TRUE|FALSE)")) {
            String[] p = inst.split(" = ");
            storeByte(p[0], boolToInt(p[1]));
            return;
        }

        // t0 = a + b | t0 = a - b
        if (inst.matches("\\w+ = \\w+ [+\\-] \\w+")) {
            String[] p = inst.split(" ");
            String op = p[3].equals("+") ? "add" : "sub";
            loadWord("ax", p[2]);
            opWord(op, p[4]);
            storeWord(p[0], "ax");
            return;
        }

        // t0 = a * b
        if (inst.matches("\\w+ = \\w+ \\* \\w+")) {
            String[] p = inst.split(" ");
            loadWord("ax", p[2]);
            mulWord(p[4]);
            storeWord(p[0], "ax");
            return;
        }

        // t0 = a / b
        if (inst.matches("\\w+ = \\w+ / \\w+")) {
            String[] p = inst.split(" ");
            loadWord("ax", p[2]);
            code("cwd");
            divWord(p[4]);
            storeWord(p[0], "ax");
            return;
        }

        // t0 = a << n
        if (inst.matches("\\w+ = \\w+ << \\d+")) {
            String[] p = inst.split(" ");
            loadWord("ax", p[2]);
            code("shl ax, " + p[4]);
            storeWord(p[0], "ax");
            return;
        }

        // t0 = a OR b | t0 = a AND b
        if (inst.matches("\\w+ = \\w+ (OR|AND) \\w+")) {
            String[] p = inst.split(" ");
            String op = p[3].equalsIgnoreCase("OR") ? "or" : "and";
            loadByte("al", p[2]);
            opByte(op, p[4]);
            storeByte(p[0], "al");
            return;
        }

        // t0 = a OPREL b → resultado BOOLEAN
        if (inst.matches("\\w+ = \\w+ (==|<>|<=|>=|<|>) \\w+")) {
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
            loadWord("ax", p[2]);
            opWord("cmp", p[4]);
            code(jmp + " " + lblTrue);
            storeByte(p[0], "0");
            code("jmp " + lblEnd);
            label(lblTrue + ":");
            storeByte(p[0], "1");
            label(lblEnd + ":");
            return;
        }

        // x = y
        if (inst.matches("v_\\w+ = \\w+")) {
            String[] p = inst.split(" = ");
            String dest = p[0].trim();
            String source = p[1].trim();

            String pureName = dest.replace(SymbolTable.prefix, "");
            String type = context.symbolTable().getType(pureName, 0, 0);

            if (type != null && type.equalsIgnoreCase("BOOLEAN")) {
                loadByte("al", source);
                storeByte(dest, "al");
            } else {
                loadWord("ax", source);
                storeWord(dest, "ax");
            }
            return;
        }

        comment("[NÃO TRADUZIDO] " + inst);
    }

    // ------------------------------------------------------------------ //
    // Rotinas auxiliares geradas sob demanda
    // ------------------------------------------------------------------ //

    private void generateHelpers() {

        // _print_int: recebe inteiro em [esp+4] (push eax antes do call)
        // converte para string ASCII e imprime via syscall write
        // registradores: EAX=acumulador, EBX=divisor(10), ECX=ponteiro, EDX=resto
        if (needsPrintInt) {
            line(code, "");
            label("_print_int:");
            code("push ebp");
            code("mov ebp, esp");
            code("mov eax, [ebp+8]");
            code("lea ecx, [_buf+11]");
            code("mov byte [ecx], 10");
            code("mov ebx, 10");
            code("test eax, eax");
            code("jns .ppos");
            code("neg eax");

            label(".ppos:");
            code("dec ecx");
            code("xor edx, edx");
            code("div ebx");
            code("add dl, '0'");
            code("mov [ecx], dl");
            code("test eax, eax");
            code("jnz .ppos");
            code("mov eax, [ebp+8]");
            code("test eax, eax");
            code("jns .pwrite");
            code("dec ecx");
            code("mov byte [ecx], '-'");

            label(".pwrite:");
            code("lea edx, [_buf+12]");
            code("sub edx, ecx");
            code("mov eax, 4");
            code("mov ebx, 1");
            code("int 0x80");
            code("pop ebp");
            code("ret");
        }

        // _print_str: ecx=endereço da string, edx=tamanho
        // syscall write(stdout=1, ecx, edx)
        if (needsPrintStr) {
            line(code, "");
            label("_print_str:");
            code("mov eax, 4");
            code("mov ebx, 1");
            code("int 0x80");
            code("ret");
        }

        // _read_int: lê até 12 bytes do stdin, converte para inteiro
        // retorna resultado em AX (word, compatível com storeWord)
        // registradores: EAX=resultado, ECX=dígito, ESI=ponteiro
        if (needsReadInt) {
            line(code, "");
            label("_read_int:");
            code("mov eax, 3");
            code("mov ebx, 0");
            code("mov ecx, _ibuf");
            code("mov edx, 12");
            code("int 0x80");
            code("mov esi, _ibuf");
            code("xor eax, eax");
            code("xor ecx, ecx");

            label(".rloop:");
            code("mov cl, [esi]");
            code("cmp cl, 10");
            code("je .rdone");
            code("cmp cl, 0");
            code("je .rdone");
            code("sub cl, '0'");
            code("imul eax, 10");
            code("add eax, ecx");
            code("inc esi");
            code("jmp .rloop");

            label(".rdone:");
            code("ret");
        }
    }
}