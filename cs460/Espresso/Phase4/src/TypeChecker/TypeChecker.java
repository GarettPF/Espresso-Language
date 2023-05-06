package TypeChecker;

import AST.*;
import Parser.sym;
import Utilities.Error;
import Utilities.SymbolTable;
import Utilities.Visitor;
import java.util.*;
import java.lang.reflect.Field;
import java.math.*;

/**
 * TypeChecker implements the type checker for the Espresso langauge.
 */
public class TypeChecker extends Visitor {
    
    /**
     * <p>Returns the 'best-fitting' method or constructor from a list 
     * of potential candidates given a set of actual parameters.</p>
     * <p>See Section 7.2.9: Finding the Right Method to Call in Practical Compiler Construction.</p>
     * <p>Remember to visit all the arguments BEFORE calling findMethod. findMethod uses the <i>.type</i> field of the parameters.</p>
     * @param candidateMethods Sequence of methods or constructors. Use {@link AST.ClassDecl#allMethods} or {@link AST.ClassDecl#constructors} from the appropriate {@link AST.ClassDecl}.
     * @param name The name of the method or constructor you are looking for.
     * @param actualParams The sequence of actual parameters being passed to the method invocation or constructor invocation. 
     * @param lookingForMethods <i>true</i> if you pass a sequence of methods ({@link AST.ClassDecl#allMethods}), <i>false</i> if you pass a sequence of constructors ({@link AST.ClassDecl#constructors}).
     * @return The {@link AST.MethodDecl}/{@link AST.ConstructorDecl} found; null if nothing was found.
     */
    public static ClassBodyDecl findMethod(Sequence candidateMethods, String name, Sequence actualParams, 
					   boolean lookingForMethods) {
	
	if (lookingForMethods) {
	    println("+------------- findMethod (Method) ------------");
	    println("| Looking for method: " + name);
	} else {
	    println("+---------- findMethod (Constructor) ----------");
	    println("| Looking for constructor: " + name);
	}
	println("| With parameters:");
	for (int i=0; i<actualParams.nchildren; i++){
	    println("|   " + i + ". " + ((actualParams.children[i] instanceof ParamDecl)?(((ParamDecl)actualParams.children[i]).type()):((Expression)actualParams.children[i]).type));
	}
	// The number of actual parameters in the invocation.
	int count = 0;
	
	// Make an array big enough to hold all the methods if needed
	ClassBodyDecl cds[] = new ClassBodyDecl[candidateMethods.nchildren];
	
	// Initialize the array to point to null
	for(int i=0;i<candidateMethods.nchildren;i++) 
	    cds[i] = null;
	
	Sequence args = actualParams;
	Sequence params;
	
	// Insert all the methods from the symbol table that:
	// 1.) has the right number of parameters
	// 2.) each formal parameter can be assigned its corresponding
	//     actual parameter.
	if (lookingForMethods)
	    println("| Finding methods with the right number of parameters and types");
	else
	    println("| Finding constructors with the right number of parameters and types");
	for (int cnt=0; cnt<candidateMethods.nchildren; cnt++) {
	    ClassBodyDecl cbd = (ClassBodyDecl)candidateMethods.children[cnt];
	    
	    // if the method doesn't have the right name, move on!
	    if (!(cbd.getname().equals(name)))
		continue;
	    
	    // Fill params with the formal parameters.
	    if (cbd instanceof ConstructorDecl) 
		params = ((ConstructorDecl)cbd).params();
	    else if (cbd instanceof MethodDecl)
		params = ((MethodDecl)cbd).params();
	    else
		// we have a static initializer, don't do anything - just skip it.
		continue;
	    
	    print("|   " + name + "(");
	    if (cbd instanceof ConstructorDecl) 
		print(Type.parseSignature(((ConstructorDecl)cbd).paramSignature()));
	    else 
		print(Type.parseSignature(((MethodDecl)cbd).paramSignature()));
	    print(" )  ");
	    
	    if (args.nchildren == params.nchildren) {
		// The have the same number of parameters
		// now check that the formal parameters are
		// assignmentcompatible with respect to the 
		// types of the actual parameters.
		// OBS this assumes the type field of the actual
		// parameters has been set (in Expression.java),
		// so make sure to call visit on the parameters first.
		boolean candidate = true;
		
		for (int i=0;i<args.nchildren; i++) {
		    candidate = candidate &&
			Type.assignmentCompatible(((ParamDecl)params.children[i]).type(),
						  (args.children[i] instanceof Expression) ?
						  ((Expression)args.children[i]).type :
						  ((ParamDecl)args.children[i]).type());
		    
		    if (!candidate) {
			println(" discarded");
			break;
		    }
		}
		if (candidate) {
		    println(" kept");
		    cds[count++] = cbd;
		}
	    }
	    else {
		println(" discarded");
	    }
	    
	}
	// now count == the number of candidates, and cds is the array with them.
	// if there is only one just return it!
	println("| " + count + " candidate(s) were found:");
	for ( int i=0;i<count;i++) {
	    ClassBodyDecl cbd = cds[i];
	    print("|   " + name + "(");
	    if (cbd instanceof ConstructorDecl) 
		print(Type.parseSignature(((ConstructorDecl)cbd).paramSignature()));
	    else 
		print(Type.parseSignature(((MethodDecl)cbd).paramSignature()));
	    println(" )");
	}
	
	if (count == 0) {
	    println("| No candidates were found.");
	    println("+------------- End of findMethod --------------");
	    return null;
	}
	
	if (count == 1) {
	    println("| Only one candidate - thats the one we will call then ;-)");
	    println("+------------- End of findMethod --------------");
	    return cds[0];
	}
	println("| Oh no, more than one candidate, now we must eliminate some >:-}");
	// there were more than one candidate.
	ClassBodyDecl x,y;
	int noCandidates = count;
	
	for (int i=0; i<count; i++) {
	    // take out a candidate
	    x = cds[i];
	    
	    if (x == null)
		continue;		    
	    cds[i] = null; // this way we won't find x in the next loop;
	    
	    // compare to all other candidates y. If any of these
	    // are less specialised, i.e. all types of x are 
	    // assignment compatible with those of y, y can be removed.
	    for (int j=0; j<count; j++) {
		y = cds[j];
		if (y == null) 
		    continue;
		
		boolean candidate = true;
		
		// Grab the parameters out of x and y
		Sequence xParams, yParams;
		if (x instanceof ConstructorDecl) {
		    xParams = ((ConstructorDecl)x).params();
		    yParams = ((ConstructorDecl)y).params();
		} else {
		    xParams = ((MethodDecl)x).params();
		    yParams = ((MethodDecl)y).params();
		}
		
		// now check is y[k] <: x[k] for all k. If it does remove y.
		// i.e. check if y[k] is a superclass of x[k] for all k.
		for (int k=0; k<xParams.nchildren; k++) {
		    candidate = candidate &&
			Type.assignmentCompatible(((ParamDecl)yParams.children[k]).type(),
						  ((ParamDecl)xParams.children[k]).type());
		    
		    if (!candidate)
			break;
		}
		if (candidate) {
		    // x is more specialized than y, so throw y away.
		    print("|   " + name + "(");
		    if (y instanceof ConstructorDecl) 
			print(Type.parseSignature(((ConstructorDecl)y).paramSignature()));
		    else 
			print(Type.parseSignature(((MethodDecl)y).paramSignature()));
		    print(" ) is less specialized than " + name + "(");
		    if (x instanceof ConstructorDecl) 
			print(Type.parseSignature(((ConstructorDecl)x).paramSignature()));
		    else 
			print(Type.parseSignature(((MethodDecl)x).paramSignature()));
		    println(" ) and is thus thrown away!");
		    
		    cds[j] = null;
		    noCandidates--;
		}
	    }
	    // now put x back in to cds
	    cds[i] = x;
	}
	if (noCandidates != 1) {
	    // illegal function call
	    println("| There is more than one candidate left!");
	    println("+------------- End of findMethod --------------");
	    return null;
	}
	
	// just find it and return it.
	println("| We were left with exactly one candidate to call!");
	println("+------------- End of findMethod --------------");
	for (int i=0; i<count; i++)
	    if (cds[i] != null)
		return cds[i];
	
	return null;
    }

