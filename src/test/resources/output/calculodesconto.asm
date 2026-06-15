; ===========================================
; Programa : calculoDesconto
; Alvo     : x86 32 bits (MASM, Windows i386)
; ===========================================

.386
.model flat, stdcall
option casemap:none

include windows.inc
include kernel32.inc
includelib kernel32.lib

.data
	v_preco dw 0
	v_resultado dw 0
	t0 dw 0
	t1 dw 0
	_buf db 14 dup(0)
	_s_true db 'true', 13, 10, 0
	_s_false db 'false', 13, 10, 0
	_nl db 13, 10
	_hOut dd ?
	_hIn dd ?
	_written dd ?
	_read dd ?

	_str0 db 'Informe o Preço', 0

.data?
	_ibuf db 12 dup(?)

.code

start:
	invoke GetStdHandle, STD_OUTPUT_HANDLE
	mov dword ptr [_hOut], eax

	invoke GetStdHandle, STD_INPUT_HANDLE
	mov dword ptr [_hIn], eax

	; t0 = 2
	mov word ptr [t0], 2

	; WRITE 2
	push 2
	call _print_int

	; WRITE "Informe o Preço"
	mov ecx, offset _str0
	call _print_str

	; READ_INTEGER v_preco
	call _read_int
	mov word ptr [v_preco], ax

	; WRITE v_preco
	movsx eax, word ptr [v_preco]
	push eax
	call _print_int

	; t1 = v_preco - 15
	mov ax, word ptr [v_preco]
	sub ax, 15
	mov word ptr [t1], ax

	; v_resultado = t1
	mov ax, word ptr [t1]
	mov word ptr [v_resultado], ax

	; WRITE v_resultado
	movsx eax, word ptr [v_resultado]
	push eax
	call _print_int

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
	jz ri_validate_pos
	neg eax
ri_validate_neg:
	cmp eax, -32767
	jl ri_overflow
	jmp ri_success
ri_validate_pos:
	cmp eax, 32767
	jg ri_overflow
ri_success:
	jmp ri_pos
	ri_overflow:
	xor eax, eax
ri_pos:
	pop ebx
	pop esi
	pop ebp
	ret

end start
