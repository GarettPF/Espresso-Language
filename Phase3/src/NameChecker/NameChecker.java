package NameChecker;

import AST.*;
import Utilities.Error;
import Utilities.SymbolTable;
import Utilities.Visitor;
import Utilities.Rewrite;
import java.util.*;
import Parser.*;;

/**
 * Performs name resultion on all name uses. Defines locals and
 * parameters in symbol tables as well.
 */
public class NameChecker extends Visitor {

    /**
     * Traverses the class hierarchy to look for a method of name
     * 'methodName'. We return #t if we find any method with the
     * correct name. Since we don't have types yet we cannot look at
     * the signature of the method, so all we do for now is look if
     * any method is defined. The search is as follows:<br><br>
     *
     *  1) look in the current class.<br>
     *  2) look in its super class.<br>
     *  3) look in all the interfaces.<br>
     * 
     * An entry in the methodTable is a symbol table it self. It holds
     * all entries of the same name, but with different signatures.
     *
     * @param methodName The name of the method for which we are searching.`
     * @param cd The class in which we start the search for the
     * method.
     * @return A symbol table with all methods called 'methodName' or null.
     */    
    public static SymbolTable getMethod(String methodName, ClassDecl cd) {	
        SymbolTable mt = cd.methodTable;
        SymbolTable method = (SymbolTable)mt.get(methodName);

        // check in the super class
        if (method == null && cd.superClass() != null) {
            method = getMethod(methodName, cd.superClass().myDecl);
        }

        // check in the interfaces
        if (method == null && cd.interfaces().nchildren != 0) {
            for (int i = 0; i < cd.interfaces().nchildren; i++) {
                ClassType iFace = (ClassType)cd.interfaces().children[i];
                method = getMethod(methodName, iFace.myDecl);
                if (method != null)
                    break;
            }
        }

        return method;
    }
    
    /** 
     * Traverses the class hierarchy starting in the class cd looking
     * for a field called 'fieldName'. The search approach is the same
     * getMethod just for fields instead. See {@link
     * #getMethod(String,ClassDecl) findMethod}.
     * @param fieldName The name of the field for which we are searching.
     * @param cd The class where the search starts.
     * @return A FieldDecl if the find was found, null otherwise.
     */
    public static FieldDecl getField(String fieldName, ClassDecl cd) {	
        SymbolTable st = cd.fieldTable;
        FieldDecl field = (FieldDecl)st.get(fieldName);

        // check in the super class
        if (field == null && cd.superClass() != null) {
            field = getField(fieldName, cd.superClass().myDecl);
        }

        // check in the interfaces
        if (field == null && cd.interfaces().nchildren != 0) {
            for (int i = 0; i < cd.interfaces().nchildren; i++) {
                ClassType iFace = (ClassType)cd.interfaces().children[i];
                field = getField(fieldName, iFace.myDecl);
                if (field != null)
                    break;
            }
        }

        return field;
    }
    
    /** 
     * Traverses all the classes and interfaces and builds a sequence
     * of the methods of the class hierarchy. Constructors are not
     * included, nor are static initializers.
     *
     * @param cd The ClassDecl from where the travesal starts.
     * @param lst The Sequence to which we add all methods from the
     *        classDecl cd that are of instanceof of class MethodDecl.
     *        (The easiest approach is simply to for-loop through the
     *        body of cd and add all the ClassBodyDecls that are
     *        instanceof of MethodDecl.)
     * @param seenClasses A hash set to hold the names of all the
     *               classes we have seen so far.  This set is used in
     *               the following way: when entering the method check
     *               if the name of cd is already in the set -- if it
     *               is then it is cause we have a circular
     *               inheritance like A :&gt; B :&gt; A -- this is
     *               illegal.  Before we leave the method we remove
     *               the name of cd again.
     */
    public void getClassHierarchyMethods(ClassDecl cd, Sequence lst, HashSet<String> seenClasses) {
        // check if we have looked through this class already
        if (seenClasses.contains(cd.name().toString())) {
            seenClasses.remove(cd.name().toString());
            return ;
        }
        seenClasses.add(cd.name().toString());

        Sequence body = cd.body(); 

        // append methods of the class
        for (int c = 0; c < body.nchildren; c++) {
            AST child = body.children[c];
            if (child instanceof MethodDecl) {
                lst.append((MethodDecl)child);
            }
        }

        // append methods of the superclass
        if (cd.superClass() != null) {

            ClassDecl superClass = (ClassDecl)cd.superClass().myDecl;
            getClassHierarchyMethods(superClass, lst, seenClasses);
        }

        // append methods of the interfaces
        if (cd.interfaces().nchildren != 0) {
            for (int i = 0; i < cd.interfaces().nchildren; i++) {
                ClassType iFace = (ClassType)cd.interfaces().children[i];
                getClassHierarchyMethods(iFace.myDecl, lst, seenClasses);
            }
        }    
    }
    
