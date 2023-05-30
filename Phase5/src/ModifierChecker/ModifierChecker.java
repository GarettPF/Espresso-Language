package ModifierChecker;

import AST.*;
import Utilities.*;
import NameChecker.*;
import TypeChecker.*;
import Utilities.Error;
import java.util.*;

public class ModifierChecker extends Visitor {

	private SymbolTable classTable;
	private ClassDecl currentClass;
	private ClassBodyDecl currentContext;
	private boolean leftHandSide = false;
        

	public ModifierChecker(SymbolTable classTable, boolean debug) {
		this.classTable = classTable;
		this.debug = debug;
	}

    /**
     * Implements the M algorithm from the Book. See Section XXX.
     * @param abstracts A hash set of the abstract methods.
     * @param concretes A hash set of the concrete methods.
     * @param cd The {@link ClassDecl ClassDecl} we are considering now.
     *
     * This method should traverse the class hierarchy exactly like
     * {@link #getMethod(String,ClassDecl) findMethod}. The string
     * associated with a method is as follows:<br><br>
     *
     * For example:<br><br>
     * void foo(int x, double d) { ... } we use the string "<tt>void bar( int double )</tt>"<br><br>
     *
     * To create this string we can use the Type.parseSignature() method on the return type as
     * well as on the paramaters and then glue it all together with the name and a set of ( ).    
     */
    public void M(HashSet<String> abstracts, HashSet<String> concretes, ClassDecl cd) {
	for (int i=0; i<cd.interfaces().nchildren; i++)
	    M(abstracts, concretes, ((ClassType)cd.interfaces().children[i]).myDecl);

	if (cd.superClass() != null)
	    M(abstracts, concretes, cd.superClass().myDecl);

	// Remove the concretes (these are from the super class only and will only ever
	// remove something that was added to the abstract set from interfaces of cd.
	Iterator<String> it = concretes.iterator();
        while (it.hasNext()) {
            String s = it.next();
            abstracts.remove(s);
        }

        // Remove the concretes from this class
	for (int i=0; i<cd.body().nchildren; i++) {
            if (cd.body().children[i] instanceof MethodDecl) {
                MethodDecl md = (MethodDecl)cd.body().children[i];
                if (!md.getModifiers().isAbstract()) {
                    concretes.add(Type.parseSignature(md.returnType().signature()) + " " +
				  md.name()+"("+Type.parseSignature(md.paramSignature()) + " )");
                    abstracts.remove(Type.parseSignature(md.returnType().signature()) + " " +
				     md.name()+"("+Type.parseSignature(md.paramSignature()) + " )");
                }
            }
        }

        // Add the abstract ones again
	for (int i=0; i<cd.body().nchildren; i++) {
            if (cd.body().children[i] instanceof MethodDecl) {
                MethodDecl md = (MethodDecl)cd.body().children[i];
                if (md.getModifiers().isAbstract() || md.block() == null) {
                    abstracts.add(Type.parseSignature(md.returnType().signature()) + " " +
				  md.name()+"("+Type.parseSignature(md.paramSignature()) + " )");
                    concretes.remove(Type.parseSignature(md.returnType().signature()) + " " +
				     md.name()+"("+Type.parseSignature(md.paramSignature()) + " )");
                }
            }
        }
    }
    //-->

    /** 
     * Uses the M algorithm from the Book to check that all abstract classes are 
     * implemented correctly in the class hierarchy. If not an error message is produced.
     *
     * @param cd A {@link ClassDecl ClassDecl} object.
     * @param methods The sequence of all methods of cd.
     */   
    public void checkImplementationOfAbstractClasses(ClassDecl cd, Sequence methods) {
	// YOUR CODE HERE
		HashSet<String> abs = new HashSet<String>();
		HashSet<String> con = new HashSet<String>();
		M(abs, con, cd);
		if (!abs.isEmpty())
			Error.error(cd, "Class " + cd.name() + " should be declared abstract.");
    }




    
	/** Assignment */
	public Object visitAssignment(Assignment as) {
	    println(as.line + ": Visiting an assignment (Operator: " + as.op()+ ")");

		boolean oldLeftHandSide = leftHandSide;

		leftHandSide = true;
		as.left().visit(this);

		// Added 06/28/2012 - no assigning to the 'length' field of an array type
		if (as.left() instanceof FieldRef) {
			FieldRef fr = (FieldRef)as.left();
			if (fr.target().type.isArrayType() && fr.fieldName().getname().equals("length"))
				Error.error(fr,"Cannot assign a value to final variable length.");
		}
		leftHandSide = oldLeftHandSide;
		as.right().visit(this);

		return null;
	}

