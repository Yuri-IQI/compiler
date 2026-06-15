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
        if (translator.needsReadBool) generateReadBool();
        if (translator.needsReadStr) generateReadStr();
    }

    private void generatePrintInt() {
        writer.blank();
        writer.label("_print_int:");
        writer.code("push ebp");
        writer.code("mov ebp, esp");
        writer.code("push ebx");
        writer.code("push esi");
        writer.code("movsx eax, word ptr [ebp+8]");

        writer.code("lea ecx, [_buf+12]");
        writer.code("mov byte ptr [ecx], 13");
        writer.code("inc ecx");
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
        writer.code("mov edx, 6");
        writer.code("jmp pb_write");

        writer.label("pb_false:");
        writer.code("mov ecx, offset _s_false");
        writer.code("mov edx, 7");

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

        writer.code("mov esi, ecx");
        writer.code("xor edx, edx");

        writer.label("ps_len:");
        writer.code("cmp byte ptr [esi+edx], 0");
        writer.code("je ps_write");
        writer.code("inc edx");
        writer.code("jmp ps_len");

        writer.label("ps_write:");
        writer.code("invoke WriteFile, dword ptr [_hOut], ecx, edx, addr _written, 0");
        writer.code("invoke WriteFile, dword ptr [_hOut], offset _nl, 2, addr _written, 0");

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
        writer.code("invoke ReadFile, dword ptr [_hIn], addr _ibuf, 12, addr _read, 0");
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
        writer.code("jz ri_validate_pos");
        writer.code("neg eax");

        writer.label("ri_validate_neg:");
        writer.code("cmp eax, -32767");
        writer.code("jl ri_overflow");
        writer.code("jmp ri_success");

        writer.label("ri_validate_pos:");
        writer.code("cmp eax, 32767");
        writer.code("jg ri_overflow");

        writer.label("ri_success:");
        writer.code("jmp ri_pos");

        writer.code("ri_overflow:");
        writer.code("xor eax, eax");

        writer.label("ri_pos:");
        writer.code("pop ebx");
        writer.code("pop esi");
        writer.code("pop ebp");
        writer.code("ret");
    }

    private void generateReadBool() {
        writer.blank();
        writer.label("_read_bool:");
        writer.code("push ebp");
        writer.code("mov ebp, esp");
        writer.code("push esi");
        writer.code("invoke ReadFile, dword ptr [_hIn], addr _ibuf, 6, addr _read, 0");
        writer.code("mov esi, offset _ibuf");
        writer.code("mov al, byte ptr [esi]");
        writer.code("or al, 20h");
        writer.code("cmp al, 't'");
        writer.code("je rb_true");
        writer.code("xor al, al");
        writer.code("jmp rb_done");

        writer.label("rb_true:");
        writer.code("mov al, 1");

        writer.label("rb_done:");
        writer.code("pop esi");
        writer.code("pop ebp");
        writer.code("ret");
    }

    private void generateReadStr() {
        writer.blank();
        writer.label("_read_str:");
        writer.code("push ebp");
        writer.code("mov ebp, esp");
        writer.code("push esi");
        writer.code("push edi");
        writer.code("push ebx");
        writer.code("mov edi, ecx");
        writer.code("invoke ReadFile, dword ptr [_hIn], edi, 255, addr _read, 0");
        writer.code("mov esi, edi");
        writer.code("xor ebx, ebx");

        writer.label("rs_scan:");
        writer.code("mov al, byte ptr [esi+ebx]");
        writer.code("cmp al, 13");
        writer.code("je rs_trim");
        writer.code("cmp al, 10");
        writer.code("je rs_trim");
        writer.code("cmp al, 0");
        writer.code("je rs_done");
        writer.code("inc ebx");
        writer.code("jmp rs_scan");

        writer.label("rs_trim:");
        writer.code("mov byte ptr [esi+ebx], 0");

        writer.label("rs_done:");
        writer.code("pop ebx");
        writer.code("pop edi");
        writer.code("pop esi");
        writer.code("pop ebp");
        writer.code("ret");
    }
}
