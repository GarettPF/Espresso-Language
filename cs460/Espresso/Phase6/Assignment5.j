.class public Assignment5
.super java/lang/Object

.method public static fib(I)I
	.limit stack 50
	.limit locals 5
;  (39) Method Declaration (fib)
;  (40) Local Variable Declaration
	astore_1
;  (40) End LocalDecl
;  (41) Local Variable Declaration
	astore_2
;  (41) End LocalDecl
;  (43) For Statement
;  (43) Local Variable Declaration
;  (43) Literal
	iconst_0
;  (43) End Literal
	istore_3
;  (43) End LocalDecl
L1:
;  (43) Binary Expression
;  (43) Name Expression --
	iload_3
;  (43) End NameExpr
;  (43) Name Expression --
	iload_0
;  (43) End NameExpr
	if_icmplt L4
	iconst_0
	goto L5
L4:
	iconst_1
L5:
;  (43) End BinaryExpr
	ifeq L2
;  (44) Local Variable Declaration
	astore 4
;  (44) End LocalDecl
;  (46) Expression Statement
;  (46) Assignment
;  (46) ArrayAccessExpr target
;  (46) ArrayAccessExpr
;  (46) End ArrayAccessExpr
;  (46) ArrayAccessExpr index
;  (46) Literal
	iconst_0
;  (46) End Literal
;  (46) Binary Expression
;  (46) Binary Expression
;  (46) ArrayAccessExpr
;  (46) End ArrayAccessExpr
;  (46) ArrayAccessExpr
;  (46) End ArrayAccessExpr
	imul
;  (46) End BinaryExpr
;  (46) Binary Expression
;  (46) ArrayAccessExpr
;  (46) End ArrayAccessExpr
;  (46) ArrayAccessExpr
;  (46) End ArrayAccessExpr
	imul
;  (46) End BinaryExpr
	iadd
;  (46) End BinaryExpr
	iastore
;  (46) End Assignment
;  (46) End ExprStat
;  (47) Expression Statement
;  (47) Assignment
;  (47) ArrayAccessExpr target
;  (47) ArrayAccessExpr
;  (47) End ArrayAccessExpr
;  (47) ArrayAccessExpr index
;  (47) Literal
	iconst_1
;  (47) End Literal
;  (47) Binary Expression
;  (47) Binary Expression
;  (47) ArrayAccessExpr
;  (47) End ArrayAccessExpr
;  (47) ArrayAccessExpr
;  (47) End ArrayAccessExpr
	imul
;  (47) End BinaryExpr
;  (47) Binary Expression
;  (47) ArrayAccessExpr
;  (47) End ArrayAccessExpr
;  (47) ArrayAccessExpr
;  (47) End ArrayAccessExpr
	imul
;  (47) End BinaryExpr
	iadd
;  (47) End BinaryExpr
	iastore
;  (47) End Assignment
;  (47) End ExprStat
;  (48) Expression Statement
;  (48) Assignment
;  (48) ArrayAccessExpr target
;  (48) ArrayAccessExpr
;  (48) End ArrayAccessExpr
;  (48) ArrayAccessExpr index
;  (48) Literal
	iconst_0
;  (48) End Literal
;  (48) Binary Expression
;  (48) Binary Expression
;  (48) ArrayAccessExpr
;  (48) End ArrayAccessExpr
;  (48) ArrayAccessExpr
;  (48) End ArrayAccessExpr
	imul
;  (48) End BinaryExpr
;  (48) Binary Expression
;  (48) ArrayAccessExpr
;  (48) End ArrayAccessExpr
;  (48) ArrayAccessExpr
;  (48) End ArrayAccessExpr
	imul
;  (48) End BinaryExpr
	iadd
;  (48) End BinaryExpr
	iastore
;  (48) End Assignment
;  (48) End ExprStat
;  (49) Expression Statement
;  (49) Assignment
;  (49) ArrayAccessExpr target
;  (49) ArrayAccessExpr
;  (49) End ArrayAccessExpr
;  (49) ArrayAccessExpr index
;  (49) Literal
	iconst_1
;  (49) End Literal
;  (49) Binary Expression
;  (49) Binary Expression
;  (49) ArrayAccessExpr
;  (49) End ArrayAccessExpr
;  (49) ArrayAccessExpr
;  (49) End ArrayAccessExpr
	imul
;  (49) End BinaryExpr
;  (49) Binary Expression
;  (49) ArrayAccessExpr
;  (49) End ArrayAccessExpr
;  (49) ArrayAccessExpr
;  (49) End ArrayAccessExpr
	imul
;  (49) End BinaryExpr
	iadd
;  (49) End BinaryExpr
	iastore
;  (49) End Assignment
;  (49) End ExprStat
;  (50) Expression Statement
;  (50) Assignment
;  (50) Name Expression --
	iaload 4
;  (50) End NameExpr
	astore_2
;  (50) End Assignment
;  (50) End ExprStat
L3:
;  (43) Expression Statement
;  (43) Unary Post Expression
;  (43) Name Expression --
	iload_3
;  (43) End NameExpr
	iinc 3 1
;  (43) End UnaryPostExpr
	pop
;  (43) End ExprStat
	goto L1
L2:
;  (43) End ForStat
;  (53) Return Statement
;  (53) ArrayAccessExpr
;  (53) End ArrayAccessExpr
	ireturn
;  (53) End ReturnStat
.end method