    /**
     * Given a list of candiate methods and a name of the method this method prints them all out.
     *
     * @param cd The {@link AST.ClassDecl} for which the methods or constructors are being listed.
     * @param candidateMethods A {@link AST.Sequence} of either {@link AST.MethodDecl}s ({@link AST.ClassDecl#allMethods}) or {@link AST.ConstructorDecl}s ({@link AST.ClassDecl#constructors}). 
     * @param name The name of the method or the constructor for which the candidate list should be produced.
     */
    public void listCandidates(ClassDecl cd, Sequence candidateMethods, String name) {

	for (int cnt=0; cnt<candidateMethods.nchildren; cnt++) {
	    ClassBodyDecl cbd = (ClassBodyDecl)(candidateMethods.children[cnt]);

	    if (cbd.getname().equals(name)) {
		if (cbd instanceof MethodDecl)
		    System.out.println("  " + name + "(" + Type.parseSignature(((MethodDecl)cbd).paramSignature()) + " )");
		else
		    System.out.println("  " + cd.name() + "(" + Type.parseSignature(((ConstructorDecl)cbd).paramSignature()) + " )");
	    }
	}
    }
    /**
     * The global class tabel. This should be set in the constructor.
     */
    private SymbolTable   classTable;
    /**
     * The class of which children are currently being visited. This should be updated when visiting a {@link AST.ClassDecl}.
     */
    private ClassDecl     currentClass;     
    /**
     * The particular {@link AST.ClassBodyDecl} (except {@link AST.FieldDecl}) of which children are currently being visited.
     */
    private ClassBodyDecl currentContext;  
    /**
     * The current {@link AST.FieldDecl} of which children are currently being visited (if applicable).
     */
    private FieldDecl     currentFieldDecl; 
    /**
     * Indicates if children being visited are part of a {@link AST.FieldDecl} initializer. (accessible though {@link AST.FieldDecl#var()}). Used for determining forward reference of a non-initialized field. You probably don't want to bother with this one.
     */
    private boolean       inFieldInit;      

    /**
     * Constructs a new type checker.
     * @pisaram classTable The global class table.
     * @param debug determins if debug information should printed out.
     */
    public TypeChecker(SymbolTable classTable, boolean debug) { 
	this.classTable = classTable; 
	this.debug = debug;
    }

    /** v
     * @param ae An {@link AST.ArrayAccessExpr} parse tree node.
     * @return Returns the type of the array access expression.
     */
    public Object visitArrayAccessExpr(ArrayAccessExpr ae) {
	println(ae.line + ": Visiting ArrayAccessExpr");
	// YOUR CODE HERE
	return ae.type;
    }

    /** 
     * @param ae An {@link AST.ArrayType} parse tree node.
     * @return Returns itself.
     */
    public Object visitArrayType(ArrayType at) {
	println(at.line + ": Visiting an ArrayType");
	println(at.line + ": ArrayType type is " + at);
	// An ArrayType is already a type, so nothing to do.
	return at;
    }

