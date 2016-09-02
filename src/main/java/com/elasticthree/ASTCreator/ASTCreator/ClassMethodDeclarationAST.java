package com.elasticthree.ASTCreator.ASTCreator;

import org.apache.log4j.Logger; 
import com.github.javaparser.ast.CompilationUnit;

public class ClassMethodDeclarationAST {
	final static Logger logger = Logger.getLogger(ClassMethodDeclarationAST.class);
	private CompilationUnit cu;
	private String pathFile;
	private ClassVisitor classVisitor;
	private MethodVisitor methodVisitor;

	public ClassMethodDeclarationAST(CompilationUnit cu, String pathClass){
		this.cu = cu;
		this.setPathFile(pathClass);
		this.classVisitor = new ClassVisitor(cu.getPackage().getName().toString());
		this.methodVisitor = new MethodVisitor(cu.getPackage().getName().toString());
	}
	
	public void getTypeDeclarationFile(){
		getClassOrInterface(cu);
		getFileMethods(cu);
	}
	
	private void getClassOrInterface(CompilationUnit cu) {
		try{
			classVisitor.visit(cu, null);
		}catch (Exception e){
       	 logger.error("Error: ",e);
		}
	}
	
	private void getFileMethods(CompilationUnit cu) {
		try{
			methodVisitor.visit(cu, null);
		}catch (Exception e){
			logger.error("Error: ",e);
		}
	}
	
	public CompilationUnit getCu() {
		return cu;
	}

	public void setCu(CompilationUnit cu) {
		this.cu = cu;
	}

	public String getPathClass() {
		return pathFile;
	}

	public void setPathFile(String pathFile) {
		this.pathFile = pathFile;
	}
	
	public ClassVisitor getClassVisitor() {
		return classVisitor;
	}

	public MethodVisitor getMethodVisitor() {
		return methodVisitor;
	}
}