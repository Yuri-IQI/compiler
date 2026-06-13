package com.inm.generator.assembly;

public class HelperInstructions {
    private final Writer writer;
    private final InstructionTranslator translator;

    public HelperInstructions(Writer writer, InstructionTranslator translator) {
        this.writer = writer;
        this.translator = translator;
    }

    public void generateHelpers() {
        if (translator.needsPrintInt) generatePrintInt();
        if (translator.needsPrintStr) generatePrintStr();
        if (translator.needsReadInt) generateReadInt();
    }

    private void generatePrintInt() {
        writer.blank();
        writer.label("_print_int:");
        writer.code("push ebp");
        writer.code("mov ebp, esp");
        writer.code("mov eax, [ebp+8]");
        writer.code("lea ecx, [_buf+11]");
        writer.code("mov byte [ecx], 10");
        writer.code("mov ebx, 10");
        writer.code("test eax, eax");
        writer.code("jns .ppos");
        writer.code("neg eax");

        writer.label(".ppos:");
        writer.code("dec ecx");
        writer.code("xor edx, edx");
        writer.code("div ebx");
        writer.code("add dl, '0'");
        writer.code("mov [ecx], dl");
        writer.code("test eax, eax");
        writer.code("jnz .ppos");
        writer.code("mov eax, [ebp+8]");
        writer.code("test eax, eax");
        writer.code("jns .pwrite");
        writer.code("dec ecx");
        writer.code("mov byte [ecx], '-'");

        writer.label(".pwrite:");
        writer.code("lea edx, [_buf+12]");
        writer.code("sub edx, ecx");
        writer.code("mov eax, 4");
        writer.code("mov ebx, 1");
        writer.code("int 0x80");
        writer.code("pop ebp");
        writer.code("ret");
    }

    private void generatePrintStr() {
        writer.blank();
        writer.label("_print_str:");
        writer.code("mov eax, 4");
        writer.code("mov ebx, 1");
        writer.code("int 0x80");
        writer.code("ret");
    }

    private void generateReadInt() {
        writer.blank();
        writer.label("_read_int:");
        writer.code("mov eax, 3");
        writer.code("mov ebx, 0");
        writer.code("mov ecx, _ibuf");
        writer.code("mov edx, 12");
        writer.code("int 0x80");
        writer.code("mov esi, _ibuf");
        writer.code("xor eax, eax");
        writer.code("xor ecx, ecx");

        writer.label(".rloop:");
        writer.code("mov cl, [esi]");
        writer.code("cmp cl, 10");
        writer.code("je .rdone");
        writer.code("cmp cl, 0");
        writer.code("je .rdone");
        writer.code("sub cl, '0'");
        writer.code("imul eax, 10");
        writer.code("add eax, ecx");
        writer.code("inc esi");
        writer.code("jmp .rloop");

        writer.label(".rdone:");
        writer.code("ret");
    }
}
