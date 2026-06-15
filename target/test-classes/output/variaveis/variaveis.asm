; ===========================================
; Programa : variaveis
; Alvo     : x86 32 bits (MASM, Windows i386)
; ===========================================

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
	_s_true db 'true', 13, 10, 0
	_s_false db 'false', 13, 10, 0
	_nl db 13, 10
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

	; READ_STRING v_z
	lea ecx, v_z
	call _read_str

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

_read_str:
	push ebp
	mov ebp, esp
	push esi
	push edi
	push ebx
	mov edi, ecx
	invoke ReadFile, dword ptr [_hIn], edi, 255, addr _read, 0
	mov esi, edi
	xor ebx, ebx
rs_scan:
	mov al, byte ptr [esi+ebx]
	cmp al, 13
	je rs_trim
	cmp al, 10
	je rs_trim
	cmp al, 0
	je rs_done
	inc ebx
	jmp rs_scan
rs_trim:
	mov byte ptr [esi+ebx], 0
rs_done:
	pop ebx
	pop edi
	pop esi
	pop ebp
	ret

end start
