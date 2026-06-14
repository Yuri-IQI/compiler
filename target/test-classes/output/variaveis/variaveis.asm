; ============================================
; Programa  : variaveis
; Gerado por: Compilador INM
; Alvo      : x86 32 bits (MASM, Windows i386)
; ============================================

.386
.model flat, stdcall
option casemap:none

include windows.inc
include kernel32.inc
includelib kernel32.lib

.data
	v_x dw 0
	v_y dw 0
	v_z db 256 dup(0)
	_buf db 14 dup(0)
	_s_true db 'true', 10
	_s_false db 'false', 10
	_hOut dd ?
	_hIn dd ?
	_written dd ?
	_read dd ?


.data?
	_ibuf db 12 dup(?)

.code

start:
	invoke GetStdHandle, STD_OUTPUT_HANDLE
	mov dword ptr [_hOut], eax

	invoke GetStdHandle, STD_INPUT_HANDLE
	mov dword ptr [_hIn], eax

	; WRITE v_x
	movsx eax, word ptr [v_x]
	push eax
	call _print_int

	; WRITE v_y
	movsx eax, word ptr [v_y]
	push eax
	call _print_int

	; READ v_z
	call _read_int
	mov word ptr [v_z], ax

	invoke Sleep, 50
	invoke ExitProcess, 0

_print_int:
	push ebp
	mov ebp, esp
	push ebx
	push esi
	movsx eax, word ptr [ebp+8]
	lea ecx, [_buf+13]
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

_read_int:
	push ebp
	mov ebp, esp
	push esi
	push ebx
	invoke ReadConsoleA, dword ptr [_hIn], addr _ibuf, 12, addr _read, 0
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
