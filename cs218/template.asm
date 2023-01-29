; Author: Garett Pascual-Folster
; Section: CS 218 - 1002
; Date Last Modified: WRITE DATE HERE
; Program Description: 

section .data
; System service call values
SERVICE_EXIT equ 60
EXIT_SUCCESS equ 0


section .bss

section .text
global _start
_start:


endProgram:
; 	Ends program with success return value
	mov rax, SERVICE_EXIT
	mov rdi, EXIT_SUCCESS
	syscall