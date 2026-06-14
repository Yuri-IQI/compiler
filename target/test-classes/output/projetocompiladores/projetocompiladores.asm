; ============================================
; Programa : ProjetoCompiladores
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
	v_resultado db 256 dup(0)
	v_aprovado db 0
	v_nota2 dw 0
	v_media dw 0
	t0 dw 0
	t1 dw 0
	t2 db 0
	_buf db 14 dup(0)
	_s_true db 'true', 13, 10, 0
	_s_false db 'false', 13, 10, 0
	_nl db 13, 10
	_hOut dd ?
	_hIn dd ?
	_written dd ?
	_read dd ?

	_str0 db 'INFORME A PRIMEIRA NOTA', 0
	_str1 db 'INFORME A SEGUNDA NOTA', 0
	_lit_2 db 'Aprovado', 0
	_lit_3 db 'Reprovado', 0

.data?
	_ibuf db 12 dup(?)

.code

start:
	invoke GetStdHandle, STD_OUTPUT_HANDLE
	mov dword ptr [_hOut], eax

	invoke GetStdHandle, STD_INPUT_HANDLE
	mov dword ptr [_hIn], eax

	; WRITE "INFORME A PRIMEIRA NOTA"
	mov ecx, offset _str0
	call _print_str

	; READ_INTEGER v_nota1
	call _read_int
	mov word ptr [v_nota1], ax

	; WRITE "INFORME A SEGUNDA NOTA"
	mov ecx, offset _str1
	call _print_str

	; READ_INTEGER v_nota2
	call _read_int
	mov word ptr [v_nota2], ax

	; t0 = v_nota1 + v_nota2
	mov ax, word ptr [v_nota1]
	add ax, word ptr [v_nota2]
	mov word ptr [t0], ax

	; t1 = t0 / 2
	mov ax, word ptr [t0]
	cwd
	mov bx, 2
	idiv bx
	mov word ptr [t1], ax

	; v_media = t1
	mov ax, word ptr [t1]
	mov word ptr [v_media], ax

	; WRITE v_media
	movsx eax, word ptr [v_media]
	push eax
	call _print_int

	; t2 = v_media >= 7
	mov ax, word ptr [v_media]
	cmp ax, 7
	jge cmp_t_t2
	mov byte ptr [t2], 0
	jmp cmp_e_t2
cmp_t_t2:
	mov byte ptr [t2], 1
cmp_e_t2:

	; v_aprovado = t2
	mov al, byte ptr [t2]
	mov byte ptr [v_aprovado], al

	; WRITE v_aprovado
	movzx eax, byte ptr [v_aprovado]
	push eax
	call _print_bool

	; ifFalse v_aprovado goto L0
	movzx eax, byte ptr [v_aprovado]
	cmp eax, 0
	je L0
	cmp eax, 0
	je L0

	; v_resultado = "Aprovado"
	lea esi, _lit_2
	lea edi, v_resultado
copy_3:
	mov al, [esi]
	mov [edi], al
	inc esi
	inc edi
	test al, al
	jnz copy_3

	; WRITE v_resultado
	mov ecx, offset v_resultado
	call _print_str

	; goto L1
	jmp L1

	; L0:
L0:

	; v_resultado = "Reprovado"
	lea esi, _lit_3
	lea edi, v_resultado
copy_4:
	mov al, [esi]
	mov [edi], al
	inc esi
	inc edi
	test al, al
	jnz copy_4

	; WRITE v_resultado
	mov ecx, offset v_resultado
	call _print_str

	; L1:
L1:

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
