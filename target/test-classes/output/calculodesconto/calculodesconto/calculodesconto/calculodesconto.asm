; ============================================
; Programa  : calculoDesconto
; Gerado por: Compilador INM
; Alvo      : x86 32 bits (NASM, Linux i386)
; ============================================

bits 32

section .data
	v_preco dw 0
	v_resultado dw 0
	t0 dw 0
	t1 dw 0
	_buf times 12 db 0
	_nl  db 10
	_str0 db 'Informe o Preço', 10
	_str0_len equ $ - _str0

section .bss
	_ibuf resb 12

section .text
global _start

_start:
	; t0 = 2
	mov word [t0], 2

	; WRITE 2
	mov eax, 2
	push eax
	call _print_int
	add esp, 4

	; WRITE "Informe o Preço"
	mov ecx, _str0
	mov edx, _str0_len
	call _print_str

	; READ v_preco
	call _read_int
	mov word [v_preco], ax

	; WRITE v_preco
	movsx eax, word [v_preco]
	push eax
	call _print_int
	add esp, 4

	; t1 = v_preco - 15
	mov ax, word [v_preco]
	sub ax, 15
	mov word [t1], ax

	; v_resultado = t1
	mov ax, word [t1]
	mov word [v_resultado], ax

	; WRITE v_resultado
	movsx eax, word [v_resultado]
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

_print_str:
	mov eax, 4
	mov ebx, 1
	int 0x80
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
