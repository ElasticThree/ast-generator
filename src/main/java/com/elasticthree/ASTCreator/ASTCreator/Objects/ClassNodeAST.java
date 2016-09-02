package com.elasticthree.ASTCreator.ASTCreator.Objects;

import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Modifier;

public class ClassNodeAST {

	private String id;
	private String name;
	private String packageName;
	private String extendsClass;
	private long numberOfMethods;
	private boolean hasFinalModifier;
	private boolean hasAbstractModifier;
	private boolean hasPrivateModifier;
	private boolean hasPublicModifier;
	private boolean hasProtectedModifier;
	private boolean hasStaticModifier;
	private boolean hasSynchronizeModifier;
	private List<AnnotationNodeAST> annotatios;
	private List<CommentsNodeAST> comments;
	private List<ClassImplementsNodeAST> impl;
	private List<ClassHasMethodNodeAST> method;

	public ClassNodeAST(String name, String packageName) {
		this.setId(packageName + "-ClassNode-" + name);
		this.name = name;
		this.packageName = packageName;
		this.extendsClass = "";
		hasFinalModifier = false;
		hasAbstractModifier = false;
		hasPrivateModifier = false;
		hasPublicModifier = false;
		hasProtectedModifier = false;
		hasStaticModifier = false;
		hasSynchronizeModifier = false;
		numberOfMethods = 0;
		annotatios = new ArrayList<AnnotationNodeAST>();
		comments = new ArrayList<CommentsNodeAST>();
		impl = new ArrayList<ClassImplementsNodeAST>();
		method = new ArrayList<ClassHasMethodNodeAST>();
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getExtendsClass() {
		return extendsClass;
	}

	public void setExtendsClass(String extendsClass) {
		this.extendsClass = extendsClass;
	}

	public long getNumberOfMethods() {
		return numberOfMethods;
	}

	public void setNumberOfMethods(long numberOfMethods) {
		this.numberOfMethods = numberOfMethods;
	}

	public boolean isHasFinalModifier() {
		return hasFinalModifier;
	}

	public void setHasFinalModifier(boolean hasFinalModifier) {
		this.hasFinalModifier = hasFinalModifier;
	}

	public boolean isHasAbstractModifier() {
		return hasAbstractModifier;
	}

	public void setHasAbstractModifier(boolean hasAbstractModifier) {
		this.hasAbstractModifier = hasAbstractModifier;
	}

	public boolean isHasPrivateModifier() {
		return hasPrivateModifier;
	}

	public void setHasPrivateModifier(boolean hasPrivateModifier) {
		this.hasPrivateModifier = hasPrivateModifier;
	}

	public boolean isHasPublicModifier() {
		return hasPublicModifier;
	}

	public void setHasPublicModifier(boolean hasPublicModifier) {
		this.hasPublicModifier = hasPublicModifier;
	}

	public boolean isHasProtectedModifier() {
		return hasProtectedModifier;
	}

	public void setHasProtectedModifier(boolean hasProtectedModifier) {
		this.hasProtectedModifier = hasProtectedModifier;
	}

	public boolean isHasStaticModifier() {
		return hasStaticModifier;
	}

	public void setHasStaticModifier(boolean hasStaticModifier) {
		this.hasStaticModifier = hasStaticModifier;
	}

	public boolean isHasSynchronizeModifier() {
		return hasSynchronizeModifier;
	}

	public void setHasSynchronizeModifier(boolean hasSynchronizeModifier) {
		this.hasSynchronizeModifier = hasSynchronizeModifier;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<AnnotationNodeAST> getAnnotatios() {
		return annotatios;
	}

	public void setAnnotatios(List<AnnotationNodeAST> annotatios) {
		this.annotatios = annotatios;
	}

	public List<CommentsNodeAST> getComments() {
		return comments;
	}

	public void setComments(List<CommentsNodeAST> comments) {
		this.comments = comments;
	}

	public List<ClassImplementsNodeAST> getImpl() {
		return impl;
	}

	public void setImpl(List<ClassImplementsNodeAST> impl) {
		this.impl = impl;
	}

	public List<ClassHasMethodNodeAST> getMethod() {
		return method;
	}

	public void setMethod(List<ClassHasMethodNodeAST> method) {
		this.method = method;
	}
	
	public void setAllModifiers(int mod){
		if (Modifier.isFinal(mod)) {
			hasFinalModifier = true;
		}
		if (Modifier.isAbstract(mod)){
			hasAbstractModifier = true;
		}
		if (Modifier.isPrivate(mod)){
			hasPrivateModifier = true;
		}
		if (Modifier.isPublic(mod)){
			hasPublicModifier = true;
		}
		if (Modifier.isProtected(mod)){
			hasProtectedModifier = true;
		}
		if (Modifier.isStatic(mod)){
			hasStaticModifier = true;
		}
		if (Modifier.isSynchronized(mod)){
			hasStaticModifier = true;
		}
	}

}