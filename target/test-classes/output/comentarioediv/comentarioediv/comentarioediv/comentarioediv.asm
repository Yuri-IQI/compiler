; ============================================
; Programa  : comentarioEDiv
; Gerado por: Compilador INM
; Alvo      : x86 32 bits (NASM, Linux i386)
; ============================================

bits 32

section .data
	v_div dw 0
	t0 dw 0
	_buf times 12 db 0
	_nl  db 10

section .bss
	_ibuf resb 12

section .text
global _start

_start:
	; t0 = 1
	mov word [t0], 1

	; v_div = 1
	mov word [v_div], 1

	; encerramento: syscall exit(0)
	mov eax, 1
	xor ebx, ebx
	int 0x80