    /** NewArray */
    public Object visitNewArray(NewArray ne) {
	println(ne.line + ": Visiting a NewArray " + ne.dimsExpr().nchildren + " " + ne.dims().nchildren);
	// YOUR CODE HERE
	println(ne.line + ": NewArray type is " + ne.type);
	return ne.type;
    }

    // arrayAssignmentCompatible: Determines if the expression 'e' can be assigned to the type 't'.
    // See Section 7.2.6 sub-heading 'Constructed Types'
    public boolean arrayAssignmentCompatible(Type t, Expression e) {
	if (t instanceof ArrayType && (e instanceof ArrayLiteral)) {
	    ArrayType at = (ArrayType)t;
	    e.type = at; //  we don't know that this is the type - but if we make it through it will be!
	    ArrayLiteral al = (ArrayLiteral)e;
	    
	    // t is an array type i.e. XXXXXX[ ]
	    // e is an array literal, i.e., { }
	    if (al.elements().nchildren == 0) // the array literal is { }
		return true;   // any array variable can hold an empty array
	    // Now check that XXXXXX can hold value of the elements of al
	    // we have to make a new type: either the base type if |dims| = 1
	    boolean b = true;
	    for (int i=0; i<al.elements().nchildren; i++) {
		if (at.getDepth() == 1) 
		    b = b && arrayAssignmentCompatible(at.baseType(), (Expression)al.elements().children[i]);
		else { 
		    ArrayType at1 = new ArrayType(at.baseType(), at.getDepth()-1);
		    b = b  && arrayAssignmentCompatible(at1, (Expression)al.elements().children[i]);
		}
	    }
	    return b;
	} else if (t instanceof ArrayType && !(e instanceof ArrayLiteral)) {
	    Type t1 = (Type)e.visit(this);
	    if (t1 instanceof ArrayType)
		if (!Type.assignmentCompatible(t,t1))
		    Error.error("Incompatible type in array assignment");
		else
		    return true;
	    Error.error(t, "Error: cannot assign non array to array type " + t.typeName());	    
	}
	else if (!(t instanceof ArrayType) && (e instanceof ArrayLiteral)) {
	    Error.error(t, "Error: cannot assign value " + ((ArrayLiteral)e).toString() + " to type " + t.typeName());
	}
	return Type.assignmentCompatible(t,(Type)e.visit(this));
    }
    
    public Object visitArrayLiteral(ArrayLiteral al) {
	// Espresso does not allow array literals without the 'new <type>' part.
	Error.error(al, "Array literal must be preceeded by a 'new <type>'");
	return null;
    }
    
    /** ASSIGNMENT */
    public Object visitAssignment(Assignment as) {
	println(as.line + ": Visiting an assignment");

	// get the types of the LHS (v) and the RHS(e)
	Type vType = (Type) as.left().visit(this);
	Type eType = (Type) as.right().visit(this);

	/** Note: as.left() should be of NameExpr or FieldRef class! */

	if (!vType.assignable())          
	    Error.error(as,"Left hand side of assignment not assignable.");

	// Cannot assign to a classname
	if (as.left() instanceof NameExpr && (((NameExpr)as.left()).myDecl instanceof ClassDecl))
	    Error.error(as,"Left hand side of assignment not assignable.");

	// Now switch on the operator
	switch (as.op().kind) {
	case AssignmentOp.EQ : {
	    // Check if the right hand side is a constant.	    
	    // if we don't do this the following is illegal: byte b; b = 4; because 4 is an int!
	    if (as.right().isConstant()) {
		if (vType.isShortType() && Literal.isShortValue(((BigDecimal)as.right().constantValue()).longValue()))
		    break;
		if (vType.isByteType() && Literal.isByteValue(((BigDecimal)as.right().constantValue()).longValue()))
		    break;		
		if (vType.isCharType() && Literal.isCharValue(((BigDecimal)as.right().constantValue()).longValue()))
		    break;
	    }
		     
	    // Now just check for assignment compatability
	    if (!Type.assignmentCompatible(vType,eType))
		Error.error(as,"Cannot assign value of type " + eType.typeName() + " to variable of type " + vType.typeName() + ".");
	    break;
	}
	}

	// YOUR CODE HERE
	if (!vType.isIntegralType() && as.op().operator().equals(">>=")) {
		println("Error: Left hand side operand of operator '>>=' must be of integral type.");
		System.exit(1);
	} else if (!eType.isIntegralType() && as.op().operator().equals(">>=")) {
		println("Error: Right hand side operand of operator '>>=' must be of integral type.");
		System.exit(1);
	} else if (vType.isIntegerType() && eType.isDoubleType()) {
		println("Error: Cannot assign value of type double to variable of type int.");
		System.exit(1);
	} else if (vType.isIntegerType() && eType.isStringType()) {
		println("Error: Cannot assign value of type String to variable of type int.");
		System.exit(1);
	} else if (!(vType.isBooleanType() && vType.identical(eType)) &&
		!(vType.isIntegralType() && vType.identical(eType)) &&
		as.op().operator().equals("&=")) {
		println("Error: Both right and left hand side operands of operator '&=' must be either of boolean or similar integral type.");
		System.exit(1);
	}


	// The overall type is always that of the LHS.
	as.type = vType;
	println(as.line + ": Assignment has type: " + as.type);

	return vType;
    }

