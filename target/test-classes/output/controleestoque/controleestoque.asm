; ============================================
; Programa : ControleEstoque
; Alvo     : x86 32 bits (MASM, Windows i386)
; ============================================

.386
.model flat, stdcall
option casemap:none

include windows.inc
include kernel32.inc
includelib kernel32.lib

.data
	v_vendidos dw 0
	v_restante dw 0
	v_continuar db 0
	v_quantidade dw 0
	t0 dw 0
	t1 dw 0
	t2 db 0
	_buf db 14 dup(0)
	_s_true db 'true', 13, 10, 0
	_s_false db 'false', 13, 10, 0
	_nl db 13, 10
	_hOut dd ?
	_hIn dd ?
	_written dd ?
	_read dd ?

	_str0 db 'VENDIDOS', 0
	_str1 db 'RESTANTE', 0
	_str2 db 'ESTOQUE ENCERRADO', 0

.data?
	_ibuf db 12 dup(?)

.code

start:
	invoke GetStdHandle, STD_OUTPUT_HANDLE
	mov dword ptr [_hOut], eax

	invoke GetStdHandle, STD_INPUT_HANDLE
	mov dword ptr [_hIn], eax

	; v_quantidade = 20
	mov word ptr [v_quantidade], 20

	; v_vendidos = 0
	mov word ptr [v_vendidos], 0

	; v_continuar = TRUE
	mov byte ptr [v_continuar], 1

	; L0:
L0:

	; ifFalse v_continuar goto L1
	movzx eax, byte ptr [v_continuar]
	cmp eax, 0
	je L1
	cmp eax, 0
	je L1

	; t0 = v_vendidos + 2
	mov ax, word ptr [v_vendidos]
	add ax, 2
	mov word ptr [t0], ax

	; v_vendidos = t0
	mov ax, word ptr [t0]
	mov word ptr [v_vendidos], ax

	; t1 = v_quantidade - v_vendidos
	mov ax, word ptr [v_quantidade]
	sub ax, word ptr [v_vendidos]
	mov word ptr [t1], ax

	; v_restante = t1
	mov ax, word ptr [t1]
	mov word ptr [v_restante], ax

	; WRITE "VENDIDOS"
	mov ecx, offset _str0
	call _print_str

	; WRITE v_vendidos
	movsx eax, word ptr [v_vendidos]
	push eax
	call _print_int

	; WRITE "RESTANTE"
	mov ecx, offset _str1
	call _print_str

	; WRITE v_restante
	movsx eax, word ptr [v_restante]
	push eax
	call _print_int

	; t2 = v_restante <= 0
	mov ax, word ptr [v_restante]
	cmp ax, 0
	jle cmp_t_t2
	mov byte ptr [t2], 0
	jmp cmp_e_t2
cmp_t_t2:
	mov byte ptr [t2], 1
cmp_e_t2:

	; ifFalse t2 goto L2
	movzx eax, byte ptr [t2]
	cmp eax, 0
	je L2
	cmp eax, 0
	je L2

	; v_continuar = FALSE
	mov byte ptr [v_continuar], 0

	; goto L3
	jmp L3

	; L2:
L2:

	; v_continuar = TRUE
	mov byte ptr [v_continuar], 1

	; L3:
L3:

	; goto L0
	jmp L0

	; L1:
L1:

	; WRITE "ESTOQUE ENCERRADO"
	mov ecx, offset _str2
	call _print_str

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