	/** CInvocation */
	public Object visitCInvocation(CInvocation ci) {
	    println(ci.line + ": Visiting an explicit constructor invocation (" + (ci.superConstructorCall() ? "super" : "this") + ").");

		// YOUR CODE HERE
		super.visitCInvocation(ci);

		return null;
	}

	/** ClassDecl */
	public Object visitClassDecl(ClassDecl cd) {
		println(cd.line + ": Visiting a class declaration for class '" + cd.name() + "'.");

		currentClass = cd;

		// If this class has not yet been declared public make it so.
		if (!cd.modifiers.isPublic())
			cd.modifiers.set(true, false, new Modifier(Modifier.Public));

		// If this is an interface declare it abstract!
		if (cd.isInterface() && !cd.modifiers.isAbstract())
			cd.modifiers.set(false, false, new Modifier(Modifier.Abstract));

		// If this class extends another class then make sure it wasn't declared
		// final.
		if (cd.superClass() != null)
			if (cd.superClass().myDecl.modifiers.isFinal())
				Error.error(cd, "Class '" + cd.name()
						+ "' cannot inherit from final class '"
						+ cd.superClass().typeName() + "'.");

		// YOUR CODE HERE
		super.visitClassDecl(cd);
		if (!cd.modifiers.isAbstract() || (cd.isClass() && cd.interfaces() == null))
			checkImplementationOfAbstractClasses(cd, cd.allMethods);

	
		return null;
		

	}

	/** FieldDecl */
	public Object visitFieldDecl(FieldDecl fd) {
	    println(fd.line + ": Visiting a field declaration for field '" +fd.var().name() + "'.");

		// YOUR CODE HERE
		// If field is not private and hasn't been declared public make it so.
		if (!fd.modifiers.isPrivate() && !fd.modifiers.isPublic())
			fd.modifiers.set(false, false, new Modifier(Modifier.Public));

		currentContext = fd;

		// check if a final field is initialized
		if (fd.modifiers.isFinal() && fd.var().init() == null)
			Error.error(fd, String.format("Error: Field '%s' in interface '%s' must be initialized.",
			fd.var().name().toString(),
			currentClass.name()));

		super.visitFieldDecl(fd);

		return null;
	}

	/** FieldRef */
	public Object visitFieldRef(FieldRef fr) {
	    println(fr.line + ": Visiting a field reference '" + fr.fieldName() + "'.");

		// YOUR CODE HERE
		Boolean targetIsClass = fr.target() != null && fr.target().isClassName();
		Boolean targetIsNull = fr.rewritten;

		// for arrays
		if (!(fr.targetType instanceof ArrayType)) {

			// check if refering a non-static field from static context & target is a class name
			if (currentContext.isStatic() && !fr.myDecl.modifiers.isStatic() && targetIsClass)
				Error.error(fr, String.format("Error: non-static field '%s' cannot be referenced in a static context.",
				fr.fieldName().toString()));
			
			// check if refering a non-static field from static context & target is null
			if (currentContext.isStatic() && !fr.myDecl.modifiers.isStatic() && targetIsNull)
				Error.error(fr, String.format("Error: non-static field '%s' cannot be referenced from a static context.",
				fr.fieldName().toString()));

			// check if refering a static field from non-static context
			if (fr.myDecl.modifiers.isFinal())
				Error.error(fr, String.format("Error: Cannot assign a value to final field '%s'.", fr.fieldName().toString()));

				
			// check if refering private field outside the field's context
			ClassType targetClass = (ClassType)fr.targetType;
			String targetClassName = targetClass.myDecl.toString();
			String currentClassName = currentClass.toString();
			Boolean isPrivate = fr.myDecl.modifiers.isPrivate();
			
			if (!targetClassName.equals(currentClassName) && isPrivate)
				Error.error(fr, String.format("Error: field '%s' was declared 'private' and cannot be accessed outside its class.",
		
				fr.fieldName().toString()));
		}

		if (!fr.rewritten) fr.target().visit(this);
	
	return null;
	}       