    /** BINARY EXPRESSION */
    public Object visitBinaryExpr(BinaryExpr be) {
	println(be.line + ": Visiting a Binary Expression");

	// YOUR CODE HERE
	Type lt = (Type)be.left().visit(this);
	Type rt = (Type)be.right().visit(this);
	String op = be.op().operator();
	
	String[] primitiveOps = {"+", "-", "*", "/", "%", "<<", ">>", ">>>", "&", "|", "^"};
	boolean isCompare = true;
	for (int i = 0; i < primitiveOps.length; i++) {
		if (op.equals(primitiveOps[i]))
			isCompare = false;
	}

	
	// print(classTable.toString());
	// println(String.format("%s %s %s", lt, op, rt));
	// println(String.format("%s", ((NameExpr)be.left()).myDecl.getClass().getSimpleName()));
	if (isCompare) {
		if (lt.isCharType() && rt.isIntegerType())
			be.type = new PrimitiveType(PrimitiveType.IntKind);
		else if ((lt.isVoidType() && rt.isVoidType()) && (op.equals("!=") || op.equals("=="))) {
			println("Error: Void type cannot be used here.");
			System.exit(1);
		}
		else if (!lt.isBooleanType() && !rt.isBooleanType() && op.equals("&&")) {
			println("Error: Operator '&&' requires operands of boolean type.");
			System.exit(1);
		}
		else if (op.equals(">") && (!lt.isNumericType() || !rt.isNumericType())) {
			println("Error: Operator '>' requires operands of numeric type.");
			System.exit(1);
		}
		else if ((op.equals("!=") || op.equals("==")) && lt instanceof ClassType && rt instanceof ClassType) {
			println(String.format("Error: Class name '%s' cannot appear as parameter to operator '!='.",
				lt.typeName()));
			System.exit(1);
		}
		else if (lt.identical(rt) && !op.equals("instanceof"))
			be.type = new PrimitiveType(PrimitiveType.BooleanKind);
		else if (lt instanceof ClassType && rt instanceof ClassType) {
			if (((NameExpr)be.right()).myDecl instanceof LocalDecl &&
			classTable.get(((LocalDecl)((NameExpr)be.right()).myDecl).name()) == null &&
			op.equals("instanceof")) {
				println(String.format("Error: '%s' is not a class name.", ((LocalDecl)((NameExpr)be.right()).myDecl).name()));
				System.exit(1);
			} 
			else if (((NameExpr)be.left()).myDecl instanceof LocalDecl &&
				classTable.get(((LocalDecl)((NameExpr)be.left()).myDecl).name()) != null &&
				op.equals("instanceof")) {
				println("Error: Left hand side of instanceof cannot be a class.");
				System.exit(1);
			}
			else if (((NameExpr)be.left()).myDecl instanceof ClassDecl && op.equals("instanceof")){
				println("Error: Left hand side of instanceof cannot be a class.");
				System.exit(1);
			}
			else
				be.type = new PrimitiveType(PrimitiveType.BooleanKind);
		}
		else if (op.equals("instanceof") && !(((NameExpr)be.left()).myDecl instanceof ClassDecl)) {
			println("Error: Left hand side of instanceof needs expression of class type");
			System.exit(1);
		}
		else {
			println(String.format("Error: Operator '%s' requires operands of the same type.", be.op().operator()));
			System.exit(1);
		}
	} else {
		if (lt.isIntegerType() && rt.isIntegerType() && 
			(op.equals("*") || op.equals("/") || op.equals("+") || op.equals("-") || op.equals("%")))
			be.type = new PrimitiveType(PrimitiveType.IntKind);
		else if (lt.isCharType() && rt.isIntegerType() && op.equals("+"))
			be.type = new PrimitiveType(PrimitiveType.IntKind);
		else if (lt.isIntegerType() && rt.isIntegerType() && op.equals("<<"))
			be.type = new PrimitiveType(PrimitiveType.IntKind);
		else if (op.equals("+") && !(lt.isNumericType() && rt.isNumericType())) {
			println("Error: Operator '+' requires operands of numeric type.");
			System.exit(1);
		}
		else if (op.equals(">>") && !rt.isIntegralType()) {
			println("Error: Operator '>>' requires right operand of integral type.");
			System.exit(1);
		}
		else if (op.equals(">>") && !lt.isIntegralType()) {
			println("Error: Operator '>>' requires left operand of integral type.");
			System.exit(1);
		}
		else if (op.equals("&") && (
			!(lt.isIntegralType() && rt.isIntegralType()) &&
			!(lt.isBooleanType() && rt.isBooleanType())) ) {
			println("Error: Operator '&' requires both operands of either integral or boolean type.");
			System.exit(1);
		}

	}
	if (be.type == null)
		System.exit(1);


	println(be.line + ": Binary Expression has type: " + be.type);
	return be.type;
    }

