; ============================================
; Programa  : danglingelse
; Gerado por: Compilador INM
; Alvo      : x86 32 bits (NASM, Linux i386)
; ============================================

bits 32

section .data
	v_x dw 0
	v_y dw 0
	t0 dw 0
	t1 dw 0
	_buf times 12 db 0
	_nl  db 10
	_str0 db 'se', 10
	_str0_len equ $ - _str0
	_str1 db 'nao', 10
	_str1_len equ $ - _str1

section .bss
	_ibuf resb 12

section .text
global _start

_start:
	; v_x = 1
	mov word [v_x], 1

	; v_y = 1
	mov word [v_y], 1

	; t0 = 1 == 1
	mov ax, 1
	cmp ax, 1
	je cmp_t_t0
	mov byte [t0], 0
	jmp cmp_e_t0
cmp_t_t0:
	mov byte [t0], 1
cmp_e_t0:

	; ifFalse t0 goto L0
	movzx eax, byte [t0]
	cmp eax, 0
	je L0

	; t1 = 1 == 2
	mov ax, 1
	cmp ax, 2
	je cmp_t_t1
	mov byte [t1], 0
	jmp cmp_e_t1
cmp_t_t1:
	mov byte [t1], 1
cmp_e_t1:

	; ifFalse t1 goto L2
	movzx eax, byte [t1]
	cmp eax, 0
	je L2

	; WRITE "se"
	mov ecx, _str0
	mov edx, _str0_len
	call _print_str

	; goto L3
	jmp L3

	; L2:
L2:

	; WRITE "nao"
	mov ecx, _str1
	mov edx, _str1_len
	call _print_str

	; L3:
L3:

	; L0:
L0:

	; L1:
L1:

	; encerramento: syscall exit(0)
	mov eax, 1
	xor ebx, ebx
	int 0x80

_print_str:
	mov eax, 4
	mov ebx, 1
	int 0x80
	ret
