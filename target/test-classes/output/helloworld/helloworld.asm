; ============================================
; Programa : helloWorld
; Alvo     : x86 32 bits (MASM, Windows i386)
; ============================================

.386
.model flat, stdcall
option casemap:none

include windows.inc
include kernel32.inc
includelib kernel32.lib

.data
	v_name db 256 dup(0)
	t0 db 256 dup(0)
	_buf db 14 dup(0)
	_s_true db 'true', 13, 10, 0
	_s_false db 'false', 13, 10, 0
	_nl db 13, 10
	_hOut dd ?
	_hIn dd ?
	_written dd ?
	_read dd ?

	_str0 db 'Qual o seu nome?', 0
	_lit_concat_1 db 'Ola, ', 0

.data?
	_ibuf db 12 dup(?)

.code

start:
	invoke GetStdHandle, STD_OUTPUT_HANDLE
	mov dword ptr [_hOut], eax

	invoke GetStdHandle, STD_INPUT_HANDLE
	mov dword ptr [_hIn], eax

	; WRITE "Qual o seu nome?"
	mov ecx, offset _str0
	call _print_str

	; READ_STRING v_name
	lea ecx, v_name
	call _read_str

	; t0 = "Ola, " CONCAT v_name
	lea esi, _lit_concat_1
	lea edi, t0
concat1_2:
	mov al, [esi]
	test al, al
	jz concat2_2
	mov [edi], al
	inc esi
	inc edi
	jmp concat1_2
concat2_2:
	lea esi, v_name
concat2_body_3:
	mov al, [esi]
	mov [edi], al
	inc esi
	inc edi
	test al, al
	jnz concat2_body_3

	; WRITE t0
	mov ecx, offset t0
	call _print_str

	invoke Sleep, 50
	invoke ExitProcess, 0

_print_str:
	push ebp
	mov ebp, esp
	push esi
	mov esi, ecx
	xor edx, edx
ps_len:
	cmp byte ptr [esi+edx], 0
	je ps_write
	inc edx
	jmp ps_len
ps_write:
	invoke WriteFile, dword ptr [_hOut], ecx, edx, addr _written, 0
	invoke WriteFile, dword ptr [_hOut], offset _nl, 2, addr _written, 0
	pop esi
	pop ebp
	ret

_read_str:
	push ebp
	mov ebp, esp
	push esi
	push edi
	push ebx
	mov edi, ecx
	invoke ReadFile, dword ptr [_hIn], edi, 255, addr _read, 0
	mov esi, edi
	xor ebx, ebx
rs_scan:
	mov al, byte ptr [esi+ebx]
	cmp al, 13
	je rs_trim
	cmp al, 10
	je rs_trim
	cmp al, 0
	je rs_done
	inc ebx
	jmp rs_scan
rs_trim:
	mov byte ptr [esi+ebx], 0
rs_done:
	pop ebx
	pop edi
	pop esi
	pop ebp
	ret

end start