    /** CAST EXPRESSION */
    public Object visitCastExpr(CastExpr ce) {
	println(ce.line + ": Visiting a cast expression");

	// We have two different types of casts:
	// Numeric: any numeric type can be cast to any other numeric type.
	// Class: (A)e, where Type(e) is B. This is legal ONLY if A :> B or B :> A
	//        that is either A has to be above B in the class hierarchy or 
	//        B has to be above A.
	// Do note that if the cast type and the expression type are identical,
	// then the cast is fine: for example (String)"Hello" or (Boolean)true.
	//
	// One small caveat: (A)a is not legal is 'a' is a class name. So if the 
	// expression is a NameExpr then it cannot be the name of a class - that is,
	// its myDecl cannot be a ClassDecl.
	// YOUR CODE HERE
	Type etype = (Type)ce.expr().visit(this);
	// println(String.format("(%s)%s", ce.type(), etype));
	// println(etype.getClass().getSimpleName() + ce.type().getClass().getSimpleName());
	if (etype instanceof PrimitiveType && ce.type() instanceof PrimitiveType) {
		PrimitiveType cetype = (PrimitiveType)ce.type();
		PrimitiveType etype_ = (PrimitiveType)etype;

		// cast to byte
		if (cetype.getKind() == PrimitiveType.ByteKind) {
			switch (etype_.getKind()) {
				case PrimitiveType.ShortKind:
				case PrimitiveType.CharKind:
				case PrimitiveType.IntKind:
				case PrimitiveType.LongKind:
				case PrimitiveType.FloatKind:
				case PrimitiveType.DoubleKind:
					ce.type = ce.type();
			}
		}

		// cast to short
		if (cetype.getKind() == PrimitiveType.ShortKind) {
			switch (etype_.getKind()) {
				case PrimitiveType.ByteKind:
				case PrimitiveType.CharKind:
				case PrimitiveType.IntKind:
				case PrimitiveType.LongKind:
				case PrimitiveType.FloatKind:
				case PrimitiveType.DoubleKind:
					ce.type = ce.type();
			}
		}

		// cast to char
		if (cetype.getKind() == PrimitiveType.CharKind) {
			switch (etype_.getKind()) {
				case PrimitiveType.IntKind:
				case PrimitiveType.ShortKind:
				case PrimitiveType.LongKind:
				case PrimitiveType.FloatKind:
				case PrimitiveType.DoubleKind:
					ce.type = ce.type();
			}
		}
		// cast to int
		if (cetype.getKind() == PrimitiveType.IntKind) {
			switch(etype_.getKind()) {
				case PrimitiveType.IntKind:
				case PrimitiveType.LongKind:
				case PrimitiveType.FloatKind:
				case PrimitiveType.DoubleKind:
				case PrimitiveType.CharKind:
					ce.type = ce.type();
			}
		}

		// cast to long
		if (cetype.getKind() == PrimitiveType.LongKind) {
			switch(etype_.getKind()) {
				case PrimitiveType.IntKind:
				case PrimitiveType.FloatKind:
				case PrimitiveType.DoubleKind:
					ce.type = ce.type();
			}
		}

		// cast to float
		if (cetype.getKind() == PrimitiveType.FloatKind) {
			switch(etype_.getKind()) {
				case PrimitiveType.IntKind:
				case PrimitiveType.LongKind:
				case PrimitiveType.DoubleKind:
					ce.type = ce.type();
			}
		}

		// cast to double
		if (cetype.getKind() == PrimitiveType.DoubleKind) {
			switch(etype_.getKind()) {
				case PrimitiveType.IntKind:
				case PrimitiveType.FloatKind:
				case PrimitiveType.LongKind:
					ce.type = ce.type();
			}
		}
		
	} else if (etype instanceof ClassType && ce.type() instanceof ClassType) {
		ClassType lhs = (ClassType)ce.type();
		ClassType rhs = (ClassType)etype;
		if (Type.isSuper(lhs, rhs) || Type.isSuper(rhs, lhs))
			ce.type = lhs;
		
	} else if (etype instanceof ClassType && ce.type() instanceof PrimitiveType) {
		println(String.format("Error: Illegal type cast. Cannot cast type '%s' to type '%s'.", 
			etype.typeName(),
			((PrimitiveType)ce.type()).typeName()));
		System.exit(1);
	}

	if (ce.expr() instanceof NameExpr && ((NameExpr)ce.expr()).myDecl instanceof ClassDecl) {
		println(String.format("Error: Cannot use class name '%s'. Object name expected in cast.",
				((ClassDecl)((NameExpr)ce.expr()).myDecl).className()));
		System.exit(1);
	}

	if (ce.type == null) System.exit(1);
	// The overall type of a cast expression is always the cast type.

	println(ce.line + ": Cast Expression has type: " + ce.type);
	return ce.type;
    }

    /** CLASSTYPE */
    public Object visitClassType(ClassType ct) {
	println(ct.line + ": Visiting a class type");
	// A class type is alreayd a type, so nothing to do.
	println(ct.line + ": Class Type has type: " + ct);
	return ct;
    }

    /** CONSTRUCTOR (EXPLICIT) INVOCATION */
    public Object visitCInvocation(CInvocation ci) {
	println(ci.line + ": Visiting an explicit constructor invocation");

	// An explicit constructor invocation takes one of two forms:
	// this ( ... )  -- this calls a constructor in the same class (currentClass)
	// super ( ... ) -- this calls a constructor in the super class (of currentClass)

	// YOUR CODE HERE
	super.visitCInvocation(ci);
	findMethod(currentClass.superClass().myDecl.constructors, 
				currentClass.superClass().name().toString(), 
				ci.args(), 
				false);

	return null;
    }

    /** CLASS DECLARATION */
    public Object visitClassDecl(ClassDecl cd) {
	println(cd.line + ": Visiting a class declaration " + cd.name());

	// The only check to do here is that we cannot have repreated interface implementations.
	// E.g.: class A implements I, I { ... } is illegal.

	// Update the current class.
	currentClass = cd;
	// YOUR CODE HERE
	super.visitClassDecl(cd);

	return null;
    }

    /** CONSTRUCTOR DECLARATION */
    public Object visitConstructorDecl(ConstructorDecl cd) {
	println(cd.line + ": Visiting a constructor declaration");

	// Update the current context
	currentContext = cd;

	// YOUR CODE HERE
	if (cd.cinvocation() != null)
		cd.cinvocation().constructor = cd;
	super.visitConstructorDecl(cd);

	return null;
    }

    /** DO STATEMENT */
    public Object visitDoStat(DoStat ds) {
	println(ds.line + ": Visiting a do statement");

	// YOUR CODE HERE
	PrimitiveType expr = (PrimitiveType)ds.expr().visit(this);
	if (!expr.isBooleanType()) {
		println("Error: Non boolean Expression found as test in do-statement.");
		System.exit(1);
	}

	if (ds.stat() != null) ds.stat().visit(this);

	return null;
    }

