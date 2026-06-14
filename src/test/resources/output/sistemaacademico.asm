; ============================================
; Programa : SistemaAcademico
; Alvo     : x86 32 bits (MASM, Windows i386)
; ============================================

.386
.model flat, stdcall
option casemap:none

include windows.inc
include kernel32.inc
includelib kernel32.lib

.data
	v_nota1 dw 0
	v_situacao db 256 dup(0)
	v_aprovado db 0
	v_nota2 dw 0
	v_nota3 dw 0
	v_frequenciaok db 0
	v_resultadofinal db 0
	v_media dw 0
	t0 dw 0
	t1 dw 0
	t2 dw 0
	t3 db 0
	t4 db 0
	t5 db 0
	t6 db 0
	_buf db 14 dup(0)
	_s_true db 'true', 13, 10, 0
	_s_false db 'false', 13, 10, 0
	_nl db 13, 10
	_hOut dd ?
	_hIn dd ?
	_written dd ?
	_read dd ?

	_str0 db 'DIGITE A PRIMEIRA NOTA', 0
	_str1 db 'DIGITE A SEGUNDA NOTA', 0
	_str2 db 'DIGITE A TERCEIRA NOTA', 0
	_str3 db 'MEDIA CALCULADA', 0
	_lit_4 db 'APROVADO', 0
	_lit_5 db 'REPROVADO', 0

.data?
	_ibuf db 12 dup(?)

.code

start:
	invoke GetStdHandle, STD_OUTPUT_HANDLE
	mov dword ptr [_hOut], eax

	invoke GetStdHandle, STD_INPUT_HANDLE
	mov dword ptr [_hIn], eax

	; WRITE "DIGITE A PRIMEIRA NOTA"
	mov ecx, offset _str0
	call _print_str

	; READ_INTEGER v_nota1
	call _read_int
	mov word ptr [v_nota1], ax

	; WRITE "DIGITE A SEGUNDA NOTA"
	mov ecx, offset _str1
	call _print_str

	; READ_INTEGER v_nota2
	call _read_int
	mov word ptr [v_nota2], ax

	; WRITE "DIGITE A TERCEIRA NOTA"
	mov ecx, offset _str2
	call _print_str

	; READ_INTEGER v_nota3
	call _read_int
	mov word ptr [v_nota3], ax

	; t0 = v_nota2 + v_nota3
	mov ax, word ptr [v_nota2]
	add ax, word ptr [v_nota3]
	mov word ptr [t0], ax

	; t1 = v_nota1 + t0
	mov ax, word ptr [v_nota1]
	add ax, word ptr [t0]
	mov word ptr [t1], ax

	; t2 = t1 / 3
	mov ax, word ptr [t1]
	cwd
	mov bx, 3
	idiv bx
	mov word ptr [t2], ax

	; v_media = t2
	mov ax, word ptr [t2]
	mov word ptr [v_media], ax

	; WRITE "MEDIA CALCULADA"
	mov ecx, offset _str3
	call _print_str

	; WRITE v_media
	movsx eax, word ptr [v_media]
	push eax
	call _print_int

	; t3 = v_media >= 7
	mov ax, word ptr [v_media]
	cmp ax, 7
	jge cmp_t_t3
	mov byte ptr [t3], 0
	jmp cmp_e_t3
cmp_t_t3:
	mov byte ptr [t3], 1
cmp_e_t3:

	; v_aprovado = t3
	mov al, byte ptr [t3]
	mov byte ptr [v_aprovado], al

	; v_frequenciaok = TRUE
	mov byte ptr [v_frequenciaok], 1

	; t4 = v_aprovado AND v_frequenciaok
	mov al, byte ptr [v_aprovado]
	and al, byte ptr [v_frequenciaok]
	mov byte ptr [t4], al

	; v_resultadofinal = t4
	mov al, byte ptr [t4]
	mov byte ptr [v_resultadofinal], al

	; WRITE v_resultadofinal
	movzx eax, byte ptr [v_resultadofinal]
	push eax
	call _print_bool

	; ifFalse v_resultadofinal goto L0
	movzx eax, byte ptr [v_resultadofinal]
	cmp eax, 0
	je L0
	cmp eax, 0
	je L0

	; v_situacao = "APROVADO"
	lea esi, _lit_4
	lea edi, v_situacao
