; ============================================
; Programa  : variaveis
; Gerado por: Compilador INM
; Alvo      : x86 32 bits (NASM, Linux i386)
; ============================================

bits 32

section .data
	v_x dw 0
	v_y dw 0
	v_z times 256 db 0
	_buf times 12 db 0
	_nl  db 10

section .bss
	_ibuf resb 12

section .text
global _start

_start:
	; WRITE v_x
	movsx eax, word [v_x]
	push eax
	call _print_int
	add esp, 4

	; WRITE v_y
	movsx eax, word [v_y]
	push eax
	call _print_int
	add esp, 4

	; READ v_z
	call _read_int
	mov word [v_z], ax

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

_read_int:
	mov eax, 3
	mov ebx, 0
	mov ecx, _ibuf
	mov edx, 12
	int 0x80
	mov esi, _ibuf
	xor eax, eax
	xor ecx, ecx
.rloop:
	mov cl, [esi]
	cmp cl, 10
	je .rdone
	cmp cl, 0
	je .rdone
	sub cl, '0'
	imul eax, 10
	add eax, ecx
	inc esi
	jmp .rloop
.rdone:
	ret
