; ============================================
; Programa  : condicional
; Gerado por: Compilador INM
; Alvo      : x86 32 bits (NASM, Linux i386)
; ============================================

bits 32

section .data
	v_x dw 0
	t0 dw 0
	_buf times 12 db 0
	_nl  db 10
	_str0 db 'maior', 10
	_str0_len equ $ - _str0
	_str1 db 'dois', 10
	_str1_len equ $ - _str1
	_str2 db 'menor', 10
	_str2_len equ $ - _str2

section .bss
	_ibuf resb 12

section .text
global _start

_start:
	; v_x = 5
	mov word [v_x], 5

	; t0 = 5 > 3
	mov ax, 5
	cmp ax, 3
	jg cmp_t_t0
	mov byte [t0], 0
	jmp cmp_e_t0
cmp_t_t0:
	mov byte [t0], 1
cmp_e_t0:

	; ifFalse t0 goto L0
	movzx eax, byte [t0]
	cmp eax, 0
	je L0

	; WRITE "maior"
	mov ecx, _str0
	mov edx, _str0_len
	call _print_str

	; WRITE "dois"
	mov ecx, _str1
	mov edx, _str1_len
	call _print_str

	; goto L1
	jmp L1

	; L0:
L0:

	; WRITE "menor"
	mov ecx, _str2
	mov edx, _str2_len
	call _print_str

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
