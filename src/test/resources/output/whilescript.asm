; ============================================
; Programa : whilescript
; Alvo     : x86 32 bits (MASM, Windows i386)
; ============================================

.386
.model flat, stdcall
option casemap:none

include windows.inc
include kernel32.inc
includelib kernel32.lib

.data
	v_id dw 0
	t0 db 0
	t1 dw 0
	_buf db 14 dup(0)
	_s_true db 'true', 13, 10, 0
	_s_false db 'false', 13, 10, 0
	_nl db 13, 10
	_hOut dd ?
	_hIn dd ?
	_written dd ?
	_read dd ?


.data?
	_ibuf db 12 dup(?)

.code

start:
	invoke GetStdHandle, STD_OUTPUT_HANDLE
	mov dword ptr [_hOut], eax

	invoke GetStdHandle, STD_INPUT_HANDLE
	mov dword ptr [_hIn], eax

	; v_id = 0
	mov word ptr [v_id], 0

	; L0:
L0:

	; t0 = v_id < 10
	mov ax, word ptr [v_id]
	cmp ax, 10
	jl cmp_t_t0
	mov byte ptr [t0], 0
	jmp cmp_e_t0
cmp_t_t0:
	mov byte ptr [t0], 1
cmp_e_t0:

	; ifFalse t0 goto L1
	movzx eax, byte ptr [t0]
	cmp eax, 0
	je L1
	cmp eax, 0
	je L1

	; t1 = v_id + 1
	mov ax, word ptr [v_id]
	add ax, 1
	mov word ptr [t1], ax

	; v_id = t1
	mov ax, word ptr [t1]
	mov word ptr [v_id], ax

	; WRITE v_id
	movsx eax, word ptr [v_id]
	push eax
	call _print_int

	; goto L0
	jmp L0

	; L1:
L1:

	invoke Sleep, 50
	invoke ExitProcess, 0

_print_int:
	push ebp
	mov ebp, esp
	push ebx
	push esi
	movsx eax, word ptr [ebp+8]
	lea ecx, [_buf+12]
	mov byte ptr [ecx], 13
	inc ecx
	mov byte ptr [ecx], 10
	dec ecx
	mov esi, eax
	test eax, eax
	jns pi_pos
	neg eax
pi_pos:
	mov ebx, 10
pi_loop:
	xor edx, edx
	div ebx
	add dl, '0'
	mov byte ptr [ecx], dl
	dec ecx
	test eax, eax
	jnz pi_loop
	test esi, esi
	jns pi_write
	mov byte ptr [ecx], '-'
	dec ecx
pi_write:
	inc ecx
	lea edx, [_buf+14]
	sub edx, ecx
	invoke WriteFile, dword ptr [_hOut], ecx, edx, addr _written, 0
	pop esi
	pop ebx
	pop ebp
	ret 4

end start
