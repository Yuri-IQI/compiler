; ============================================
; Programa  : EscreveTeste
; Gerado por: Compilador INM
; Alvo      : x86 32 bits (NASM, Linux i386)
; ============================================

bits 32

section .data
	_buf times 12 db 0
	_nl  db 10
	_str0 db 'teste', 10
	_str0_len equ $ - _str0

section .bss
	_ibuf resb 12

section .text
global _start

_start:
	; WRITE "teste"
	mov ecx, _str0
	mov edx, _str0_len
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
