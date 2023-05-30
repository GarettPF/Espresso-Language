package NameChecker;

import AST.*;
import Utilities.Error;
import Utilities.SymbolTable;
import Utilities.Visitor;
import Parser.*;
import Phases.Phase2;

/** 
 * A visitor class which visits classes, and their members and enters
 * them into the appropriate tables.
 */
public class ClassAndMemberFinder extends Visitor {

    /**
     * Adds a method to the method table of a class.
     * @param cd The class that the method should be added to.
     * @param md The MethodDeclaration that is being added
     * @param name The name of the method or constructor being added to the class' method table.
     * @param sig The signature of the method/constructor being added.
     */     
    private void addMethod(ClassDecl cd, ClassBodyDecl md, String name, String sig) {
		SymbolTable st = (SymbolTable)cd.methodTable.get(name);
		
		// Are there methods defined in this class' symbol table with the right name?
		if (st != null) {
			// Are there any methods with the same parameter signature?
			Object m = st.get(sig);
			if (m != null) 		
				// CMF1.java
				Error.error(md,"Method " + name + "(" + Type.parseSignature(sig) + " ) already defined.");
		}
		
		// If we are inserting a constructor just insert in now - don't
		// go looking in the super classes.
		if (name.equals("<init>")) {
			if (st == null) {
				//A constructor with this name has never been inserted before
				// Create a new symbol table to hold all constructors.
				SymbolTable mt = new SymbolTable();
				// Insert the signature with the method decl.
				mt.put(sig, md);
				// Insert this symbol table into the method table.
				cd.methodTable.put(name, mt);		
			} else 
				st.put(sig, md);
			return ;
		}
		
		// Static initializers
		if (name.equals("<clinit>")) {
			// We can only have one static initializer, so it doesn't exist in the table.
			SymbolTable mt = new SymbolTable();
			mt.put(sig, md);
			cd.methodTable.put(name, mt);
			return;
		}
		
		// Ok, we have dealt with constructors and static initializers, now deal with methods.
		
		// We will not search the hierarchy now - we do that later
		// when the entire class hierarchy has been defined. That
		// means we might be violating certain things now, but that is
		// ok, we will catch it later.
		
		// It's all good - just insert it.
		if (st == null) {
			// A method of this name has never been inserted before.
			// Create a new symbol table to hold all methods of name 'name'
			SymbolTable mt = new SymbolTable();
			// Insert the signature with the method decl.
			mt.put(sig, md);
			// Insert this symbol table into the method table.
			cd.methodTable.put(name, mt);
		} else 
			// Methods with this name have been defined before, so just use that entry.
			st.put(sig, md);
	}

	/**
	 * Adds a {@link FieldDecl} to a class' field table.
	 * @param cd The class to which we are adding the field.
	 * @param f The FieldDecl being added.
	 * @param name The name of the field being added to the class' field table.    
	 */
	private void addField(ClassDecl cd, FieldDecl f, String name) {
		// We will not search the hierarchy now - we do that later when the 
		// entire class hierarchy has been defined.
		cd.fieldTable.put(name,f);
    }
    
    // %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
        
    /** 
     * Contains references to all class declarations. This is set in the constructor.
     */
    private SymbolTable classTable;

    /** 
     * Holds a reference to the current class. This is set in {@link #visitClassDecl(ClassDecl)}.
     */
    private ClassDecl currentClass;

    /**
     * Constructs a ClassAndMemberFinder visitor object.
     * @param classTable The (global) table of classes (stored in {@link Phases.Phase#classTable Phases/Phase.ClassTable}).
     * @param debug Determine if this visitor should produce output.
     */
    public ClassAndMemberFinder(SymbolTable classTable, boolean debug) { 
		this.classTable = classTable; 
		this.debug = debug;
    }
    
    
    /** 
     * Visits a {@link ClassDecl}.
     * @param cd A {@link ClassDecl} object.
     * Inserts cd into the global class table.<br>
     * <ul>
     * <li>Put cd into the global class table.</li>
     * <li>If no super class was defined, set cd's super class to be Object. The Object class and its myDecl lives in {@link Phases.Phase2}.</li>
     * <li>Updates currentClass.</li>
     * <li>Visit the class.</li>
     * <li>If there are no constructors defined in cd, then create a default contructor and add it to the class and the method table.</li>
     * </ul>
     * @return null
     */
    public Object visitClassDecl(ClassDecl cd) {
		println("ClassDecl:\t Inserting class '" + cd.name() +"' into global class table.");
		
		// Enter this class into the class table 
		classTable.put(cd.name(), cd);
		// CMF3.java (class already exists)
		
		// 01/17/2012 added for allowing for common superclass 'Object'
		// For espresso it is simiilar to java/lang/Object for Java
		// see Phases/Phases2.java for the class 'Object'
		if (cd.superClass() == null && !cd.name().equals("Object")) {
			cd.children[2] = new ClassType(new Name(new Token(sym.IDENTIFIER,"Object",cd.line,0,0)));
			((ClassType)cd.children[2]).myDecl = Phase2.Objects_myDecl;
		}		
		// Update the current class 
		currentClass = cd;
		
		// Visit the children
		super.visitClassDecl(cd);
		
		// If there are not constructors at all - insert the default -
		// don't actually make any parse tree stuff - just generate
		// the code automatically in the code generation phase.
		if (cd.methodTable.get("<init>") == null && !cd.isInterface()) { 
			Token t = new Token(sym.IDENTIFIER, cd.name(), 0, 0, 0);
			Modifier m = new Modifier(Modifier.Public);
			
			ConstructorDecl c = new ConstructorDecl(new Sequence(m),
								new Name(t),
								new Sequence(),
								null,
								new Sequence());
			addMethod(cd, c, "<init>", "");
			cd.body().append(c);
			println("ClassDecl:\t Generating default construction <init>() for class '" + cd.name() + "'");
		}
		
		return null;
    }
    
	public Object visitMethodDecl(MethodDecl md) {
		String name = md.getname();
		String cname = currentClass.name();
		String sig = md.paramSignature();
		println(String.format("MethodDecl:\t Inserting method '%s' with signature '%s' into method table for class '%s'.", name, sig, cname));
		
		addMethod(currentClass, md, name, sig);
		return md.visitChildren(this);
	}

	public Object visitFieldDecl(FieldDecl fd) {
		println(String.format("FieldDecl:\t Inserting field '%s' into field table of class '%s'.", fd.name(), currentClass.name()));
		addField(currentClass, fd, fd.name());
		return null;
	}

	public Object visitConstructorDecl(ConstructorDecl cd) {
		println(String.format("ConstructorDecl: Inserting constructor '<init>' with signature '%s' into method table for class '%s'.", cd.paramSignature(), currentClass.name()));
		addMethod(currentClass, cd, "<init>", cd.paramSignature());
		return cd.visitChildren(this);
	}
    
    /** 
     * Visit a {@link StaticInitDecl}.
     * @param si A {@link StaticInitDecl} object.
     * <ul>
     * <li> Insert static initializer with name &lt;clinit&gt; into class' method table.</li>
     * </ul>
     * @return null
     */
    public Object visitStaticInitDecl(StaticInitDecl si) {
		println("StaticInitDecl:\t Inserting <clinit> into method table for class '" + 
			currentClass.name() + "'.");
		
		addMethod(currentClass, si, "<clinit>", "");
		return null;
    }
}

