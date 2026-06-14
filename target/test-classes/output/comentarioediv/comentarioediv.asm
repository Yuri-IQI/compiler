; ============================================
; Programa : comentarioEDiv
; Alvo     : x86 32 bits (MASM, Windows i386)
; ============================================

.386
.model flat, stdcall
option casemap:none

include windows.inc
include kernel32.inc
includelib kernel32.lib

.data
	v_div dw 0
	t0 dw 0
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

	; t0 = 1
	mov word ptr [t0], 1

	; v_div = 1
	mov word ptr [v_div], 1

	invoke Sleep, 50
	invoke ExitProcess, 0

end start