    /** FIELD DECLARATION */
    public Object visitFieldDecl(FieldDecl fd) {
	println(fd.line + ": Visiting a field declaration");

	// Update the current context
	currentContext = fd;
	// set inFieldInit to true as we are about to visit the field initializer.
	// (happens from visitVar() if it isn't null.
	inFieldInit = true;
	// Set the current field.
	currentFieldDecl = fd;
	// Visit the var
	if (fd.var().init() != null)
	    fd.var().visit(this);
	// Set current field back to null (fields cannot be nested, so this is OK)
	currentFieldDecl = null;
	// set ifFieldInit back to false as we are done with the initializer.
	inFieldInit = false;

	return fd.type();
    }

    /** FIELD REFERENCE */
    public Object visitFieldRef(FieldRef fr) {
	println(fr.line + ": Visiting a field reference" + fr.target());

	Type targetType = (Type) fr.target().visit(this);
	String field    = fr.fieldName().getname();
	// Changed June 22 2012 ARRAY
	if (fr.fieldName().getname().equals("length")) {
	    if (targetType.isArrayType()) {
		fr.type = new PrimitiveType(PrimitiveType.IntKind);
		println(fr.line + ": Field Reference was a an Array.length reference, and it has type: " + fr.type);
		fr.targetType = targetType;
		return fr.type;
	    }
	}

	if (targetType.isClassType()) {
	    ClassType c = (ClassType)targetType;
	    ClassDecl cd = c.myDecl;
	    fr.targetType = targetType;

	    println(fr.line + ": FieldRef: Looking up symbol '" + field + "' in fieldTable of class '" + 
		    c.typeName() + "'.");

	    // Lookup field in the field table of the class associated with the target.
	    FieldDecl lookup = (FieldDecl) NameChecker.NameChecker.getField(field, cd);

	    // Field not found in class.
	    if (lookup == null)
		Error.error(fr,"Field '" + field + "' not found in class '" + cd.name() + "'.");
	    else {
		fr.myDecl = lookup;
		fr.type = lookup.type();
	    }
	} else 
	    Error.error(fr,"Attempt to access field '" + field + "' in something not of class type.");
	println(fr.line + ": Field Reference has type: " + fr.type);

	/*if (inFieldInit && currentFieldDecl.fieldNumber <= fr.myDecl.fieldNumber && currentClass.name().equals(   (((ClassType)fr.targetType).myDecl).name()))
	    Error.error(fr,"Illegal forward reference of non-initialized field.");
	*/
	return fr.type;
    }

    /** FOR STATEMENT */
    public Object visitForStat(ForStat fs) {
	println(fs.line + ": Visiting a for statement");

	// YOUR CODE HERE
	PrimitiveType expr;

	fs.init().visit(this);
	if (fs.expr() != null) {
		expr = (PrimitiveType)fs.expr().visit(this);
		if (!expr.isBooleanType()) {
			println("Error: Non boolean Expression found in for-statement.");
			System.exit(1);
		}
	}
	fs.incr().visit(this);


	if (fs.stats() != null) fs.stats().visit(this);

	return null;
    }

    /** IF STATEMENT */
    public Object visitIfStat(IfStat is) {
	println(is.line + ": Visiting a if statement");

	// YOUR CODE HERE
	PrimitiveType expr = (PrimitiveType)is.expr().visit(this);
	if (!expr.isBooleanType()) {
		println("Error: Non boolean Expression found as test in if-statement.");
		System.exit(1);
	}

	is.thenpart().visit(this);
	if (is.elsepart() != null) is.elsepart().visit(this);

	return null;
    }

    /** INVOCATION */
    public Object visitInvocation(Invocation in) {
	println(in.line + ": Visiting an Invocation");

	// YOUR CODE HERE

	if (in.target() != null) {
		Type tType = (Type)in.target().visit(this);
		if (!(tType instanceof ClassType)) {
			println(String.format("Error: Attempt to invoke method '%s' in something not of class type.",
				in.methodName()));
			System.exit(1);
		}
		in.params().visit(this);
		MethodDecl md = (MethodDecl)findMethod(((ClassType)tType).myDecl.allMethods, 
												in.methodName().toString(),
												in.params(),
												true);
		in.type = md.returnType();
	} else {
		super.visitInvocation(in);
		MethodDecl md = (MethodDecl)findMethod(currentClass.allMethods,
												in.methodName().toString(),
												in.params(),
												true);
		in.type = md.returnType();
	}

	println(in.line + ": Invocation has type: " + in.type);
	return in.type;
    }

    /** LITERAL */
    public Object visitLiteral(Literal li) {
	println(li.line + ": Visiting a literal (" + li.getText() + ")");

	// YOUR CODE HERE
	if (li.getKind() == Literal.NullKind)
		li.type = new NullType(li);
	else
		li.type = new PrimitiveType(li.getKind());

	println(li.line + ": Literal has type: " + li.type);
	return li.type;
    }

    /** METHOD DECLARATION */
    public Object visitMethodDecl(MethodDecl md) {
	println(md.line + ": Visiting a method declaration");
	currentContext = md;

	// YOUR CODE HERE
	super.visitMethodDecl(md);

	return null;
    }

    /** NAME EXPRESSION */
    public Object visitNameExpr(NameExpr ne) {
	println(ne.line + ": Visiting a Name Expression");

	// YOUR CODE HERE
	if (ne.isClassName()) {
		ClassType ctype = new ClassType(ne.name());
		ctype.myDecl = (ClassDecl)ne.myDecl;
		ne.type = ctype;
	} else {
		ne.type = ((VarDecl)ne.myDecl).type();
	}

	println(ne.line + ": Name Expression has type: " + ne.type);
	return ne.type;
    }

