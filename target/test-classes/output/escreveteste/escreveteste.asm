; ============================================
; Programa  : EscreveTeste
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
	_buf db 14 dup(0)
	_s_true db 'true', 10
	_s_false db 'false', 10
	_hOut dd ?
	_hIn dd ?
	_written dd ?
	_read dd ?

	_str0 db 'teste', 0

.data?
	_ibuf db 12 dup(?)

.code

start:
	invoke GetStdHandle, STD_OUTPUT_HANDLE
	mov dword ptr [_hOut], eax

	invoke GetStdHandle, STD_INPUT_HANDLE
	mov dword ptr [_hIn], eax

	; WRITE "teste"
	mov ecx, offset _str0
	call _print_str

	invoke Sleep, 50
	invoke ExitProcess, 0

_print_str:
	push ebp
	mov ebp, esp
	push esi
	push edi
	mov esi, ecx
	mov edi, ecx
	xor eax, eax
	mov ecx, 256
	repne scasb
	mov eax, 256
	sub eax, ecx
	dec eax
	invoke WriteFile, dword ptr [_hOut], esi, eax, addr _written, 0
	pop edi
	pop esi
	pop ebp
	ret

end start
