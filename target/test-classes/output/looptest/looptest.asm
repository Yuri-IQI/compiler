; ============================================
; Programa  : loopTest
; Gerado por: Compilador INM
; Alvo      : x86 32 bits (NASM, Linux i386)
; ============================================

bits 32

section .data
	v_x dw 0
	t0 dw 0
	t1 dw 0
	_buf times 12 db 0
	_nl  db 10

section .bss
	_ibuf resb 12

section .text
global _start

_start:
	; v_x = 0
	mov word [v_x], 0

	; L0:
L0:

	; t0 = v_x < 5
	mov ax, word [v_x]
	cmp ax, 5
	jl cmp_t_t0
	mov byte [t0], 0
	jmp cmp_e_t0
cmp_t_t0:
	mov byte [t0], 1
cmp_e_t0:

	; ifFalse t0 goto L1
	movzx eax, byte [t0]
	cmp eax, 0
	je L1

	; WRITE v_x
	movsx eax, word [v_x]
	push eax
	call _print_int
	add esp, 4

	; t1 = v_x + 1
	mov ax, word [v_x]
	add ax, 1
	mov word [t1], ax

	; v_x = t1
	mov ax, word [t1]
	mov word [v_x], ax

	; goto L0
	jmp L0

	; L1:
L1:

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