	/** MethodDecl */
	public Object visitMethodDecl(MethodDecl md) {
	    println(md.line + ": Visiting a method declaration for method '" + md.name() + "'.");

		// YOUR CODE HERE
		String methodname = md.getname();

		if (md.getModifiers().isFinal() && md.getMyClass().isInterface())
			Error.error(md, String.format("Error: Method '%s' cannot be declared final in an interface.",
			methodname));

		if (md.getModifiers().isAbstract() && md.block() != null) 
			Error.error(md, String.format("Error: Abstract method '%s' cannot have a body.", methodname));

		if (md.block() == null && !md.getModifiers().isAbstract() && md.getMyClass().isClass())
			Error.error(md, String.format("Error: Method '%s' does not have a body, or should be declared abstract.",
			methodname));

		if (md.block() == null && md.getModifiers().isAbstract() && !md.getMyClass().modifiers.isAbstract())
			Error.error(md, String.format("Error: Method '%s' does not have a body, or class should be declared abstract.",
			methodname));

		ClassDecl superClass = currentClass.superClass().myDecl;
		MethodDecl method = (MethodDecl)TypeChecker.findMethod(superClass.allMethods, methodname, md.params(), true);
		currentContext = md;

		if (method != null && method.getModifiers().isFinal())
			Error.error(md, String.format("Error: Method '%s' was implemented as final in super class, cannot be reimplemented.", 
			methodname));

		if (method != null && md.getModifiers().isStatic() && !method.getModifiers().isStatic())
			Error.error(md, String.format("Error: Method '%s' declared non-static in superclass, cannot be reimplemented static.",
			methodname));

		if (method != null && !md.getModifiers().isStatic() && method.getModifiers().isStatic())
			Error.error(md, String.format("Error: Method '%s' declared static in superclass, cannot be reimplemented non-static.",
			methodname));

		
		
		super.visitMethodDecl(md);
		return null;
	}

	/** Invocation */
	public Object visitInvocation(Invocation in) {
	    println(in.line + ": Visiting an invocation of method '" + in.methodName() + "'.");

	    // YOUR CODE HERE

		// for string types
		if (!in.targetType.isStringType()) {
			super.visitInvocation(in);

			Boolean methodFound = false;
			Modifiers mods = in.targetMethod.getModifiers();
			String methodName = in.targetMethod.getname();
			Boolean targetIsClass = in.target() != null && in.target().isClassName();
			Boolean targetIsNull = in.target() == null;
			Boolean isContextStatic = currentContext != null && currentContext.isStatic();
			
			// reference non-static method in static context
			// <Class-name>.<non-static method>()
			if (isContextStatic && targetIsClass && !mods.isStatic())
			Error.error(in, String.format("Error: non-static method '%s' cannot be referenced from a static context.", 
			methodName));
			
			
			// reference non-static method in static context
			// <non-static method>()
			if (isContextStatic && targetIsNull && !mods.isStatic())
			Error.error(in, String.format("Error: non-static method '%s' cannot be referenced from a static context.",
			methodName));
			
			
			// invoking method with private access outside of class
			if (mods.isPrivate()) {
				Sequence currentContextMethods = ((MethodDecl)currentContext).getMyClass().allMethods;
				for (int m = 0; m < currentContextMethods.nchildren; m++) {
					String currentContextMethodName = ((MethodDecl)currentContextMethods.children[m]).name().toString();
					if (currentContextMethodName.equals(methodName))
					methodFound = true;
				}
				
				if (!methodFound) {
					String paramSigs = in.targetMethod.paramSignature();
					
					Error.error(in, String.format("Error: %s(%s ) has private access in '%s'.", 
					methodName,
					Type.parseSignature(paramSigs),
					in.targetMethod.getMyClass().name()));
				}
			}
		}
			
			
			
	    return null;
	}
    