    /** NEW */
    public Object visitNew(New ne) {
	println(ne.line + ": Visiting a new");

	// YOUR CODE HERE
	super.visitNew(ne);

	ConstructorDecl md = (ConstructorDecl)findMethod(ne.type().myDecl.constructors,
											ne.type().name().toString(),
											ne.args(),
											false);
	ne.type = ne.type();

	println(ne.line + ": New has type: " + ne.type);
	return ne.type;
    }


    /** RETURN STATEMENT */
    public Object visitReturnStat(ReturnStat rs) {
	println(rs.line + ": Visiting a return statement");
	Type returnType;

	if (currentContext instanceof MethodDecl)
	    returnType = ((MethodDecl)currentContext).returnType();
	else
	    returnType = null;

	// Check is there is a return in a Static Initializer
	if (currentContext instanceof StaticInitDecl) 
	    Error.error(rs,"return outside method");

	// Check if a void method is returning something.
	if (returnType == null || returnType.isVoidType()) {
	    if (rs.expr() != null)
		Error.error(rs, "Return statement of a void function cannot return a value.");
	    return null;
	}

	// Check if a non void method is returning without a proper value.
	if (rs.expr() == null && returnType != null)
	    Error.error(rs, "Non void function must return a value.");

	Type returnValueType = (Type) rs.expr().visit(this);	
	if (rs.expr().isConstant()) {
	    if (returnType.isShortType() && Literal.isShortValue(((BigDecimal)rs.expr().constantValue()).longValue()))
		;// is ok break;                                                                                                    
	    else if (returnType.isByteType() && Literal.isByteValue(((BigDecimal)rs.expr().constantValue()).longValue()))
		; // is ok break;                                                                                                   
	    else if (returnType.isCharType() && Literal.isCharValue(((BigDecimal)rs.expr().constantValue()).longValue()))
		; // break;
	    else if (!Type.assignmentCompatible(returnType,returnValueType))
		Error.error(rs, "Illegal value of type " + returnValueType.typeName() + 
			    " in method expecting value of type " + returnType.typeName() + ".");
	} else if (!Type.assignmentCompatible(returnType,returnValueType))
	    Error.error(rs, "Illegal value of type " + returnValueType.typeName() + 
			" in method expecting value of type " + returnType.typeName() + ".");
	rs.setType(returnType);
	return null;
    }

    /** STATIC INITIALIZER */
    public Object visitStaticInitDecl(StaticInitDecl si) {
	println(si.line + ": Visiting a static initializer");

	// YOUR CODE HERE
	super.visitStaticInitDecl(si);

	return null;
    }

    /** SUPER */
    public Object visitSuper(Super su) {
	println(su.line + ": Visiting a super");

	// YOUR CODE HERE
	su.type = currentClass.superClass();
	println(su.line + ": Super has type: " + su.type);

	return su.type;
    }

    /** SWITCH STATEMENT */
    public Object visitSwitchStat(SwitchStat ss) {
	println(ss.line + ": Visiting a Switch statement");

	// YOUR CODE HERE

	return null;
    }

    public void buildClassHierarchyList(ArrayList<ClassDecl> classes, HashSet<String> seenClasses, ClassDecl cd) {
	if (seenClasses.contains(cd.name()))
	    return;
	
	classes.add(cd);
	seenClasses.add(cd.name());
	
        if (cd.superClass() != null) 
	    buildClassHierarchyList(classes, seenClasses, cd.superClass().myDecl);
        
        Sequence interfaces = cd.interfaces();
        if (interfaces.nchildren > 0) {
            for (int i=0; i<interfaces.nchildren; i++) {
		buildClassHierarchyList(classes, seenClasses,((ClassType)interfaces.children[i]).myDecl);
            }
        }
    }
   