    /**
     * For each method (not constructors) in the lst list, check that
     * if it exists more than once with the same parameter signature
     * that they all return something of the same type.
     * @param lst A sequence of MethodDecls.<br><br> 
     *
     * The easiest way to do this is simiply to double-for loop though
     * lst.  A better way is to use a HashTable and use the method
     * name+signature as the key and the return type signature of the
     * method as the value.
    */
    public void checkReturnTypesOfIdenticalMethods(Sequence lst) {
        Hashtable methods = new Hashtable<String, Type>();
        for (int m = 0; m < lst.nchildren; m++) {
            MethodDecl method = (MethodDecl)lst.children[m];
            if (methods.containsKey(method.name().toString() + method.paramSignature())) {
                // check if the return types are the same

                Type type = (Type)methods.get(method.name().toString() + method.paramSignature());
                if (method.returnType().signature() != type.signature()) {
                    String signature = method.name().toString() + '(';
                    for (int p = 0; p < method.params().nchildren; p++) {
                        ParamDecl param = (ParamDecl)method.params().children[p];
                        String paramType = param.type().toString().split(" ")[1];
                        paramType = paramType.substring(0, paramType.length()-1);
                        signature += " " + paramType;
                    }

                    println("Error:");
                    println(String.format("Error:  %s %s )", method.returnType().typeName(), signature));
                    Error.error(String.format("Error:  %s %s )", type.typeName(), signature));
                }
            } else {
                methods.put(method.name().toString() + method.paramSignature(), method.returnType());
            }
        }
    }

    /**
     * Checks that a class hierarchy does not contain the same field twice.
     * @param fields A hash set of the fields we have seen so far. Should start out empty from the caller.
     * @param cd The ClassDecl we are currently working with.
     * @param seenClasses: A hash set of all the classes we have already visited. We should not re-visit
     *              a class we have already visited cause any of its fields will cause an error as
     *              they will already be in the fields set. This set also start out empty from the caller.<br><br>
     * There is no need to visit classes that have already been visited cause they have the <b>same</b>
     * fields as already collected. Besides, this can only ever happen for interfaces, and fields
     * in interfaces are final anyways (at least in Espresso). We could never encounter a
     * class again as this would indicate a circular inheritance situation, and we already checked
     * for that.
     */
    public  void checkUniqueFields(HashSet<String> fields, ClassDecl cd, HashSet<String> seenClasses) {
        // check if we have accessed this class already
        if (seenClasses.contains(cd.name().toString())) {
            seenClasses.remove(cd.name().toString());
            return ;
        }

        // append methods of the superclass
        if (cd.superClass() != null) {

            ClassDecl superClass = (ClassDecl)cd.superClass().myDecl;
            checkUniqueFields(fields, superClass, seenClasses);
        }

        // append methods of the interfaces
        if (cd.interfaces().nchildren != 0) {
            for (int i = 0; i < cd.interfaces().nchildren; i++) {
                ClassType iFace = (ClassType)cd.interfaces().children[i];
                checkUniqueFields(fields, iFace.myDecl, seenClasses);
            }
        }  

        // add the fields from the current class
        Sequence body = cd.body();
        for (int c = 0; c < body.nchildren; c++) {
            AST child = body.children[c];
            if (child instanceof FieldDecl) {
                if (!fields.add(child.toString()))
                    Error.error(String.format("Error: Field '%s' already defined.", child.children[2]));
            }
        }

        seenClasses.add(cd.name().toString());
    }
    
    // %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    
    /**
     * Points to the current scope.
     */
    private SymbolTable currentScope;

    /**
     * The global class table. This is set in the constructor.
     */
    private SymbolTable classTable;

    /**
     * The current class in which we are working. This is set in visitClassDecl().
     */
    private ClassDecl   currentClass;

    /**
     * Constructs a NameChecker visitor object.
     * @param classTable The (global) table of classes (stored in {@link Phases.Phase#classTable Phases/Phase.ClassTable}).
     * @param debug Determine if this visitor should produce output.
     */
    public NameChecker(SymbolTable classTable, boolean debug) { 
        this.classTable = classTable; 
        this.debug = debug;
    }
    
