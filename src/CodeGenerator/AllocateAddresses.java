package CodeGenerator;

import AST.*;
import Utilities.Visitor;

class AllocateAddresses extends Visitor {

	private Generator gen;
	private ClassDecl currentClass;
	private ClassBodyDecl currentBodyDecl;

	AllocateAddresses(Generator g, ClassDecl currentClass, boolean debug) {
		this.debug = debug;
		gen = g;
		this.currentClass = currentClass;
	}

	// BLOCK
	public Object visitBlock(Block bl) {
		// YOUR CODE HERE
		int beforeAddress = gen.getAddress();

		for (int c = 0; c < bl.stats().nchildren; c++) {
			Statement st = (Statement)bl.stats().children[c];
			if (st instanceof WhileStat) {
				beforeAddress = gen.getAddress();
				st.visit(this);

				gen.setAddress(beforeAddress);
			} else if (st instanceof ForStat) {
				ForStat fs = (ForStat)st;
				fs.init().visit(this);
				beforeAddress = gen.getAddress();
				fs.stats().visit(this);
				gen.setAddress(beforeAddress);
			} else if (st instanceof IfStat) {
				IfStat is = (IfStat)st;
				beforeAddress = gen.getAddress();
				is.visitChildren(this);
				gen.setAddress(beforeAddress);
			} else {
				st.visit(this);
			}
		}

		gen.setAddress(beforeAddress);

		return null;
	}

	// LOCAL VARIABLE DECLARATION
	public Object visitLocalDecl(LocalDecl ld) {
		// YOUR CODE HERE
		ld.address = gen.getAddress();
		if (ld.type().width() == 2)
			gen.inc2Address();
		else
			gen.incAddress();

		println(ld.line + ": LocalDecl:\tAssigning address:  " + ld.address + " to local variable '"
				+ ld.var().name().getname() + "'.");
		return null;
	}

	// PARAMETER DECLARATION
	public Object visitParamDecl(ParamDecl pd) {
		// YOUR CODE HERE

		pd.address = gen.getAddress();

		if (pd.type().width() == 2)
			gen.inc2Address();
		else
			gen.incAddress();

		println(pd.line + ": ParamDecl:\tAssigning address:  " + pd.address + " to parameter '"
				+ pd.paramName().getname() + "'.");
		return null;
	}

	// METHOD DECLARATION
	public Object visitMethodDecl(MethodDecl md) {
		println(md.line + ": MethodDecl:\tResetting address counter for method '" + md.name().getname() + "'.");
		// YOUR CODE HERE
		currentBodyDecl = md;
		gen.resetAddress();
		if (md.isStatic())
			gen.setAddress(0);
		else
			gen.setAddress(1);
		md.visitChildren(this);
		currentBodyDecl.localsUsed = gen.getLocalsUsed();

		println(md.line + ": End MethodDecl");
		return null;
	}

	// CONSTRUCTOR DECLARATION
	public Object visitConstructorDecl(ConstructorDecl cd) {
		println(cd.line + ": ConstructorDecl:\tResetting address counter for constructor '" + cd.name().getname()
				+ "'.");
		gen.resetAddress();
		gen.setAddress(1);
		currentBodyDecl = cd;
		super.visitConstructorDecl(cd);
		cd.localsUsed = gen.getLocalsUsed();
		// System.out.println("Locals Used: " + cd.localsUsed);
		println(cd.line + ": End ConstructorDecl");
		return null;
	}

	// STATIC INITIALIZER
	public Object visitStaticInitDecl(StaticInitDecl si) {
		println(si.line + ": StaticInit:\tResetting address counter for static initializer for class '"
				+ currentClass.name() + "'.");
		// YOUR CODE HERE
		currentBodyDecl = si;
		gen.resetAddress();
		gen.setAddress(0);
		
		si.visitChildren(this);
		currentBodyDecl.localsUsed = gen.getLocalsUsed();

		println(si.line + ": End StaticInit");
		return null;
	}
}
