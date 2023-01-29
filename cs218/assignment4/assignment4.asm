; Author: Garett Pascual-Folster
; Section: 1002
; Date Last Modified: Feb 21, 2021
; Program Description: This program will utilize macros to perform different tasks


; Macro 1 - Swap two values
; Macro 2 - Remove leading spaces
; Macro 3 - KMB number string to integer

;	Counts the number of characters in the null terminated string
;	Returns the count in rdx
%macro countStringLength 1
	mov rdx, 1
	mov rbx, %1
	%%countStringLengthLoop:
		mov ch, byte[rbx]
		cmp ch, NULL
		je %%countStringLengthDone
		
		inc rdx
		inc rbx
	jmp %%countStringLengthLoop
	%%countStringLengthDone:
%endmacro

; Macro 1 -	Swaps two given values
%macro swapValues 2
;	store first value
	mov rax, %1
	push rax
;	copy second value to first register
	mov rbx, %2
	mov rax, rbx
;	pop first value to second register
	pop rbx
;	store the values
	mov %1, rax
	mov %2, rbx
%endmacro

; Macro 2 - Remove leading spaces
%macro removeLeadingSpaces 1
	mov rax, %1
	mov r8, rax	; store initial address

;	count the number of spaces
	mov rbx, 0	; count = 0
	%%countSpacesLoop:
		mov cl, byte[rax]
		cmp cl, 32
		jne %%notSpace
		inc rbx
		inc rax
	jmp %%countSpacesLoop
	%%notSpace:

;	push the rest of the string
	mov rdx, 0
	%%pushRemaindingString:
		mov dl, byte[rax]
		push rdx
	
	;	check if cl is null
		cmp dl, NULL
		je %%isNULL

		inc rax
	jmp %%pushRemaindingString
	%%isNULL:

;	go back the amount of leading spaces
	sub rax, rbx

;	pop characters backwards
	%%popCharBackwards:
		pop rdx
		mov byte[rax], dl

	;	check if in the front
		cmp r8, rax
		je %%finished

		sub rax, 1
	loop %%popCharBackwards
	%%finished:

%endmacro

; Macro 3 - KMB number string to integer
%macro KMBstringTOint 2
	mov rsi, %2
	
;	skip the spaces if any
	%%skipSpaces:
		mov bl, byte[rsi]
		cmp bl, 32
		jne %%noSpace
			inc rsi
	jmp %%skipSpaces
	%%noSpace:

;	check if negative or not
	mov r8, 0	; 0 = not negative, 1 = negative
	mov bl, byte[rsi]
	cmp bl, 45
	jne %%notNegative
		mov r8, 1	; is negative
		inc rsi
	%%notNegative:

;	calculate the number
	mov r9, 0	; 0 = not decimal, 1 = decimal
	mov r10, 10	; to move the decimal
	mov rdi, 0	; count of digits after decimal
	mov rax, 0
	mov rbx, 0
	%%calculateNumber:
		mov bl, byte[rsi]

	;	skip if found decimal
		cmp r9, 1
		je %%decimalFoundAlready
		;	check if decimal
			cmp bl, 46
			jne %%noDecimal
				mov r9, 1	; decimal
				inc rsi
				jmp %%calculateNumber
			%%noDecimal:
		%%decimalFoundAlready:

	;	check if rsi is not a number
		cmp bl, 75
		je %%multiplyK
		cmp bl, 107
		je %%multiplyK
		cmp bl, 77
		je %%multiplyM
		cmp bl, 109
		je %%multiplyM
		cmp bl, 66
		je %%multiplyB
		cmp bl, 98
		je %%multiplyB

	;	add to rdi if decimal found already
		cmp r9, 1
		jne %%noAdd
			inc rdi
		%%noAdd:

	;	store numbers
		sub bl, 48
		mul r10		; mov rax to the left one time
		add rax, rbx

		inc rsi
	jmp %%calculateNumber

;	set the factor
	%%multiplyK:
		mov rcx, 3
		jmp %%skipOthers
	%%multiplyM:
		mov rcx, 6
		jmp %%skipOthers
	%%multiplyB:
		mov rcx, 9
		jmp %%skipOthers
	%%skipOthers:

;	Multiple for the correct decimal places
	sub rcx, rdi
	push rax	; store rax value
	mov rax, 1
	%%powerOfRCX:
		mul r10
	loop %%powerOfRCX
	mov rdi, rax
	pop rax		; bring back rax value
	mul rdi

;	make negative if it is
	cmp r8, 0
	je %%numberIsNotNegative
		mov rbx, -1
		imul rbx
	%%numberIsNotNegative:

	mov qword[%1], rax

%endmacro

section .data
; Do not modify these declarations
SYSTEM_WRITE equ 1
SYSTEM_EXIT equ 60
SUCCESS equ 0
STANDARD_OUT equ 1
LINEFEED equ 10
NULL equ 0

