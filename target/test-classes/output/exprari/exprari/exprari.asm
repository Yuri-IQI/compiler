; ============================================
; Programa  : exprAri
; Gerado por: Compilador INM
; Alvo      : x86 32 bits (NASM, Linux i386)
; ============================================

bits 32

section .data
	v_x dw 0
	v_y dw 0
	v_z dw 0
	t0 dw 0
	t1 dw 0
	t2 dw 0
	_buf times 12 db 0
	_nl  db 10

section .bss
	_ibuf resb 12

section .text
global _start

_start:
	; v_x = 10
	mov word [v_x], 10

	; v_y = 3
	mov word [v_y], 3

	; t0 = 3 << 1
	mov ax, 3
	shl ax, 1
	mov word [t0], ax

	; t1 = t0 - 1
	mov ax, word [t0]
	sub ax, 1
	mov word [t1], ax

	; t2 = 10 + t1
	mov ax, 10
	add ax, word [t1]
	mov word [t2], ax

	; v_z = t2
	mov ax, word [t2]
	mov word [v_z], ax

	; WRITE v_z
	movsx eax, word [v_z]
	push eax
	call _print_int
	add esp, 4

	; encerramento: syscall exit(0)
	mov eax, 1
	xor ebx, ebx
	int 0x80

_print_int:
	push ebp
	mov ebp, esp
	mov eax, [ebp+8]
	lea ecx, [_buf+11]
	mov byte [ecx], 10
	mov ebx, 10
	test eax, eax
	jns .ppos
	neg eax
.ppos:
	dec ecx
	xor edx, edx
	div ebx
	add dl, '0'
	mov [ecx], dl
	test eax, eax
	jnz .ppos
	mov eax, [ebp+8]
	test eax, eax
	jns .pwrite
	dec ecx
	mov byte [ecx], '-'
.pwrite:
	lea edx, [_buf+12]
	sub edx, ecx
	mov eax, 4
	mov ebx, 1
	int 0x80
	pop ebp
	ret
