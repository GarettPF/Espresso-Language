; Author: Garett Pascual-Folster
; Section: 1002
; Date Last Modified: Feb 2, 2021
; Program Description: This program will utilize arrays, loops, and conditionals
;                      to find the gross profit per customer and other statistics

section .data
; System service call values
SERVICE_EXIT equ 60
EXIT_SUCCESS equ 0

; Data arrays
incomesArray dd 2623050, 2001216, 2234775, 917928, 1651860, 2785944, 2867940, 1426334, 3445403, 321312 ,2227471, 4550044, 5693760, 4315778, 1129436, 2914947, 2922605, 4048278, 2577665, 1189755 ,1149910, 4771482, 2938604, 5418294, 4168236, 3323200, 1147806, 3888342, 1166384, 2259264 ,2931360, 3199300, 1972840, 3827702, 6241932, 1965182, 3715480, 584675, 2703380, 1227150 ,883803, 2879740, 4942170, 5211360, 2864330, 980640, 2175500, 937584, 4923562, 5716340 ,1771740, 1096532, 5166720, 5664483, 2949450, 6088170, 2474469, 2992975, 3363267, 5709758 ,3488940, 4967768, 1966440, 3789874, 2897174, 4566132, 1296412, 3729418, 4131382, 335676 ,2343118, 4012624, 1813645, 3712176, 3150976, 3286752, 367524, 2332174, 1256904, 1149408 ,4685213, 2279030, 2442830, 337700, 5050752, 4338564, 4893858, 3499272, 2723440, 4208708 ,1831123, 2782410, 1701088, 2794230, 1996412, 2349500, 3069142, 3572285, 4626192, 2032506
expensesArray dd 1322279, 503753, 563344, 1176344, 971840, 778305, 1400947, 986725, 605207, 1361759 ,829895, 746045, 1357201, 1292539, 1145571, 1195105, 1221781, 1324924, 1211537, 676675 ,834180, 764255, 598365, 1429762, 807940, 914853, 853706, 1013334, 803670, 794454 ,886489, 1349721, 777289, 694460, 1248321, 1370640, 1211898, 810188, 817132, 756192 ,1046625, 890519, 1459755, 1410694, 651661, 1351063, 663862, 1107541, 1244732, 929763 ,1172956, 1042143, 1043792, 979506, 1262030, 570394, 610862, 579591, 1101513, 1322892 ,516834, 928870, 1372861, 1384850, 1347583, 1383582, 761759, 547566, 1490124, 951916 ,1496162, 554149, 1335786, 1386474, 1446764, 910098, 1247993, 1413991, 1248019, 774009 ,1110959, 909827, 803093, 745410, 1058486, 998513, 791917, 1041561, 923280, 815781 ,547680, 1045567, 803765, 1180309, 637734, 751832, 752571, 918934, 1275003, 865797
customersArray dd 4350, 4467, 5385, 6039, 5130, 3864, 6780, 3227, 6253, 6694 ,3901, 5206, 6590, 6113, 5852, 5141, 5285, 5538, 4205, 6295 ,3898, 5898, 4786, 5451, 4262, 6200, 6138, 4947, 4336, 5043 ,5910, 5350, 5332, 4997, 6267, 7069, 6406, 3341, 4582, 4545 ,5853, 5255, 5553, 6204, 5810, 4540, 4351, 4596, 5249, 5833 ,6562, 4588, 6210, 5919, 5618, 6270, 5391, 6301, 5451, 6247 ,5460, 5164, 4682, 5557, 5593, 5508, 6058, 4367, 5953, 5086 ,3743, 6514, 5615, 5288, 3296, 4891, 4428, 4721, 4968, 7368 ,4937, 6110, 4085, 3070, 5637, 5927, 5331, 4716, 4720, 5341 ,4781, 4890, 4012, 3930, 5338, 4625, 4049, 6065, 6218, 5943
LIST_LENGTH equ 100

; Data variables
totalGrossProfit dd 0
averageGrossProfit dd 0
highestCostPerCustomer dd 0
lowestIncomePerCustomer dd 0
profitableCount dd 0


section .bss
grossProfitPerCustomer resd 100


section .text
global _start
_start:

;   Find the gross profit per customer
    mov rbx, 0 ; index = 0
    mov rcx, LIST_LENGTH
    grossProfitLoop:
    ;   calculations
        mov eax, dword[incomesArray + rbx * 4]
        sub eax, dword[expensesArray + rbx * 4]
        cdq
        idiv dword[customersArray + rbx * 4]

    ;   store value
        mov dword[grossProfitPerCustomer + rbx * 4], eax

        inc rbx
    loop grossProfitLoop



;   Find the total gross profit for all 100 stores
    mov eax, 0 ; sum of incomes
    mov ebx, 0 ; sum of expenses
    mov rcx, LIST_LENGTH
    mov rdx, 0 ; index = 0
    totalGrossProfitLoop:
    ;   calculation of the sums
        add eax, dword[incomesArray + rdx * 4]
        add ebx, dword[expensesArray + rdx * 4]
        
        inc rdx
    loop totalGrossProfitLoop
    sub eax, ebx
    add dword[totalGrossProfit], eax



;   Find the average gross profit
    mov eax, dword[totalGrossProfit]
    mov ebx, 100
    cdq
    idiv ebx
    mov dword[averageGrossProfit], eax



;   Find the highest cost per customer
    mov rbx, 0  ; index = 0
    mov rcx, LIST_LENGTH
    mov esi, 0  ; value of highest cost per customer
    highestCostPerCustomerLoop:
    ;   divide
        mov eax, dword[expensesArray + rbx * 4]
        cdq
        idiv dword[customersArray + rbx * 4]

    ;   check if greater than esi
        cmp eax, esi
        jl notGreater
        ;   update the highest value
            mov esi, eax
        notGreater:

        inc rbx
    loop highestCostPerCustomerLoop
    mov dword[highestCostPerCustomer], esi



;   Find the lowest income per customer
    mov rbx, 0 ; index = 0
    mov rcx, LIST_LENGTH
;   Do the first element of the lists
    mov eax, dword[incomesArray]
    cdq
    idiv dword[customersArray]
    mov esi, eax
;   Now do the looping
    lowestIncomePerCustomerLoop:
    ;   divide
        mov eax, dword[incomesArray + rbx * 4]
        cdq
        idiv dword[customersArray + rbx * 4]

    ;   check if less than esi
        cmp eax, esi
        jg notLesser
        ;   update the lowest value
            mov esi, eax
        notLesser:

        inc rbx
    loop lowestIncomePerCustomerLoop
    mov dword[lowestIncomePerCustomer], esi



;   Find the count of the stores that are profitable
    mov rbx, 0 ; index = 0
    mov rcx, LIST_LENGTH
    mov esi, 0 ; count of profitable stores
    profitableCountLoop:
    ;   calculations
        mov eax, dword[incomesArray + rbx * 4]
        mov edx, dword[expensesArray + rbx * 4]
        sub eax, edx

    ;   check if profitable
        cmp eax, 0
        jl notProfitable
        ;   Add to the count
            inc esi
        notProfitable:

        inc rbx
    loop profitableCountLoop
    mov dword[profitableCount], esi



endProgram:
;   Ends the program with success return value
    mov rax, SERVICE_EXIT
    mov rdi, EXIT_SUCCESS
    syscall