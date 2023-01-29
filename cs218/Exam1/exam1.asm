; Author: Garett Pascual-Folster
; Section: CS 218 - 1002
; Date Last Modified: Feb 23
; Program Description: EXAM 1 PART 2

%macro calculateAdjustment 3
	mov eax, dword[%2]
	mov bx, word[%1]

;	do initial division
	div ebx
;	add the 6
	add eax, 6
;	multiply by 12
	mov ebx, 12
	mul ebx

;	store answer
	mov qword[%3], rax
%endmacro

section .data
	; System service call values
	SERVICE_EXIT equ 60
	EXIT_SUCCESS equ 0

	; variable for arguments
	X dw 100
	Y dd 12345
	Answer dq 0

section .text
global _start
_start:

;	run macro
	calculateAdjustment X, Y, Answer

endProgram:
; 	Ends program with success return value
	mov rax, SERVICE_EXIT
	mov rdi, EXIT_SUCCESS
	syscall