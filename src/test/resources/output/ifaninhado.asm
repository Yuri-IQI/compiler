; ============================================
; Programa  : ifaninhado
; Gerado por: Compilador INM
; Alvo      : x86 32 bits (NASM, Linux i386)
; ============================================

bits 32

section .data
	t0 dw 0
	t1 dw 0
	_buf times 12 db 0
	_nl  db 10
	_str0 db 'teste7', 10
	_str0_len equ $ - _str0
	_str1 db 'teste5', 10
	_str1_len equ $ - _str1
	_str2 db 'teste3', 10
	_str2_len equ $ - _str2
	_str3 db 'teste1', 10
	_str3_len equ $ - _str3
	_str4 db 'teste2', 10
	_str4_len equ $ - _str4
	_str5 db 'teste4', 10
	_str5_len equ $ - _str5
	_str6 db 'teste6', 10
	_str6_len equ $ - _str6
	_str7 db 'teste8', 10
	_str7_len equ $ - _str7

section .bss
	_ibuf resb 12

section .text
global _start

_start:
	; WRITE "teste7"
	mov ecx, _str0
	mov edx, _str0_len
	call _print_str

	; ifFalse TRUE goto L0
	mov eax, 1
	cmp eax, 0
	je L0

	; WRITE "teste5"
	mov ecx, _str1
	mov edx, _str1_len
	call _print_str

	; t0 = 4 > 2
	mov ax, 4
	cmp ax, 2
	jg cmp_t_t0
	mov byte [t0], 0
	jmp cmp_e_t0
cmp_t_t0:
	mov byte [t0], 1
cmp_e_t0:

	; ifFalse t0 goto L2
	movzx eax, byte [t0]
	cmp eax, 0
	je L2

	; WRITE "teste3"
	mov ecx, _str2
	mov edx, _str2_len
	call _print_str

	; t1 = ~FALSE
	mov al, 0
	xor al, 1
	mov byte [t1], al

	; ifFalse t1 goto L4
	movzx eax, byte [t1]
	cmp eax, 0
	je L4

	; WRITE "teste1"
	mov ecx, _str3
	mov edx, _str3_len
	call _print_str

	; WRITE "teste2"
	mov ecx, _str4
	mov edx, _str4_len
	call _print_str

	; L4:
L4:

	; L5:
L5:

	; WRITE "teste4"
	mov ecx, _str5
	mov edx, _str5_len
	call _print_str

	; L2:
L2:

	; L3:
L3:

	; WRITE "teste6"
	mov ecx, _str6
	mov edx, _str6_len
	call _print_str

	; L0:
L0:

	; L1:
L1:

	; WRITE "teste8"
	mov ecx, _str7
	mov edx, _str7_len
	call _print_str

	; encerramento: syscall exit(0)
	mov eax, 1
	xor ebx, ebx
	int 0x80

_print_str:
	mov eax, 4
	mov ebx, 1
	int 0x80
	ret