    /**
     * Visits a {@link Block}.
     * @param bl A {@link Block} object.
     * <ul>
     * <li> Open a scope.</li>
     * <li> Visit the children.</li>
     * <li> Close the scope.</li>
     *</ul>
     * @return null
     */
    public Object visitBlock(Block bl) {
        println("Block:\t\t Creating new scope for Block.");
        currentScope = currentScope.newScope();
        super.visitBlock(bl);
        currentScope = currentScope.closeScope(); 
        return null;
    }
    
    /** 
     * Visits a {@link ClassDecl}.
     * @param cd A {@link ClassDecl} object.
     * <ul>
     * <li> Set the current Scope to be the field table of cd (this is not necessary but may save a little look-up time).</li>
     * <li> Set the current class to cd.</li>
     * <li> If the class has a super class, check that it is a class and not an interface. (test file: NC5.java)</li>
     * <li> Check that the class does not extend itself (test file: NC6.java).</li>
     * <li> If the class has a super class which has a <b>private</b> default constructor, then the super class cannot be extended (test file: NC7.java).</li>
     * <li> Check that all the implemented interfaces are interfaces and not classes (test file: NC8.java).</li>
     * <li> Visit the children.</li>
     * <li> Call getClassHierarchyMethods(), checkReturnTypesOfIdenticalMethods(), checkImplementationOfAbstractClasses() if the class is not an interface and is not declared abstract.</li>
     * <li> Call checkUniqueFields(). </li>
     * <li> Update cd.allMethod to the method sequence computed in getClassHierarchyMethods().</li>
     * <li> Fill cd.constructors with the ConstructorDecls from this class (cd).</li>
     * <li> Call the {@link Utilities.Rewrite} re-writer. This rewriter transforms all name expressions that are really field references into proper FieldRef nodes.</li>
     * </ul>
     * @return null
     */
    public Object visitClassDecl(ClassDecl cd) {
        println("ClassDecl:\t Visiting class '"+cd.name()+"'");
        
        // If we use the field table here as the top scope, then we do not
        // need to look in the field table when we resolve NameExpr. Note,
        // at this time we have not yet rewritten NameExprs which are really
        // FieldRefs with a null target as we have not resolved anything yet.
        currentScope = cd.fieldTable;
        currentClass = cd;
        
        HashSet<String> seenClasses = new HashSet<String>();
        
        // Check that the superclass is a class.
        if (cd.superClass() != null)  {
            if (cd.superClass().myDecl.isInterface())
            // NC5.java
            Error.error(cd,"Class '" + cd.name() + "' cannot inherit from interface '" +
                    cd.superClass().myDecl.name() + "'.");
            
        }
            
        if (cd.superClass() != null) {
            if (cd.name().equals(cd.superClass().typeName()))
                // NC6.java
                Error.error(cd, "Class '" + cd.name() + "' cannot extend itself.");
            // If a superclass has a private default constructor, the 
            // class cannot be extended.
            ClassDecl superClass = (ClassDecl)classTable.get(cd.superClass().typeName());
            SymbolTable st = (SymbolTable)superClass.methodTable.get("<init>");
            ConstructorDecl ccd = (ConstructorDecl)st.get("");
            if (ccd != null && ccd.getModifiers().isPrivate())
                // NC7.java
                Error.error(cd, "Class '" + superClass.className().getname() + "' cannot be extended because it has a private default constructor.");
        }
        
        // Check that the interfaces implemented are interfaces.
        for (int i=0; i<cd.interfaces().nchildren; i++) {
            ClassType ct = (ClassType)cd.interfaces().children[i];
            if (ct.myDecl.isClass())
                // NC8.java
                Error.error(cd,"Class '" + cd.name() + "' cannot implement class '" + ct.name() + "'.");
        }

        // Visit the children
        super.visitClassDecl(cd);
        
        currentScope = null;
        Sequence methods = new Sequence();
        
        getClassHierarchyMethods(cd, methods, seenClasses);
        checkReturnTypesOfIdenticalMethods(methods);
        
        // All field names can only be used once in a class hierarchy
        seenClasses = new HashSet<String>();
        checkUniqueFields(new HashSet<String>(), cd, seenClasses);
        
        cd.allMethods = methods; // now contains only MethodDecls
        
        // Fill cd.constructors.
        SymbolTable st = (SymbolTable)cd.methodTable.get("<init>");
        ConstructorDecl cod;
        if (st != null) {
            for (Enumeration<Object> e = st.entries.elements(); e.hasMoreElements(); ) {
                cod = (ConstructorDecl)e.nextElement();
                cd.constructors.append(cod);
            }
        }
        
        // needed for rewriting the tree to replace field references
        // represented by NameExpr.
        println("ClassDecl:\t Performing tree Rewrite on " + cd.name());
        new Rewrite().go(cd, cd);
        
        return null;
    }

