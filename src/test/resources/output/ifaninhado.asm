; ============================================
; Programa : ifaninhado
; Alvo     : x86 32 bits (MASM, Windows i386)
; ============================================

.386
.model flat, stdcall
option casemap:none

include windows.inc
include kernel32.inc
includelib kernel32.lib

.data
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

	_str0 db 'teste7', 0
	_str1 db 'teste5', 0
	_str2 db 'teste3', 0
	_str3 db 'teste1', 0
	_str4 db 'teste2', 0
	_str5 db 'teste4', 0
	_str6 db 'teste6', 0
	_str7 db 'teste8', 0

.data?
	_ibuf db 12 dup(?)

.code

start:
	invoke GetStdHandle, STD_OUTPUT_HANDLE
	mov dword ptr [_hOut], eax

	invoke GetStdHandle, STD_INPUT_HANDLE
	mov dword ptr [_hIn], eax

	; WRITE "teste7"
	mov ecx, offset _str0
	call _print_str

	; ifFalse TRUE goto L0
	mov eax, 1
	cmp eax, 0
	je L0

	; WRITE "teste5"
	mov ecx, offset _str1
	call _print_str

	; t0 = 4 > 2
	mov ax, 4
	cmp ax, 2
	jg cmp_t_t0
	mov byte ptr [t0], 0
	jmp cmp_e_t0
cmp_t_t0:
	mov byte ptr [t0], 1
cmp_e_t0:

	; ifFalse t0 goto L2
	movzx eax, byte ptr [t0]
	cmp eax, 0
	je L2
	cmp eax, 0
	je L2

	; WRITE "teste3"
	mov ecx, offset _str2
	call _print_str

	; t1 = ~FALSE
	mov al, 0
	xor al, 1
	mov byte ptr [t1], al

	; ifFalse t1 goto L4
	movzx eax, byte ptr [t1]
	cmp eax, 0
	je L4
	cmp eax, 0
	je L4

	; WRITE "teste1"
	mov ecx, offset _str3
	call _print_str

	; WRITE "teste2"
	mov ecx, offset _str4
	call _print_str

	; L4:
L4:

	; L5:
L5:

	; WRITE "teste4"
	mov ecx, offset _str5
	call _print_str

	; L2:
L2:

	; L3:
L3:

	; WRITE "teste6"
	mov ecx, offset _str6
	call _print_str

	; L0:
L0:

	; L1:
L1:

	; WRITE "teste8"
	mov ecx, offset _str7
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

end start