copy_5:
	mov al, [esi]
	mov [edi], al
	inc esi
	inc edi
	test al, al
	jnz copy_5

	; WRITE v_situacao
	mov ecx, offset v_situacao
	call _print_str

	; goto L1
	jmp L1

	; L0:
L0:

	; v_situacao = "REPROVADO"
	lea esi, _lit_5
	lea edi, v_situacao
copy_6:
	mov al, [esi]
	mov [edi], al
	inc esi
	inc edi
	test al, al
	jnz copy_6

	; WRITE v_situacao
	mov ecx, offset v_situacao
	call _print_str

	; L1:
L1:

	; t5 = v_media < 7
	mov ax, word ptr [v_media]
	cmp ax, 7
	jl cmp_t_t5
	mov byte ptr [t5], 0
	jmp cmp_e_t5
cmp_t_t5:
	mov byte ptr [t5], 1
cmp_e_t5:

	; t6 = ~t5
	mov al, byte ptr [t5]
	xor al, 1
	mov byte ptr [t6], al

	; v_aprovado = t6
	mov al, byte ptr [t6]
	mov byte ptr [v_aprovado], al

	; WRITE v_aprovado
	movzx eax, byte ptr [v_aprovado]
	push eax
	call _print_bool

	invoke Sleep, 50
	invoke ExitProcess, 0

_print_int:
	push ebp
	mov ebp, esp
	push ebx
	push esi
	movsx eax, word ptr [ebp+8]
	lea ecx, [_buf+12]
	mov byte ptr [ecx], 13
	inc ecx
	mov byte ptr [ecx], 10
	dec ecx
	mov esi, eax
	test eax, eax
	jns pi_pos
	neg eax
pi_pos:
	mov ebx, 10
pi_loop:
	xor edx, edx
	div ebx
	add dl, '0'
	mov byte ptr [ecx], dl
	dec ecx
	test eax, eax
	jnz pi_loop
	test esi, esi
	jns pi_write
	mov byte ptr [ecx], '-'
	dec ecx
pi_write:
	inc ecx
	lea edx, [_buf+14]
	sub edx, ecx
	invoke WriteFile, dword ptr [_hOut], ecx, edx, addr _written, 0
	pop esi
	pop ebx
	pop ebp
	ret 4

_print_bool:
	push ebp
	mov ebp, esp
	movzx eax, byte ptr [ebp+8]
	test eax, eax
	jz pb_false
	mov ecx, offset _s_true
	mov edx, 6
	jmp pb_write
pb_false:
	mov ecx, offset _s_false
	mov edx, 7
pb_write:
	invoke WriteFile, dword ptr [_hOut], ecx, edx, addr _written, 0
	pop ebp
	ret 4

_print_str:
	push ebp
	mov ebp, esp
	push esi
	mov esi, ecx
	xor edx, edx
ps_len:
	cmp byte ptr [esi+edx], 0
	je ps_write
	inc edx
	jmp ps_len
ps_write:
	invoke WriteFile, dword ptr [_hOut], ecx, edx, addr _written, 0
	invoke WriteFile, dword ptr [_hOut], offset _nl, 2, addr _written, 0
	pop esi
	pop ebp
	ret

_read_int:
	push ebp
	mov ebp, esp
	push esi
	push ebx
	invoke ReadFile, dword ptr [_hIn], addr _ibuf, 12, addr _read, 0
	mov esi, offset _ibuf
	xor eax, eax
	xor ebx, ebx
	mov cl, byte ptr [esi]
	cmp cl, '-'
	jne ri_loop
	mov ebx, 1
	inc esi
ri_loop:
	mov cl, byte ptr [esi]
	cmp cl, '0'
	jl ri_done
	cmp cl, '9'
	jg ri_done
	sub cl, '0'
	imul eax, eax, 10
	movzx ecx, cl
	add eax, ecx
	inc esi
	jmp ri_loop
ri_done:
	test ebx, ebx
	jz ri_pos
	neg eax
ri_pos:
	pop ebx
	pop esi
	pop ebp
	ret

end start