macro1Label db "Macro 1: "
oldValue db "Fails", LINEFEED, NULL
newValue db "Works", LINEFEED, NULL

macro2Label db "Macro 2: "
macro2message db "         <- If you see a space, you must replace.", LINEFEED, NULL

macro3Label1 db "Macro 3-1: "
macro3Label2 db "Macro 3-2: "
macro3Label3 db "Macro 3-3: "
macro3Number1 db "1.62k", NULL
macro3Number2 db "   -14.213M", NULL
macro3Number3 db "  491B", NULL
macro3Integer1 dq 0
macro3Integer2 dq 0
macro3Integer3 dq 0
macro3Expected1 dq 1620
macro3Expected2 dq -14213000
macro3Expected3 dq 491000000000
macro3Success db "Pass", LINEFEED, NULL
macro3Fail db "Fail", LINEFEED, NULL

section .bss
oldAddress resq 1
newAddress resq 1

section .text
global _start
_start:

	mov rax, oldValue
	mov qword[oldAddress], rax
	mov rax, newValue
	mov qword[newAddress], rax

; Macro 1
; Invoke macro 1 using qword[oldAddress] and qword[newAddress] as the arguments

	; YOUR CODE HERE
	swapValues qword[oldAddress], qword[newAddress]

; Macro 1 Test - Do not alter
	mov rax, SYSTEM_WRITE
	mov rdi, STANDARD_OUT
	mov rsi, macro1Label
	mov rdx, 9
	syscall
	
	mov rax, SYSTEM_WRITE
	mov rdi, STANDARD_OUT
	mov rsi, qword[oldAddress]
	mov rdx, 6
	syscall
		
; Macro 2
; Invoke macro 2 using macro2message as the argument

	; YOUR CODE HERE
	removeLeadingSpaces macro2message

; Macro 2 Test - Do not alter
	mov rax, SYSTEM_WRITE
	mov rdi, STANDARD_OUT
	mov rsi, macro2Label
	mov rdx, 9
	syscall
	
	mov rax, SYSTEM_WRITE
	mov rdi, STANDARD_OUT
	mov rsi, macro2message
	countStringLength macro2message
	syscall
	
; Macro 3 - Test 1
; Invoke macro 3 using macro3Integer1 and macro3Number1 as the arguments

	; YOUR CODE HERE
	KMBstringTOint macro3Integer1, macro3Number1

; Macro 3 - Test 1 Results - Do not alter
	mov rax, SYSTEM_WRITE
	mov rdi, STANDARD_OUT
	mov rsi, macro3Label1
	mov rdx, 11
	syscall
	
	mov rax, qword[macro3Expected1]
	cmp rax, qword[macro3Integer1]
	je macroTest3_1_success
		mov rsi, macro3Fail
		jmp macroTest3_1_print
	macroTest3_1_success:
		mov rsi, macro3Success
	macroTest3_1_print:
	
	mov rax, SYSTEM_WRITE
	mov rdi, STANDARD_OUT
	mov rdx, 6
	syscall
	
; Macro 3 - Test 2
; Invoke macro 3 using macro3Integer2 and  macro3Number2 as the arguments

	; YOUR CODE HERE
	KMBstringTOint macro3Integer2, macro3Number2

; Macro 3 - Test 2 Results - Do not alter
	mov rax, SYSTEM_WRITE
	mov rdi, STANDARD_OUT
	mov rsi, macro3Label2
	mov rdx, 11
	syscall
	
	mov rax, qword[macro3Expected2]
	cmp rax, qword[macro3Integer2]
	je macroTest3_2_success
		mov rsi, macro3Fail
		jmp macroTest3_2_print
	macroTest3_2_success:
		mov rsi, macro3Success
	macroTest3_2_print:
	
	mov rax, SYSTEM_WRITE
	mov rdi, STANDARD_OUT
	mov rdx, 6
	syscall	
	
; Macro 3 - Test 3
; Invoke macro 3 using macro3Integer3 and macro3Number3 as the arguments

	; YOUR CODE HERE
	KMBstringTOint macro3Integer3, macro3Number3

; Macro 3 - Test 3 Results - Do not alter
	mov rax, SYSTEM_WRITE
	mov rdi, STANDARD_OUT
	mov rsi, macro3Label3
	mov rdx, 11
	syscall
	
	mov rax, qword[macro3Expected3]
	cmp rax, qword[macro3Integer3]
	je macroTest3_3_success
		mov rsi, macro3Fail
		jmp macroTest3_3_print
	macroTest3_3_success:
		mov rsi, macro3Success
	macroTest3_3_print:
	
	mov rax, SYSTEM_WRITE
	mov rdi, STANDARD_OUT
	mov rdx, 6
	syscall	
	
endProgram:
	mov rax, SYSTEM_EXIT
	mov rdi, SUCCESS
	syscall