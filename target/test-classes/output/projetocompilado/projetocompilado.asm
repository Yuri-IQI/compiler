; ============================================
; Programa  : ProjetoCompilado
; Gerado por: Compilador INM
; Alvo      : x86 32 bits (NASM, Linux i386)
; ============================================

bits 32

section .data
	v_nota1 dw 0
	v_resultado times 256 db 0
	v_aprovado db 0
	v_nota2 dw 0
	v_media dw 0
	t0 dw 0
	t1 dw 0
	t2 dw 0
	_buf times 12 db 0
	_nl  db 10
	_str0 db 'INFORME A PRIMEIRA NOTA', 10
	_str0_len equ $ - _str0
	_str1 db 'INFORME A SEGUNDA NOTA', 10
	_str1_len equ $ - _str1

section .bss
	_ibuf resb 12

section .text
global _start

_start:
	; WRITE "INFORME A PRIMEIRA NOTA"
	mov ecx, _str0
	mov edx, _str0_len
	call _print_str

	; READ v_nota1
	call _read_int
	mov word [v_nota1], ax

	; WRITE "INFORME A SEGUNDA NOTA"
	mov ecx, _str1
	mov edx, _str1_len
	call _print_str

	; READ v_nota2
	call _read_int
	mov word [v_nota2], ax

	; t0 = v_nota1 + v_nota2
	mov ax, word [v_nota1]
	add ax, word [v_nota2]
	mov word [t0], ax

	; t1 = t0 / 2
	mov ax, word [t0]
	cwd
	mov bx, 2
	idiv bx
	mov word [t1], ax

	; v_media = t1
	mov ax, word [t1]
	mov word [v_media], ax

	; WRITE v_media
	movsx eax, word [v_media]
	push eax
	call _print_int
	add esp, 4

	; t2 = v_media >= 7
	mov ax, word [v_media]
	cmp ax, 7
	jge cmp_t_t2
	mov byte [t2], 0
	jmp cmp_e_t2
cmp_t_t2:
	mov byte [t2], 1
cmp_e_t2:

	; v_aprovado = t2
	mov al, byte [t2]
	mov byte [v_aprovado], al

	; WRITE v_aprovado
	movzx eax, byte [v_aprovado]
	push eax
	call _print_int
	add esp, 4

	; ifFalse v_aprovado goto L0
	movzx eax, byte [v_aprovado]
	cmp eax, 0
	je L0

	; v_resultado = "Aprovado"
	; [NÃO TRADUZIDO] v_resultado = "Aprovado"

	; WRITE v_resultado
	mov ecx, v_resultado
	mov edx, 256
	call _print_str

	; goto L1
	jmp L1

	; L0:
L0:

	; v_resultado = "Reprovado"
	; [NÃO TRADUZIDO] v_resultado = "Reprovado"

	; WRITE v_resultado
	mov ecx, v_resultado
	mov edx, 256
	call _print_str

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