.method public static sieve(I)[I
	.limit stack 50
	.limit locals 9
;  (56) Method Declaration (sieve)
;  (57) Local Variable Declaration
	astore_1
;  (57) End LocalDecl
;  (58) For Statement
;  (58) Local Variable Declaration
;  (58) Literal
	iconst_0
;  (58) End Literal
	istore_2
;  (58) End LocalDecl
L6:
;  (58) Binary Expression
;  (58) Name Expression --
	iload_2
;  (58) End NameExpr
;  (58) Name Expression --
	iload_0
;  (58) End NameExpr
	if_icmplt L9
	iconst_0
	goto L10
L9:
	iconst_1
L10:
;  (58) End BinaryExpr
	ifeq L7
;  (59) Expression Statement
;  (59) Assignment
;  (59) ArrayAccessExpr target
;  (59) Name Expression --
	aload_1
;  (59) End NameExpr
;  (59) ArrayAccessExpr index
;  (59) Name Expression --
	iload_2
;  (59) End NameExpr
;  (59) Binary Expression
;  (59) Name Expression --
	iload_2
;  (59) End NameExpr
;  (59) Literal
	iconst_1
;  (59) End Literal
	iadd
;  (59) End BinaryExpr
	iastore
;  (59) End Assignment
;  (59) End ExprStat
L8:
;  (58) Expression Statement
;  (58) Unary Post Expression
;  (58) Name Expression --
	iload_2
;  (58) End NameExpr
	iinc 2 1
;  (58) End UnaryPostExpr
	pop
;  (58) End ExprStat
	goto L6
L7:
;  (58) End ForStat
;  (60) Expression Statement
;  (60) Assignment
;  (60) ArrayAccessExpr target
;  (60) Name Expression --
	aload_1
;  (60) End NameExpr
;  (60) ArrayAccessExpr index
;  (60) Literal
	iconst_0
;  (60) End Literal
;  (60) Literal
	iconst_0
;  (60) End Literal
	iastore
;  (60) End Assignment
;  (60) End ExprStat
;  (61) For Statement
;  (61) Local Variable Declaration
;  (61) Literal
	iconst_1
;  (61) End Literal
	istore_3
;  (61) End LocalDecl
L11:
;  (61) Binary Expression
;  (61) Name Expression --
	iload_3
;  (61) End NameExpr
;  (61) Name Expression --
	iload_0
;  (61) End NameExpr
	if_icmplt L14
	iconst_0
	goto L15
L14:
	iconst_1
L15:
;  (61) End BinaryExpr
	ifeq L12
;  (62) If Statement
;  (62) Binary Expression
;  (62) ArrayAccessExpr
;  (62) End ArrayAccessExpr
;  (62) Literal
	iconst_0
;  (62) End Literal
	if_icmpne L17
	iconst_0
	goto L18
L17:
	iconst_1
L18:
;  (62) End BinaryExpr
	ifeq L16
;  (63) Local Variable Declaration
;  (63) Binary Expression
;  (63) ArrayAccessExpr
;  (63) End ArrayAccessExpr
;  (63) Literal
	iconst_2
;  (63) End Literal
	imul
;  (63) End BinaryExpr
	istore 4
;  (63) End LocalDecl
;  (64) While Statement
L19:
;  (64) Binary Expression
;  (64) Name Expression --
	iload 4
;  (64) End NameExpr
;  (64) Name Expression --
	iload_0
;  (64) End NameExpr
	if_icmple L21
	iconst_0
	goto L22
L21:
	iconst_1
L22:
;  (64) End BinaryExpr
	ifeq L20
;  (65) Expression Statement
;  (65) Assignment
;  (65) ArrayAccessExpr target
;  (65) Name Expression --
	aload_1
;  (65) End NameExpr
;  (65) ArrayAccessExpr index
;  (65) Binary Expression
;  (65) Name Expression --
	iload 4
;  (65) End NameExpr
;  (65) Literal
	iconst_1
;  (65) End Literal
	isub
;  (65) End BinaryExpr
;  (65) Literal
	iconst_0
;  (65) End Literal
	iastore
;  (65) End Assignment
;  (65) End ExprStat
;  (66) Expression Statement
;  (66) Assignment
;  (66) Binary Expression
;  (66) Name Expression --
	iload 4
;  (66) End NameExpr
;  (66) ArrayAccessExpr
;  (66) End ArrayAccessExpr
	iadd
;  (66) End BinaryExpr
	istore 4
;  (66) End Assignment
;  (66) End ExprStat
	goto L19
L20:
;  (64) End WhileStat
L16:
;  (62) End IfStat
L13:
;  (61) Expression Statement
;  (61) Unary Post Expression
;  (61) Name Expression --
	iload_3
;  (61) End NameExpr
	iinc 3 1
;  (61) End UnaryPostExpr
	pop
;  (61) End ExprStat
	goto L11
L12:
;  (61) End ForStat
;  (70) Local Variable Declaration
;  (70) Literal
	iconst_0
;  (70) End Literal
	istore 4
;  (70) End LocalDecl
;  (71) For Statement
;  (71) Local Variable Declaration
;  (71) Literal
	iconst_0
;  (71) End Literal
	istore 5
;  (71) End LocalDecl
L23:
;  (71) Binary Expression
;  (71) Name Expression --
	iload 5
;  (71) End NameExpr
;  (71) Name Expression --
	iload_0
;  (71) End NameExpr
	if_icmplt L26
	iconst_0
	goto L27
L26:
	iconst_1
L27:
;  (71) End BinaryExpr
	ifeq L24
;  (72) If Statement
;  (72) Binary Expression
;  (72) ArrayAccessExpr
;  (72) End ArrayAccessExpr
;  (72) Literal
	iconst_0
;  (72) End Literal
	if_icmpne L29
	iconst_0
	goto L30
L29:
	iconst_1
L30:
;  (72) End BinaryExpr
	ifeq L28
;  (73) Expression Statement
;  (73) Unary Post Expression
;  (73) Name Expression --
	iload 4
;  (73) End NameExpr
	iinc 4 1
;  (73) End UnaryPostExpr
	pop
;  (73) End ExprStat
L28:
;  (72) End IfStat
L25:
;  (71) Expression Statement
;  (71) Unary Post Expression
;  (71) Name Expression --
	iload 5
;  (71) End NameExpr
	iinc 5 1
;  (71) End UnaryPostExpr
	pop
;  (71) End ExprStat
	goto L23
L24:
;  (71) End ForStat
;  (74) Local Variable Declaration
	astore 6
;  (74) End LocalDecl
;  (75) Local Variable Declaration
;  (75) Literal
	iconst_0
;  (75) End Literal
	istore 7
;  (75) End LocalDecl
;  (76) For Statement
;  (76) Local Variable Declaration
;  (76) Literal
	iconst_0
;  (76) End Literal
	istore 8
;  (76) End LocalDecl
L31:
;  (76) Binary Expression
;  (76) Name Expression --
	iload 8
;  (76) End NameExpr
;  (76) Name Expression --
	iload_0
;  (76) End NameExpr
	if_icmplt L34
	iconst_0
	goto L35
L34:
	iconst_1
L35:
;  (76) End BinaryExpr
	ifeq L32
;  (77) If Statement
;  (77) Binary Expression
;  (77) ArrayAccessExpr
;  (77) End ArrayAccessExpr
;  (77) Literal
	iconst_0
;  (77) End Literal
	if_icmpne L37
	iconst_0
	goto L38
L37:
	iconst_1
L38:
;  (77) End BinaryExpr
	ifeq L36
;  (79) Expression Statement
;  (79) Assignment
;  (79) ArrayAccessExpr target
;  (79) Name Expression --
	iaload 6
;  (79) End NameExpr
;  (79) ArrayAccessExpr index
;  (79) Name Expression --
	iload 7
;  (79) End NameExpr
;  (79) ArrayAccessExpr
;  (79) End ArrayAccessExpr
	iastore
;  (79) End Assignment
;  (79) End ExprStat
;  (80) Expression Statement
;  (80) Unary Post Expression
;  (80) Name Expression --
	iload 7
;  (80) End NameExpr
	iinc 7 1
;  (80) End UnaryPostExpr
	pop
;  (80) End ExprStat
L36:
;  (77) End IfStat
L33:
;  (76) Expression Statement
;  (76) Unary Post Expression
;  (76) Name Expression --
	iload 8
;  (76) End NameExpr
	iinc 8 1
;  (76) End UnaryPostExpr
	pop
;  (76) End ExprStat
	goto L31
L32:
;  (76) End ForStat
;  (82) Return Statement
;  (82) Name Expression --
	iaload 6
;  (82) End NameExpr
	areturn
;  (82) End ReturnStat
.end method

.method public static checkMatrixDim([[II)Z
	.limit stack 50
	.limit locals 3
;  (85) Method Declaration (checkMatrixDim)
;  (86) If Statement
;  (86) Binary Expression
;  (86) Array length
;  (86) Name Expression --
	aload_0
;  (86) End NameExpr
	arraylength
;  (86) Name Expression --
	iload_1
;  (86) End NameExpr
	if_icmpne L40
	iconst_0
	goto L41
L40:
	iconst_1
L41:
;  (86) End BinaryExpr
	ifeq L39
;  (87) Return Statement
;  (87) Literal
	iconst_0
;  (87) End Literal
	ireturn
;  (87) End ReturnStat
L39:
;  (86) End IfStat
;  (88) For Statement
;  (88) Local Variable Declaration
;  (88) Literal
	iconst_0
;  (88) End Literal
	istore_2
;  (88) End LocalDecl
L42:
;  (88) Binary Expression
;  (88) Name Expression --
	iload_2
;  (88) End NameExpr
;  (88) Name Expression --
	iload_1
;  (88) End NameExpr
	if_icmplt L45
	iconst_0
	goto L46
L45:
	iconst_1
L46:
;  (88) End BinaryExpr
	ifeq L43
;  (89) If Statement
;  (89) Binary Expression
;  (89) Array length
;  (89) ArrayAccessExpr
;  (89) End ArrayAccessExpr
	arraylength
;  (89) Name Expression --
	iload_1
;  (89) End NameExpr
	if_icmpne L48
	iconst_0
	goto L49
L48:
	iconst_1
L49:
;  (89) End BinaryExpr
	ifeq L47
;  (90) Return Statement
;  (90) Literal
	iconst_0
;  (90) End Literal
	ireturn
;  (90) End ReturnStat
L47:
;  (89) End IfStat
L44:
;  (88) Expression Statement
;  (88) Unary Post Expression
;  (88) Name Expression --
	iload_2
;  (88) End NameExpr
	iinc 2 1
;  (88) End UnaryPostExpr
	pop
;  (88) End ExprStat
	goto L42
L43:
;  (88) End ForStat
;  (91) Return Statement
;  (91) Literal
	iconst_1
;  (91) End Literal
	ireturn
;  (91) End ReturnStat
.end method

.method public static rjustify(II)Ljava/lang/String;
	.limit stack 50
	.limit locals 3
;  (94) Method Declaration (rjustify)
;  (95) Local Variable Declaration
;  (95) Binary Expression
;  (95) Literal
	ldc ""
;  (95) End Literal
;  (95) Name Expression --
	iload_0
;  (95) End NameExpr
	iadd
;  (95) End BinaryExpr
	astore_2
;  (95) End LocalDecl
;  (96) While Statement
L50:
;  (96) Binary Expression
;  (96) Invocation
;  (96) Name Expression --
	aload_2
;  (96) End NameExpr
;  (96) End Invocation
;  (96) Name Expression --
	iload_1
;  (96) End NameExpr
	if_icmplt L52
	iconst_0
	goto L53
L52:
	iconst_1
L53:
;  (96) End BinaryExpr
	ifeq L51
;  (97) Expression Statement
;  (97) Assignment
;  (97) Binary Expression
;  (97) Literal
	ldc " "
;  (97) End Literal
;  (97) Name Expression --
	aload_2
;  (97) End NameExpr
	iadd
;  (97) End BinaryExpr
	astore_2
;  (97) End Assignment
;  (97) End ExprStat
	goto L50
L51:
;  (96) End WhileStat
;  (98) Return Statement
;  (98) Name Expression --
	aload_2
;  (98) End NameExpr
	areturn
;  (98) End ReturnStat
.end method

.method public static printMatrix([[II)V
	.limit stack 50
	.limit locals 10
;  (101) Method Declaration (printMatrix)
;  (102) If Statement
;  (102) Invocation
;  (102) Name Expression --
	aload_0
;  (102) End NameExpr
;  (102) Name Expression --
	iload_1
;  (102) End NameExpr
	invokestatic Assignment5/checkMatrixDim([[II)Z
;  (102) End Invocation
	ifeq L54
;  (103) Local Variable Declaration
;  (103) Literal
	iconst_0
;  (103) End Literal
	istore_2
;  (103) End LocalDecl
;  (104) Local Variable Declaration
;  (104) Literal
	iconst_0
;  (104) End Literal
	istore_3
;  (104) End LocalDecl
;  (105) For Statement
;  (105) Local Variable Declaration
;  (105) Literal
	iconst_0
;  (105) End Literal
	istore 4
;  (105) End LocalDecl
L55:
;  (105) Binary Expression
;  (105) Name Expression --
	iload 4
;  (105) End NameExpr
;  (105) Name Expression --
	iload_1
;  (105) End NameExpr
	if_icmplt L58
	iconst_0
	goto L59
L58:
	iconst_1
L59:
;  (105) End BinaryExpr
	ifeq L56
;  (106) For Statement
;  (106) Local Variable Declaration
;  (106) Literal
	iconst_0
;  (106) End Literal
	istore 5
;  (106) End LocalDecl
L60:
;  (106) Binary Expression
;  (106) Name Expression --
	iload 5
;  (106) End NameExpr
;  (106) Name Expression --
	iload_1
;  (106) End NameExpr
	if_icmplt L63
	iconst_0
	goto L64
L63:
	iconst_1
L64:
;  (106) End BinaryExpr
	ifeq L61
;  (107) If Statement
;  (107) Binary Expression
;  (107) ArrayAccessExpr
;  (107) End ArrayAccessExpr
;  (107) Name Expression --
	iload_3
;  (107) End NameExpr
	if_icmplt L66
	iconst_0
	goto L67
L66:
	iconst_1
L67:
;  (107) End BinaryExpr
	ifeq L65
;  (108) Expression Statement
;  (108) Assignment
;  (108) ArrayAccessExpr
;  (108) End ArrayAccessExpr
	istore_3
;  (108) End Assignment
;  (108) End ExprStat
L65:
;  (107) End IfStat
;  (109) If Statement
;  (109) Binary Expression
;  (109) ArrayAccessExpr
;  (109) End ArrayAccessExpr
;  (109) Name Expression --
	iload_2
;  (109) End NameExpr
	if_icmpgt L69
	iconst_0
	goto L70
L69:
	iconst_1
L70:
;  (109) End BinaryExpr
	ifeq L68
;  (110) Expression Statement
;  (110) Assignment
;  (110) ArrayAccessExpr
;  (110) End ArrayAccessExpr
	istore_2
;  (110) End Assignment
;  (110) End ExprStat
L68:
;  (109) End IfStat
L62:
;  (106) Expression Statement
;  (106) Unary Post Expression
;  (106) Name Expression --
	iload 5
;  (106) End NameExpr
	iinc 5 1
;  (106) End UnaryPostExpr
	pop
;  (106) End ExprStat
	goto L60
L61:
;  (106) End ForStat
L57:
;  (105) Expression Statement
;  (105) Unary Post Expression
;  (105) Name Expression --
	iload 4
;  (105) End NameExpr
	iinc 4 1
;  (105) End UnaryPostExpr
	pop
;  (105) End ExprStat
	goto L55
L56:
;  (105) End ForStat
;  (112) Local Variable Declaration
;  (112) Invocation
;  (112) Binary Expression
;  (112) Literal
	ldc ""
;  (112) End Literal
;  (112) Name Expression --
	iload_3
;  (112) End NameExpr
	iadd
;  (112) End BinaryExpr
;  (112) End Invocation
	istore 5
;  (112) End LocalDecl
;  (113) Local Variable Declaration
;  (113) Invocation
;  (113) Binary Expression
;  (113) Literal
	ldc ""
;  (113) End Literal
;  (113) Name Expression --
	iload_2
;  (113) End NameExpr
	iadd
;  (113) End BinaryExpr
;  (113) End Invocation
	istore 6
;  (113) End LocalDecl
;  (114) Local Variable Declaration
;  (114) Binary Expression
;  (114) Ternary Statement
;  (114) Binary Expression
;  (114) Name Expression --
	iload 5
;  (114) End NameExpr
;  (114) Name Expression --
	iload 6
;  (114) End NameExpr
	if_icmpgt L73
	iconst_0
	goto L74
L73:
	iconst_1
L74:
;  (114) End BinaryExpr
	ifeq L71
;  (114) Name Expression --
	iload 5
;  (114) End NameExpr
	goto L72
L71:
;  (114) Name Expression --
	iload 6
;  (114) End NameExpr
L72:
;  (114) Ternary
;  (114) Literal
	iconst_1
;  (114) End Literal
	iadd
;  (114) End BinaryExpr
	istore 7
;  (114) End LocalDecl
;  (116) For Statement
;  (116) Local Variable Declaration
;  (116) Literal
	iconst_0
;  (116) End Literal
	istore 8
;  (116) End LocalDecl
L75:
;  (116) Binary Expression
;  (116) Name Expression --
	iload 8
;  (116) End NameExpr
;  (116) Name Expression --
	iload_1
;  (116) End NameExpr
	if_icmplt L78
	iconst_0
	goto L79
L78:
	iconst_1
L79:
;  (116) End BinaryExpr
	ifeq L76
;  (117) Expression Statement
;  (117) Invocation
;  (117) Field Reference
;  (117) Name Expression --
;  (117) End NameExpr
	getstatic System/out LIo;
;  (117) End FieldRef
	pop
;  (117) Literal
	ldc "|"
;  (117) End Literal
	invokestatic Io/print(Ljava/lang/String;)V
;  (117) End Invocation
;  (117) End ExprStat
;  (118) For Statement
;  (118) Local Variable Declaration
;  (118) Literal
	iconst_0
;  (118) End Literal
	istore 9
;  (118) End LocalDecl
L80:
;  (118) Binary Expression
;  (118) Name Expression --
	iload 9
;  (118) End NameExpr
;  (118) Name Expression --
	iload_1
;  (118) End NameExpr
	if_icmplt L83
	iconst_0
	goto L84
L83:
	iconst_1
L84:
;  (118) End BinaryExpr
	ifeq L81
;  (119) Expression Statement
;  (119) Invocation
;  (119) Field Reference
;  (119) Name Expression --
;  (119) End NameExpr
	getstatic System/out LIo;
;  (119) End FieldRef
	pop
;  (119) Invocation
;  (119) ArrayAccessExpr
;  (119) End ArrayAccessExpr
;  (119) Name Expression --
	iload 7
;  (119) End NameExpr
	invokestatic Assignment5/rjustify(II)Ljava/lang/String;
;  (119) End Invocation
	invokestatic Io/print(Ljava/lang/String;)V
;  (119) End Invocation
;  (119) End ExprStat
L82:
;  (118) Expression Statement
;  (118) Unary Post Expression
;  (118) Name Expression --
	iload 9
;  (118) End NameExpr
	iinc 9 1
;  (118) End UnaryPostExpr
	pop
;  (118) End ExprStat
	goto L80
L81:
;  (118) End ForStat
;  (120) Expression Statement
;  (120) Invocation
;  (120) Field Reference
;  (120) Name Expression --
;  (120) End NameExpr
	getstatic System/out LIo;
;  (120) End FieldRef
	pop
;  (120) Literal
	ldc " |"
;  (120) End Literal
	invokestatic Io/println(Ljava/lang/String;)V
;  (120) End Invocation
;  (120) End ExprStat
L77:
;  (116) Expression Statement
;  (116) Unary Post Expression
;  (116) Name Expression --
	iload 8
;  (116) End NameExpr
	iinc 8 1
;  (116) End UnaryPostExpr
	pop
;  (116) End ExprStat
	goto L75
L76:
;  (116) End ForStat
L54:
;  (102) End IfStat
	return
.end method

.method public static matrixMult([[I[[II)[[I
	.limit stack 50
	.limit locals 8
;  (125) Method Declaration (matrixMult)
;  (126) If Statement
;  (126) Binary Expression
;  (126) Unary Pre Expression
;  (126) Invocation
;  (126) Name Expression --
	aload_0
;  (126) End NameExpr
;  (126) Name Expression --
	iload_2
;  (126) End NameExpr
	invokestatic Assignment5/checkMatrixDim([[II)Z
;  (126) End Invocation
	iconst_1
	ixor
;  (126) End UnaryPreExpr
	dup
	ifne L87
	pop
;  (126) Unary Pre Expression
;  (126) Invocation
;  (126) Name Expression --
	aload_1
;  (126) End NameExpr
;  (126) Name Expression --
	iload_2
;  (126) End NameExpr
	invokestatic Assignment5/checkMatrixDim([[II)Z
;  (126) End Invocation
	iconst_1
	ixor
;  (126) End UnaryPreExpr
L87:
;  (126) End BinaryExpr
	ifeq L85
;  (127) Return Statement
;  (127) Literal
	aconst_null
;  (127) End Literal
	areturn
;  (127) End ReturnStat
	goto L86
L85:
;  (129) Local Variable Declaration
	astore_3
;  (129) End LocalDecl
;  (130) For Statement
;  (130) Local Variable Declaration
;  (130) Literal
	iconst_0
;  (130) End Literal
	istore 4
;  (130) End LocalDecl
L88:
;  (130) Binary Expression
;  (130) Name Expression --
	iload 4
;  (130) End NameExpr
;  (130) Name Expression --
	iload_2
;  (130) End NameExpr
	if_icmplt L91
	iconst_0
	goto L92
L91:
	iconst_1
L92:
;  (130) End BinaryExpr
	ifeq L89
;  (131) For Statement
;  (131) Local Variable Declaration
;  (131) Literal
	iconst_0
;  (131) End Literal
	istore 5
;  (131) End LocalDecl
L93:
;  (131) Binary Expression
;  (131) Name Expression --
	iload 5
;  (131) End NameExpr
;  (131) Name Expression --
	iload_2
;  (131) End NameExpr
	if_icmplt L96
	iconst_0
	goto L97
L96:
	iconst_1
L97:
;  (131) End BinaryExpr
	ifeq L94
;  (132) Expression Statement
;  (132) Assignment
;  (132) ArrayAccessExpr target
;  (132) ArrayAccessExpr
;  (132) End ArrayAccessExpr
;  (132) ArrayAccessExpr index
;  (132) Name Expression --
	iload 5
;  (132) End NameExpr
;  (132) Literal
	iconst_0
;  (132) End Literal
	iastore
;  (132) End Assignment
;  (132) End ExprStat
L95:
;  (131) Expression Statement
;  (131) Unary Post Expression
;  (131) Name Expression --
	iload 5
;  (131) End NameExpr
	iinc 5 1
;  (131) End UnaryPostExpr
	pop
;  (131) End ExprStat
	goto L93
L94:
;  (131) End ForStat
L90:
;  (130) Expression Statement
;  (130) Unary Post Expression
;  (130) Name Expression --
	iload 4
;  (130) End NameExpr
	iinc 4 1
;  (130) End UnaryPostExpr
	pop
;  (130) End ExprStat
	goto L88
L89:
;  (130) End ForStat
;  (133) For Statement
;  (133) Local Variable Declaration
;  (133) Literal
	iconst_0
;  (133) End Literal
	istore 5
;  (133) End LocalDecl
L98:
;  (133) Binary Expression
;  (133) Name Expression --
	iload 5
;  (133) End NameExpr
;  (133) Name Expression --
	iload_2
;  (133) End NameExpr
	if_icmplt L101
	iconst_0
	goto L102
L101:
	iconst_1
L102:
;  (133) End BinaryExpr
	ifeq L99
;  (134) For Statement
;  (134) Local Variable Declaration
;  (134) Literal
	iconst_0
;  (134) End Literal
	istore 6
;  (134) End LocalDecl
L103:
;  (134) Binary Expression
;  (134) Name Expression --
	iload 6
;  (134) End NameExpr
;  (134) Name Expression --
	iload_2
;  (134) End NameExpr
	if_icmplt L106
	iconst_0
	goto L107
L106:
	iconst_1
L107:
;  (134) End BinaryExpr
	ifeq L104
;  (135) For Statement
;  (135) Local Variable Declaration
;  (135) Literal
	iconst_0
;  (135) End Literal
	istore 7
;  (135) End LocalDecl
L108:
;  (135) Binary Expression
;  (135) Name Expression --
	iload 7
;  (135) End NameExpr
;  (135) Name Expression --
	iload_2
;  (135) End NameExpr
	if_icmplt L111
	iconst_0
	goto L112
L111:
	iconst_1
L112:
;  (135) End BinaryExpr
	ifeq L109
;  (136) Expression Statement
;  (136) Assignment
;  (136) ArrayAccessExpr target
;  (136) ArrayAccessExpr
;  (136) End ArrayAccessExpr
;  (136) ArrayAccessExpr index
;  (136) Name Expression --
	iload 6
;  (136) End NameExpr
	dup2
	iaload
;  (136) Binary Expression
;  (136) ArrayAccessExpr
;  (136) End ArrayAccessExpr
;  (136) ArrayAccessExpr
;  (136) End ArrayAccessExpr
	imul
;  (136) End BinaryExpr
	iadd
	iastore
;  (136) End Assignment
;  (136) End ExprStat
L110:
;  (135) Expression Statement
;  (135) Unary Post Expression
;  (135) Name Expression --
	iload 7
;  (135) End NameExpr
	iinc 7 1
;  (135) End UnaryPostExpr
	pop
;  (135) End ExprStat
	goto L108
L109:
;  (135) End ForStat
L105:
;  (134) Expression Statement
;  (134) Unary Post Expression
;  (134) Name Expression --
	iload 6
;  (134) End NameExpr
	iinc 6 1
;  (134) End UnaryPostExpr
	pop
;  (134) End ExprStat
	goto L103
L104:
;  (134) End ForStat
L100:
;  (133) Expression Statement
;  (133) Unary Post Expression
;  (133) Name Expression --
	iload 5
;  (133) End NameExpr
	iinc 5 1
;  (133) End UnaryPostExpr
	pop
;  (133) End ExprStat
	goto L98
L99:
;  (133) End ForStat
;  (137) Return Statement
;  (137) Name Expression --
	aload_3
;  (137) End NameExpr
	areturn
;  (137) End ReturnStat
L86:
;  (126) End IfStat
	aconst_null
	areturn
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 50
	.limit locals 8
;  (141) Method Declaration (main)
;  (143) For Statement
;  (143) Local Variable Declaration
;  (143) Literal
	iconst_0
;  (143) End Literal
	istore_1
;  (143) End LocalDecl
L113:
;  (143) Binary Expression
;  (143) Name Expression --
	iload_1
;  (143) End NameExpr
;  (143) Literal
	bipush 15
;  (143) End Literal
	if_icmplt L116
	iconst_0
	goto L117
L116:
	iconst_1
L117:
;  (143) End BinaryExpr
	ifeq L114
;  (144) Expression Statement
;  (144) Invocation
;  (144) Field Reference
;  (144) Name Expression --
;  (144) End NameExpr
	getstatic System/out LIo;
;  (144) End FieldRef
	pop
;  (144) Binary Expression
;  (144) Binary Expression
;  (144) Binary Expression
;  (144) Literal
	ldc "fib("
;  (144) End Literal
;  (144) Name Expression --
	iload_1
;  (144) End NameExpr
	iadd
;  (144) End BinaryExpr
;  (144) Literal
	ldc ") = "
;  (144) End Literal
	iadd
;  (144) End BinaryExpr
;  (144) Invocation
;  (144) Name Expression --
	iload_1
;  (144) End NameExpr
	invokestatic Assignment5/fib(I)I
;  (144) End Invocation
	iadd
;  (144) End BinaryExpr
	invokestatic Io/println(Ljava/lang/String;)V
;  (144) End Invocation
;  (144) End ExprStat
L115:
;  (143) Expression Statement
;  (143) Unary Post Expression
;  (143) Name Expression --
	iload_1
;  (143) End NameExpr
	iinc 1 1
;  (143) End UnaryPostExpr
	pop
;  (143) End ExprStat
	goto L113
L114:
;  (143) End ForStat
;  (147) Local Variable Declaration
;  (147) Invocation
;  (147) Literal
	bipush 100
;  (147) End Literal
	invokestatic Assignment5/sieve(I)[I
;  (147) End Invocation
	astore_2
;  (147) End LocalDecl
;  (148) Expression Statement
;  (148) Invocation
;  (148) Field Reference
;  (148) Name Expression --
;  (148) End NameExpr
	getstatic System/out LIo;
;  (148) End FieldRef
	pop
;  (148) Binary Expression
;  (148) Binary Expression
;  (148) Literal
	ldc "There are "
;  (148) End Literal
;  (148) Array length
;  (148) Name Expression --
	aload_2
;  (148) End NameExpr
	arraylength
	iadd
;  (148) End BinaryExpr
;  (149) Literal
	ldc " primes less than 100 and they are {"
;  (149) End Literal
	iadd
;  (148) End BinaryExpr
	invokestatic Io/print(Ljava/lang/String;)V
;  (148) End Invocation
;  (148) End ExprStat
;  (150) For Statement
;  (150) Local Variable Declaration
;  (150) Literal
	iconst_0
;  (150) End Literal
	istore_3
;  (150) End LocalDecl
L118:
;  (150) Binary Expression
;  (150) Name Expression --
	iload_3
;  (150) End NameExpr
;  (150) Array length
;  (150) Name Expression --
	aload_2
;  (150) End NameExpr
	arraylength
	if_icmplt L121
	iconst_0
	goto L122
L121:
	iconst_1
L122:
;  (150) End BinaryExpr
	ifeq L119
;  (151) Expression Statement
;  (151) Invocation
;  (151) Field Reference
;  (151) Name Expression --
;  (151) End NameExpr
	getstatic System/out LIo;
;  (151) End FieldRef
	pop
;  (151) ArrayAccessExpr
;  (151) End ArrayAccessExpr
	invokestatic Io/print(I)V
;  (151) End Invocation
;  (151) End ExprStat
;  (152) If Statement
;  (152) Binary Expression
;  (152) Name Expression --
	iload_3
;  (152) End NameExpr
;  (152) Binary Expression
;  (152) Array length
;  (152) Name Expression --
	aload_2
;  (152) End NameExpr
	arraylength
;  (152) Literal
	iconst_1
;  (152) End Literal
	isub
;  (152) End BinaryExpr
	if_icmplt L124
	iconst_0
	goto L125
L124:
	iconst_1
L125:
;  (152) End BinaryExpr
	ifeq L123
;  (153) Expression Statement
;  (153) Invocation
;  (153) Field Reference
;  (153) Name Expression --
;  (153) End NameExpr
	getstatic System/out LIo;
;  (153) End FieldRef
	pop
;  (153) Literal
	ldc ", "
;  (153) End Literal
	invokestatic Io/print(Ljava/lang/String;)V
;  (153) End Invocation
;  (153) End ExprStat
L123:
;  (152) End IfStat
L120:
;  (150) Expression Statement
;  (150) Unary Post Expression
;  (150) Name Expression --
	iload_3
;  (150) End NameExpr
	iinc 3 1
;  (150) End UnaryPostExpr
	pop
;  (150) End ExprStat
	goto L118
L119:
;  (150) End ForStat
;  (155) Expression Statement
;  (155) Invocation
;  (155) Field Reference
;  (155) Name Expression --
;  (155) End NameExpr
	getstatic System/out LIo;
;  (155) End FieldRef
	pop
;  (155) Literal
	ldc "}"
;  (155) End Literal
	invokestatic Io/println(Ljava/lang/String;)V
;  (155) End Invocation
;  (155) End ExprStat
;  (157) Expression Statement
;  (157) Invocation
;  (157) Field Reference
;  (157) Name Expression --
;  (157) End NameExpr
	getstatic System/out LIo;
;  (157) End FieldRef
	pop
;  (157) Invocation
;  (160) Literal
	iconst_5
;  (160) End Literal
	invokestatic Assignment5/checkMatrixDim([[II)Z
;  (157) End Invocation
	invokestatic Io/println(Z)V
;  (157) End Invocation
;  (157) End ExprStat
;  (162) Expression Statement
;  (162) Invocation
;  (162) Field Reference
;  (162) Name Expression --
;  (162) End NameExpr
	getstatic System/out LIo;
;  (162) End FieldRef
	pop
;  (163) Binary Expression
;  (163) Binary Expression
;  (163) Literal
	ldc "Here is 123 right justified 7: '"
;  (163) End Literal
;  (163) Invocation
;  (163) Literal
	bipush 123
;  (163) End Literal
;  (163) Literal
	iconst_4
;  (163) End Literal
	invokestatic Assignment5/rjustify(II)Ljava/lang/String;
;  (163) End Invocation
	iadd
;  (163) End BinaryExpr
;  (163) Literal
	ldc "'"
;  (163) End Literal
	iadd
;  (163) End BinaryExpr
	invokestatic Io/println(Ljava/lang/String;)V
;  (162) End Invocation
;  (162) End ExprStat
;  (165) Expression Statement
;  (165) Invocation
;  (167) Literal
	iconst_5
;  (167) End Literal
	invokestatic Assignment5/printMatrix([[II)V
;  (165) End Invocation
;  (165) End ExprStat
;  (170) Expression Statement
;  (170) Assignment
	astore 4
;  (170) End Assignment
;  (170) End ExprStat
;  (171) Expression Statement
;  (171) Assignment
	astore 6
;  (171) End Assignment
;  (171) End ExprStat
;  (172) Expression Statement
;  (172) Invocation
;  (172) Field Reference
;  (172) Name Expression --
;  (172) End NameExpr
	getstatic System/out LIo;
;  (172) End FieldRef
	pop
;  (172) Literal
	ldc "F:"
;  (172) End Literal
	invokestatic Io/println(Ljava/lang/String;)V
;  (172) End Invocation
;  (172) End ExprStat
;  (173) Expression Statement
;  (173) Invocation
;  (173) Name Expression --
	iaload 6
;  (173) End NameExpr
;  (173) Literal
	iconst_2
;  (173) End Literal
	invokestatic Assignment5/printMatrix([[II)V
;  (173) End Invocation
;  (173) End ExprStat
;  (174) For Statement
;  (174) Local Variable Declaration
;  (174) Literal
	iconst_1
;  (174) End Literal
	istore 7
;  (174) End LocalDecl
L126:
;  (174) Binary Expression
;  (174) Name Expression --
	iload 7
;  (174) End NameExpr
;  (174) Literal
	bipush 25
;  (174) End Literal
	if_icmple L129
	iconst_0
	goto L130
L129:
	iconst_1
L130:
;  (174) End BinaryExpr
	ifeq L127
;  (175) Expression Statement
;  (175) Assignment
;  (175) Invocation
;  (175) Name Expression --
	iaload 6
;  (175) End NameExpr
;  (175) Name Expression --
	iaload 4
;  (175) End NameExpr
;  (175) Literal
	iconst_2
;  (175) End Literal
	invokestatic Assignment5/matrixMult([[I[[II)[[I
;  (175) End Invocation
	astore 6
;  (175) End Assignment
;  (175) End ExprStat
L128:
;  (174) Expression Statement
;  (174) Unary Post Expression
;  (174) Name Expression --
	iload 7
;  (174) End NameExpr
	iinc 7 1
;  (174) End UnaryPostExpr
	pop
;  (174) End ExprStat
	goto L126
L127:
;  (174) End ForStat
;  (177) Expression Statement
;  (177) Invocation
;  (177) Field Reference
;  (177) Name Expression --
;  (177) End NameExpr
	getstatic System/out LIo;
;  (177) End FieldRef
	pop
;  (177) Literal
	ldc "F^25:"
;  (177) End Literal
	invokestatic Io/println(Ljava/lang/String;)V
;  (177) End Invocation
;  (177) End ExprStat
;  (178) Expression Statement
;  (178) Invocation
;  (178) Name Expression --
	iaload 6
;  (178) End NameExpr
;  (178) Literal
	iconst_2
;  (178) End Literal
	invokestatic Assignment5/printMatrix([[II)V
;  (178) End Invocation
;  (178) End ExprStat
;  (179) Expression Statement
;  (179) Invocation
;  (179) Field Reference
;  (179) Name Expression --
;  (179) End NameExpr
	getstatic System/out LIo;
;  (179) End FieldRef
	pop
;  (179) Binary Expression
;  (179) Literal
	ldc "Fib(25) = "
;  (179) End Literal
;  (179) ArrayAccessExpr
;  (179) End ArrayAccessExpr
	iadd
;  (179) End BinaryExpr
	invokestatic Io/println(Ljava/lang/String;)V
;  (179) End Invocation
;  (179) End ExprStat
;  (181) Expression Statement
;  (181) Invocation
;  (181) Invocation
;  (182) Literal
	iconst_2
;  (182) End Literal
	invokestatic Assignment5/matrixMult([[I[[II)[[I
;  (181) End Invocation
;  (182) Literal
	iconst_2
;  (182) End Literal
	invokestatic Assignment5/printMatrix([[II)V
;  (181) End Invocation
;  (181) End ExprStat
	return
.end method

.method public <init>()V
	.limit stack 50
	.limit locals 1
;  (0) Constructor Declaration
;  (0) Explicit Constructor Invocation
	aload_0
	invokespecial java/lang/Object/<init>()V
;  (0) End CInvocation
;  (0) Field Init Generation Start
;  (0) Field Init Generation End
	return
.end method