	public Object visitNameExpr(NameExpr ne) {
	    println(ne.line + ": Visiting a name expression '" + ne.name() + "'. (Nothing to do!)");
	    return null;
	}

	/** ConstructorDecl */
	public Object visitConstructorDecl(ConstructorDecl cd) {
	    println(cd.line + ": visiting a constructor declaration for class '" + cd.name() + "'.");
	      
		// YOUR CODE HERE
		currentContext = cd;
		super.visitConstructorDecl(cd);

		return null;
	}

	/** New */
	public Object visitNew(New ne) {
	    println(ne.line + ": visiting a new '" + ne.type().myDecl.name() + "'.");

		// YOUR CODE HERE
		if (ne.type().myDecl.getModifiers().isAbstract())
			Error.error(String.format("Error: Cannot instantiate abstract class '%s'.",
			ne.type().myDecl.name()));

		if (ne.getConstructorDecl().getModifiers().isPrivate() && 
		!currentClass.name().equals(ne.getConstructorDecl().getname())) {
			String paramSigs = ne.getConstructorDecl().paramSignature();
				
			Error.error(ne, String.format("Error: %s(%s ) has private access in '%s'.", 
			ne.type().name().toString(),
			Type.parseSignature(paramSigs),
			ne.type().name().toString()));
		}

		super.visitNew(ne);

		return null;
	}

	/** StaticInit */
	public Object visitStaticInitDecl(StaticInitDecl si) {
		println(si.line + ": visiting a static initializer");

		// YOUR CODE HERE
		currentContext = si;
		super.visitStaticInitDecl(si);

		return null;
	}

	/** Super */
	public Object visitSuper(Super su) {
		println(su.line + ": visiting a super");

		if (currentContext.isStatic())
			Error.error(su,
					"non-static variable super cannot be referenced from a static context");

		return null;
	}

	/** This */
	public Object visitThis(This th) {
		println(th.line + ": visiting a this");

		if (currentContext.isStatic())
			Error.error(th,	"non-static variable this cannot be referenced from a static context");

		return null;
	}

	/** UnaryPostExpression */
    public Object visitUnaryPostExpr(UnaryPostExpr up) {
	println(up.line + ": visiting a unary post expression with operator '" + up.op() + "'.");
	
	// YOUR CODE HERE

	// for arrays int A[]; A.length++;
	if (up.expr() instanceof FieldRef) {
		FieldRef fr = (FieldRef)up.expr();
		if (fr.targetType instanceof ArrayType) {
			Error.error(up, "cannot assign a value to final variable length.");
		}
	}

	if (up.expr() instanceof FieldRef) {
		FieldRef field = (FieldRef)up.expr();
		if (field.myDecl.modifiers.isFinal())
			Error.error(up, String.format("Error: Cannot assign a value to final field '%s'.",
			field.fieldName().toString()));
	}

	super.visitUnaryPostExpr(up);
	return null;
    }
    
    /** UnaryPreExpr */
    public Object visitUnaryPreExpr(UnaryPreExpr up) {
	println(up.line + ": visiting a unary pre expression with operator '" + up.op() + "'.");
	
	// YOUR CODE HERE

	// for arrays int A[]; A.length++;
	if (up.expr() instanceof FieldRef) {
		FieldRef fr = (FieldRef)up.expr();
		if (fr.targetType instanceof ArrayType) {
			Error.error(up, "cannot assign a value to final variable length.");
		}
	}

	Expression exp = up.expr();
	if (exp instanceof FieldRef && ((FieldRef)exp).myDecl.modifiers.isFinal())
		Error.error(up, String.format("Error: Cannot assign a value to final field '%s'.", 
		((FieldRef)exp).fieldName().toString()));

	super.visitUnaryPreExpr(up);
	return null;
    }
}
