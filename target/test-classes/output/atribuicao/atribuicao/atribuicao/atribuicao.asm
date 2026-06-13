; ============================================
; Programa  : atribuicao
; Gerado por: Compilador INM
; Alvo      : x86 32 bits (NASM, Linux i386)
; ============================================

bits 32

section .data
	v_variavel dw 0
	_buf times 12 db 0
	_nl  db 10

section .bss
	_ibuf resb 12

section .text
global _start

_start:
	; v_variavel = 2
	mov word [v_variavel], 2

	; WRITE 2
	mov eax, 2
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