    public Object visitClassType(ClassType ct) {
        println(String.format("ClassType:\t Looking up class/interface '%s' in class table.", ct.name().toString()));
        return null;
    }

    public Object visitMethodDecl(MethodDecl md) {
        println(String.format("MethodDecl:\t Creating new scope for Method '%s' with signature '%s' (Parameters and Locals).", md.name(), md.paramSignature()));
        currentScope = currentScope.newScope();
        super.visitMethodDecl(md);
        currentScope = currentScope.closeScope();
        return null;
    }

    public Object visitLocalDecl(LocalDecl ld) {
        println("LocalDecl:\t Declaring local symbol '" + ld.name() + "'.");
        currentScope.put(ld.name(), ld);
        return ld.visitChildren(this);
    }

    public Object visitNameExpr(NameExpr ne) {
        println("NameExpr:\t Looking up symbol '" + ne.name() + "'.");
        // check for symbols that are not class types
        Object obj = currentScope.get(ne.name().toString());
        if (obj instanceof ParamDecl) {
            println(" Found Parameter");
            return null;
        } else if (obj instanceof LocalDecl) {
            println(" Found Local Variable");
            return null;
        } else if (getField(ne.name().toString(), currentClass) instanceof FieldDecl) {
            println(" Found Field");
            return null;
        }

        // check for symbols that are of class types
        try {
            Object classType = classTable.get(ne.name().toString());
            if (classType != null) {
                println(" Found Class");
                return null;
            }
        } catch (Exception e) {;}

        Error.error(String.format("Error: Symbol '%s' not declared.", ne.name().toString()));
        return null;
    }

    public Object visitConstructorDecl(ConstructorDecl cd) {
        println(String.format("ConstructorDecl: Creating new scope for constructor <init> with signature '%s' (Parameters and Locals).", cd.paramSignature()));
        currentScope = currentScope.newScope();
        super.visitConstructorDecl(cd);
        currentScope = currentScope.closeScope();
        return null;
    }

    public Object visitParamDecl(ParamDecl pd) {
        println(String.format("ParamDecl:\t Declaring parameter '%s'.", pd.name()));
        currentScope.put(pd.name(), pd);
        return pd.visitChildren(this);
    }

    public Object visitFieldRef(FieldRef fr) {
        if (fr.target() != null && !(fr.target() instanceof This))
            println("FieldRef:\t Target too complicated for now!");
        else {
            println(String.format("FieldRef:\t Looking up field '%s'.", fr.fieldName().toString()));
            if (getField(fr.fieldName().toString(), currentClass) == null)
                Error.error(String.format("Field '%s' not found.", fr.fieldName().toString()));
        }
        return fr.visitChildren(this);
    }

    public Object visitInvocation(Invocation i) {
        if (i.target() instanceof This || i.target() == null) {
            println(String.format("Invocation:\t Looking up method '%s'.", i.methodName()));
            if (getMethod(i.methodName().toString(), currentClass) == null)
                Error.error(String.format("Error: Method '%s' not found.", i.methodName()));
        } else
            println("Invocation:\t Target too complicated for now!");
        return i.visitChildren(this);
    }

    public Object visitForStat(ForStat fs) {
        println("ForStat:\t Creating new scope for For Statement.");
        currentScope.newScope();
        fs.visitChildren(this);
        currentScope.closeScope();
        return null;
    }

    public Object visitSwitchStat(SwitchStat ss) {
        println("SwitchStat:\t Visiting SwitchStat.");
        return ss.visitChildren(this);
    }
    
    /** 
     * Visits a {@link This} object.
     * @param th A {@link This} object.
     * Creates a new {@link ClassType} with the name of the current class. 
     * <ul>
     * <li>Sets the myDecl of thi newly created classType to the current class.</li>
     * </ul>
     * @return null
     */
    public Object visitThis(This th) {
        println("This:\t Visiting This.");
        ClassType ct = new ClassType(new Name(new Token(16,currentClass.name(),0,0,0)));
        ct.myDecl = currentClass;
        th.type = ct;
        return null;
    }

}