    public Type computeIntersectionType(ClassType trueType, ClassType falseType) {

	// 3/24/22 experimental - the next 6 lines removed - they are probably not needed.
	/*		
	if (Type.isSuper(trueType, falseType)) {
	    return trueType;
	} else if (Type.isSuper(falseType, trueType)) {
	    return falseType;
	 
	    }*/
	// build a list of superclasses for both.
	ArrayList<ClassDecl> trueHierarchy = new ArrayList<ClassDecl>();
	HashSet<String> trueSeenClasses = new HashSet<String>();

	buildClassHierarchyList(trueHierarchy, trueSeenClasses, trueType.myDecl);
	//System.out.print(trueType.myDecl.name() + ": { ");
	//for (ClassDecl s : trueHierarchy)
	//    System.out.print(s.name() + " ");
	//System.out.println("}");

	ArrayList<ClassDecl> falseHierarchy = new ArrayList<ClassDecl>();
	HashSet<String> falseSeenClasses = new HashSet<String>();
	buildClassHierarchyList(falseHierarchy, falseSeenClasses, falseType.myDecl);

	//System.out.print(falseType.myDecl.name() + ": { ");
	//for (ClassDecl s : falseHierarchy)
	//    System.out.print(s.name() + " ");
	//System.out.println("}");

	// compute their intersection
	ArrayList<ClassDecl> commonHierarchy = new ArrayList<ClassDecl>();
	HashSet<String> commonClasses = new HashSet<String>();
	for (ClassDecl cd : trueHierarchy) {
	    if (falseSeenClasses.contains(cd.name())) {
		commonHierarchy.add(cd);
		commonClasses.add(cd.name());
	    }
	}

	//System.out.print("common classes: { ");
        //for (int i=0; i<commonHierarchy.size(); i++) {
	//    ClassDecl cd = commonHierarchy.get(i);
        //    System.out.print(cd.name() + " ");
	//}
        //System.out.println("}");
	//System.out.println("Size: = " + commonHierarchy.size());
	// Do some weeding out here.
	for (int i=0; i<commonHierarchy.size(); i++) {
	    ClassDecl cd1 = commonHierarchy.get(i);
	    if (cd1 == null) {
		//System.out.println("oops i=" + i + " is null!");
		continue;
	    }
	    for (int j=i+1; j<commonHierarchy.size(); j++) {
		//System.out.println("(i,j) = (" + i + "," + j + ")");
	       ClassDecl cd2 = commonHierarchy.get(j);
	       if (cd2 == null) {
		   //System.out.println("oops j=" + j + " is null!");
		   continue;
	       }
	       //System.out.print("Comparing " + cd1.name() + " to " + cd2.name() + ": ");
	       ClassType ct1 = new ClassType(cd1.className());
	       ct1.myDecl = cd1;
	       ClassType ct2 = new ClassType(cd2.className());
	       ct2.myDecl = cd2;
	    
	       if (Type.isSuper(ct1, ct2)) {
		   //System.out.println(" Throwing away " + cd1.name());
		   commonHierarchy.set(i, null);
	       } else if (Type.isSuper(ct2, ct1)) {
		   //System.out.println(" Throwing away " + cd2.name());
		   commonHierarchy.set(j, null);
	       } else
		   ;//System.out.println(" Keeping both");
	    }
	}
	// what's left?
	//System.out.print("common classes left over: { ");
        //for (ClassDecl s : commonHierarchy) {
	//    if (s == null)
	//		continue;
	//   System.out.print(s.name() + " ");
	//}
        //System.out.println("}");

	// there should be exactly ONE class and any number of interfaces left.
	ClassType superClass = null;
	Sequence interfaces = new Sequence();
	for (ClassDecl s : commonHierarchy) {
            if (s == null)
                continue;
	    ClassType ct = new ClassType(s.className());
	    ct.myDecl = s;
	    
	    if (s.isInterface())
		interfaces.append(ct);
	    else
		superClass = ct;
	}
	int intNo = ClassDecl.interSectionTypeCounter++;
	ClassDecl cd = new ClassDecl(new Sequence(new Modifier(Modifier.Public)),
				     new Name(new Token(sym.IDENTIFIER, "INT#"+intNo, 0,0,0)),
				     superClass, interfaces, new Sequence(), ClassDecl.IS_NOT_INTERFACE);

	ClassType ct = new ClassType(cd.className());
	ct.myDecl = cd;
	//cd.visit(new Utilities.PrintVisitor());
	ct.isIntersectionType = true;
	return ct;
    }
    
    /** TERNARY EXPRESSION */
    public Object visitTernary(Ternary te) {
	println(te.line + ": Visiting a ternary expression");

	// YOUR CODE HERE
	te.visitChildren(this);
	// println(String.format("%s ? %s : %s", te.expr().type, te.trueBranch().type, te.falseBranch().type));

	if (te.expr().type.isBooleanType() &&
		te.trueBranch().type.identical(te.falseBranch().type))
		te.type = te.trueBranch().type;

	println(te.line + ": Ternary has type: " + te.type);
	return te.type;
    }

    /** THIS */
    public Object visitThis(This th) {
	println(th.line + ": Visiting a this statement");

	th.type = th.type();

	println(th.line + ": This has type: " + th.type);
	return th.type;
    }

    /** UNARY POST EXPRESSION */
    public Object visitUnaryPostExpr(UnaryPostExpr up) {
	println(up.line + ": Visiting a unary post expression");
	// YOUR CODE HERE
	super.visitUnaryPostExpr(up);
	if (up.expr() instanceof Literal) {
		println("Error: Variable expected, found value.");
		System.exit(1);
	}

	up.type = up.expr().type;

	println(up.line + ": Unary Post Expression has type: " + up.type);
	return up.type;
    }

    /** UNARY PRE EXPRESSION */
    public Object visitUnaryPreExpr(UnaryPreExpr up) {
	println(up.line + ": Visiting a unary pre expression");

	// YOUR CODE HERE
	if (((PrimitiveType)up.expr().visit(this)).isStringType() && (
		up.op().operator().equals("-") ||
		up.op().operator().equals("++"))) {
		println(String.format("Error: Cannot apply operator '%s' to something of type String.",
			up.op().operator()));
		System.exit(1);
	}

	up.type = up.expr().type;

	println(up.line + ": Unary Pre Expression has type: " + up.type);
	return up.type;
    }

    /** VAR */
    public Object visitVar(Var va) {
	println(va.line + ": Visiting a var");

	// YOUR CODE HERE
	super.visitVar(va);
	if (va.init() != null && 
		va.init() instanceof Literal &&
		((Literal)va.init()).getKind() == Literal.StringKind) {
		println("Error: Cannot assign value of type String to variable of type int.");
		System.exit(1);
	}

	return null;
    }

    /** WHILE STATEMENT */
    public Object visitWhileStat(WhileStat ws) {
	println(ws.line + ": Visiting a while statement"); 

	// YOUR CODE HERE
	PrimitiveType expr;
	if (ws.expr() != null) {
		expr = (PrimitiveType)ws.expr().visit(this);
		if (!expr.isBooleanType()) {
			println("Error: Non boolean Expression found as test in while-statement.");
			System.exit(1);
		}
	}

	if (ws.stat() != null) ws.stat().visit(this);

	return null;
    }

}
