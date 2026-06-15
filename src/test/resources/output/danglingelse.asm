; ===========================================
; Programa : danglingelse
; Alvo     : x86 32 bits (MASM, Windows i386)
; ===========================================

.386
.model flat, stdcall
option casemap:none

include windows.inc
include kernel32.inc
includelib kernel32.lib

.data
	v_x dw 0
	v_y dw 0
	t0 db 0
	t1 db 0
	_buf db 14 dup(0)
	_s_true db 'true', 13, 10, 0
	_s_false db 'false', 13, 10, 0
	_nl db 13, 10
	_hOut dd ?
	_hIn dd ?
	_written dd ?
	_read dd ?

	_str0 db 'se', 0
	_str1 db 'nao', 0

.data?
	_ibuf db 12 dup(?)

.code

start:
	invoke GetStdHandle, STD_OUTPUT_HANDLE
	mov dword ptr [_hOut], eax

	invoke GetStdHandle, STD_INPUT_HANDLE
	mov dword ptr [_hIn], eax

	; v_x = 1
	mov word ptr [v_x], 1

	; v_y = 1
	mov word ptr [v_y], 1

	; t0 = TRUE
	mov byte ptr [t0], 1

	; ifFalse t0 goto L0
	movzx eax, byte ptr [t0]
	cmp eax, 0
	je L0

	; t1 = FALSE
	mov byte ptr [t1], 0

	; ifFalse t1 goto L2
	movzx eax, byte ptr [t1]
	cmp eax, 0
	je L2

	; WRITE "se"
	mov ecx, offset _str0
	call _print_str

	; goto L3
	jmp L3

	; L2:
L2:

	; WRITE "nao"
	mov ecx, offset _str1
	call _print_str

	; L3:
L3:

	; L0:
L0:

	; L1:
L1:

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

end start
