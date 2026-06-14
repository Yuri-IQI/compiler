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
        if (translator.needsPrintBool) generatePrintBool();
        if (translator.needsPrintStr) generatePrintStr();
        if (translator.needsReadInt) generateReadInt();
    }

    private void generatePrintInt() {
        writer.blank();
        writer.label("_print_int:");
        writer.code("push ebp");
        writer.code("mov ebp, esp");
        writer.code("push ebx");
        writer.code("push esi");
        writer.code("movsx eax, word ptr [ebp+8]");

        writer.code("lea ecx, [_buf+13]");
        writer.code("mov byte ptr [ecx], 10");
        writer.code("dec ecx");

        writer.code("mov esi, eax");
        writer.code("test eax, eax");
        writer.code("jns pi_pos");
        writer.code("neg eax");

        writer.label("pi_pos:");
        writer.code("mov ebx, 10");

        writer.label("pi_loop:");
        writer.code("xor edx, edx");
        writer.code("div ebx");
        writer.code("add dl, '0'");
        writer.code("mov byte ptr [ecx], dl");
        writer.code("dec ecx");
        writer.code("test eax, eax");
        writer.code("jnz pi_loop");

        writer.code("test esi, esi");
        writer.code("jns pi_write");
        writer.code("mov byte ptr [ecx], '-'");
        writer.code("dec ecx");

        writer.label("pi_write:");
        writer.code("inc ecx");
        writer.code("lea edx, [_buf+14]");
        writer.code("sub edx, ecx");
        writer.code("invoke WriteFile, dword ptr [_hOut], ecx, edx, addr _written, 0");

        writer.code("pop esi");
        writer.code("pop ebx");
        writer.code("pop ebp");
        writer.code("ret 4");
    }

    private void generatePrintBool() {
        writer.blank();
        writer.label("_print_bool:");
        writer.code("push ebp");
        writer.code("mov ebp, esp");

        writer.code("movzx eax, byte ptr [ebp+8]");
        writer.code("test eax, eax");
        writer.code("jz pb_false");

        writer.code("mov ecx, offset _s_true");
        writer.code("mov edx, 5");
        writer.code("jmp pb_write");

        writer.label("pb_false:");
        writer.code("mov ecx, offset _s_false");
        writer.code("mov edx, 6");

        writer.label("pb_write:");
        writer.code("invoke WriteFile, dword ptr [_hOut], ecx, edx, addr _written, 0");

        writer.code("pop ebp");
        writer.code("ret 4");
    }

    private void generatePrintStr() {
        writer.blank();
        writer.label("_print_str:");
        writer.code("push ebp");
        writer.code("mov ebp, esp");
        writer.code("push esi");
        writer.code("push edi");
        writer.code("mov esi, ecx");

        writer.code("mov edi, ecx");
        writer.code("xor eax, eax");
        writer.code("mov ecx, 256");
        writer.code("repne scasb");

        writer.code("mov eax, 256");
        writer.code("sub eax, ecx");
        writer.code("dec eax");

        writer.code("invoke WriteFile, dword ptr [_hOut], esi, eax, addr _written, 0");

        writer.code("pop edi");
        writer.code("pop esi");
        writer.code("pop ebp");
        writer.code("ret");
    }

    private void generateReadInt() {
        writer.blank();
        writer.label("_read_int:");
        writer.code("push ebp");
        writer.code("mov ebp, esp");
        writer.code("push esi");
        writer.code("push ebx");

        writer.code("invoke ReadConsoleA, dword ptr [_hIn], addr _ibuf, 12, addr _read, 0");
        writer.code("mov esi, offset _ibuf");
        writer.code("xor eax, eax");
        writer.code("xor ebx, ebx");

        writer.code("mov cl, byte ptr [esi]");
        writer.code("cmp cl, '-'");
        writer.code("jne ri_loop");
        writer.code("mov ebx, 1");
        writer.code("inc esi");

        writer.label("ri_loop:");
        writer.code("mov cl, byte ptr [esi]");

        writer.code("cmp cl, '0'");
        writer.code("jl ri_done");
        writer.code("cmp cl, '9'");
        writer.code("jg ri_done");

        writer.code("sub cl, '0'");
        writer.code("imul eax, eax, 10");
        writer.code("movzx ecx, cl");
        writer.code("add eax, ecx");
        writer.code("inc esi");
        writer.code("jmp ri_loop");

        writer.label("ri_done:");
        writer.code("test ebx, ebx");
        writer.code("jz ri_pos");
        writer.code("neg eax");

        writer.label("ri_pos:");
        writer.code("pop ebx");
        writer.code("pop esi");
        writer.code("pop ebp");
        writer.code("ret");
    }
}
