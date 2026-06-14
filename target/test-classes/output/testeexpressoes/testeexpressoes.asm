; ============================================
; Programa : TesteExpressoes
; Alvo     : x86 32 bits (MASM, Windows i386)
; ============================================

.386
.model flat, stdcall
option casemap:none

include windows.inc
include kernel32.inc
includelib kernel32.lib

.data
	v_r2 dw 0
	v_texto db 256 dup(0)
	v_a dw 0
	v_b dw 0
	v_c dw 0
	v_d dw 0
	v_comp2 db 0
	v_comp1 db 0
	v_final db 0
	v_r1 dw 0
	t0 dw 0
	t1 dw 0
	t2 dw 0
	t3 dw 0
	t4 dw 0
	t5 dw 0
	t6 dw 0
	t7 dw 0
	t8 db 0
	t9 db 0
	t10 db 0
	t11 db 0
	t12 db 0
	_buf db 14 dup(0)
	_s_true db 'true', 13, 10, 0
	_s_false db 'false', 13, 10, 0
	_nl db 13, 10
	_hOut dd ?
	_hIn dd ?
	_written dd ?
	_read dd ?

	_lit_0 db 'VERDADEIRO', 0
	_lit_1 db 'FALSO', 0

.data?
	_ibuf db 12 dup(?)

.code

start:
	invoke GetStdHandle, STD_OUTPUT_HANDLE
	mov dword ptr [_hOut], eax

	invoke GetStdHandle, STD_INPUT_HANDLE
	mov dword ptr [_hIn], eax

	; v_a = 10
	mov word ptr [v_a], 10

	; v_b = 5
	mov word ptr [v_b], 5

	; v_c = 2
	mov word ptr [v_c], 2

	; v_d = 8
	mov word ptr [v_d], 8

	; t0 = 10 + 5
	mov ax, 10
	add ax, 5
	mov word ptr [t0], ax

	; t1 = 2 + 8
	mov ax, 2
	add ax, 8
	mov word ptr [t1], ax

	; t2 = t0 * t1
	mov ax, word ptr [t0]
	imul word ptr [t1]
	mov word ptr [t2], ax

	; v_r1 = t2
	mov ax, word ptr [t2]
	mov word ptr [v_r1], ax

	; t3 = 10 * 5
	mov ax, 10
	imul ax, ax, 5
	mov word ptr [t3], ax

	; t4 = 8 / 2
	mov ax, 8
	cwd
	mov bx, 2
	idiv bx
	mov word ptr [t4], ax

	; t5 = 10 - 2
	mov ax, 10
	sub ax, 2
	mov word ptr [t5], ax

	; t6 = t4 + t5
	mov ax, word ptr [t4]
	add ax, word ptr [t5]
	mov word ptr [t6], ax

	; t7 = t3 - t6
	mov ax, word ptr [t3]
	sub ax, word ptr [t6]
	mov word ptr [t7], ax

	; v_r2 = t7
	mov ax, word ptr [t7]
	mov word ptr [v_r2], ax

	; t8 = v_r1 > v_r2
	mov ax, word ptr [v_r1]
	cmp ax, word ptr [v_r2]
	jg cmp_t_t8
	mov byte ptr [t8], 0
	jmp cmp_e_t8
cmp_t_t8:
	mov byte ptr [t8], 1
cmp_e_t8:

	; v_comp1 = t8
	mov al, byte ptr [t8]
	mov byte ptr [v_comp1], al

	; t9 = 10 >= 5
	mov ax, 10
	cmp ax, 5
	jge cmp_t_t9
	mov byte ptr [t9], 0
	jmp cmp_e_t9
cmp_t_t9:
	mov byte ptr [t9], 1
cmp_e_t9:

	; t10 = 8 <> 2
	mov ax, 8
	cmp ax, 2
	jne cmp_t_t10
	mov byte ptr [t10], 0
	jmp cmp_e_t10
cmp_t_t10:
	mov byte ptr [t10], 1
cmp_e_t10:

	; t11 = t9 AND t10
	mov al, byte ptr [t9]
	and al, byte ptr [t10]
	mov byte ptr [t11], al

	; v_comp2 = t11
	mov al, byte ptr [t11]
	mov byte ptr [v_comp2], al

	; t12 = v_comp1 OR v_comp2
	mov al, byte ptr [v_comp1]
	or al, byte ptr [v_comp2]
	mov byte ptr [t12], al

	; v_final = t12
	mov al, byte ptr [t12]
	mov byte ptr [v_final], al

	; WRITE v_r1
	movsx eax, word ptr [v_r1]
	push eax
	call _print_int

	; WRITE v_r2
	movsx eax, word ptr [v_r2]
	push eax
	call _print_int

	; WRITE v_comp1
	movzx eax, byte ptr [v_comp1]
	push eax
	call _print_bool

	; WRITE v_comp2
	movzx eax, byte ptr [v_comp2]
	push eax
	call _print_bool

	; WRITE v_final
	movzx eax, byte ptr [v_final]
	push eax
	call _print_bool

	; ifFalse v_final goto L0
	movzx eax, byte ptr [v_final]
	cmp eax, 0
	je L0
	cmp eax, 0
	je L0

	; v_texto = "VERDADEIRO"
	lea esi, _lit_0
	lea edi, v_texto
copy_1:
	mov al, [esi]
	mov [edi], al
	inc esi
	inc edi
	test al, al
	jnz copy_1

	; WRITE v_texto
	mov ecx, offset v_texto
	call _print_str

	; goto L1
	jmp L1

	; L0:
L0:

	; v_texto = "FALSO"
	lea esi, _lit_1
	lea edi, v_texto
copy_2:
	mov al, [esi]
	mov [edi], al
	inc esi
	inc edi
	test al, al
	jnz copy_2

	; WRITE v_texto
	mov ecx, offset v_texto
	call _print_str

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

_print_bool:
	push ebp
	mov ebp, esp
	movzx eax, byte ptr [ebp+8]
	test eax, eax
	jz pb_false
	mov ecx, offset _s_true
	mov edx, 6
	jmp pb_write
pb_false:
	mov ecx, offset _s_false
	mov edx, 7
pb_write:
	invoke WriteFile, dword ptr [_hOut], ecx, edx, addr _written, 0
	pop ebp
	ret 4

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
