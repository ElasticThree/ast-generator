package com.elasticthree.ASTCreator.ASTCreator.Neo4jDriver;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.neo4j.driver.v1.*;

import com.elasticthree.ASTCreator.ASTCreator.Objects.AnnotationNodeAST;
import com.elasticthree.ASTCreator.ASTCreator.Objects.ClassHasMethodNodeAST;
import com.elasticthree.ASTCreator.ASTCreator.Objects.ClassImplementsNodeAST;
import com.elasticthree.ASTCreator.ASTCreator.Objects.ClassNodeAST;
import com.elasticthree.ASTCreator.ASTCreator.Objects.CommentsNodeAST;
import com.elasticthree.ASTCreator.ASTCreator.Objects.FileNodeAST;
import com.elasticthree.ASTCreator.ASTCreator.Objects.ParameterMethodNodeAST;
import com.elasticthree.ASTCreator.ASTCreator.Objects.ThrowMethodNodeAST;

public class Neo4JDriver {

	final static Logger logger = Logger.getLogger(Neo4JDriver.class);
	private String host;
	private String usern;
	private String password;

	public Neo4JDriver() {
		Properties prop = new Properties();
		InputStream input = null;

		try {
			input = new FileInputStream("resources/config.properties");
			prop.load(input);
			this.host = prop.getProperty("host");
			this.usern = prop.getProperty("neo4j_username");
			this.password = prop.getProperty("neo4j_password");
		} catch (IOException ex) {
			logger.debug("IOException: ", ex);
			host = null;
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void insertNeo4JDB(FileNodeAST fileNodeAST) {

		if (fileNodeAST != null && getHost() != null) {
			Driver driver = GraphDatabase.driver("bolt://" + getHost(),
					AuthTokens.basic(getUsern(), getPassword()));
			Session session = driver.session();
			// File Node of AST
			logger.debug("// For testing we just printing the output");
			String fileNodeInsertQuery = "CREATE (";
			fileNodeInsertQuery += "f:File {";
			// File node properties
			fileNodeInsertQuery += "package:\'" 
					+ fileNodeAST.getPackageName() + "\',";
			fileNodeInsertQuery += "name:\'" 
					+ fileNodeAST.getName() + "\',";
			fileNodeInsertQuery += "NumberOfClasses:\'"
					+ String.valueOf(fileNodeAST.getNumberOfClasses()) + "\',";
			fileNodeInsertQuery += "NumberOfInterfaces:\'"
					+ String.valueOf(fileNodeAST.getNumberOfInterfaces()) + "\'";
			fileNodeInsertQuery += "})";

			if (fileNodeAST.getNumberOfClasses() > 0) {
				for (int i=0; i < fileNodeAST.getClasses().size(); i++){
					ClassNodeAST classNode =  fileNodeAST.getClasses().get(i);
					fileNodeInsertQuery += ",(";
					fileNodeInsertQuery += "class" + classNode.getName() + ":Class {";
					// Class node properties
					if (classNode.isHasFinalModifier())
						fileNodeInsertQuery += "HasFinalModifier:\'"
								+ String.valueOf(classNode.isHasFinalModifier()) + "\',";
					if (classNode.isHasAbstractModifier())
						fileNodeInsertQuery += "HasAbstractModifier:\'"
								+ String.valueOf(classNode.isHasAbstractModifier()) + "\',";
					if (classNode.isHasPrivateModifier())
						fileNodeInsertQuery += "HasPrivateModifier:\'"
								+ String.valueOf(classNode.isHasPrivateModifier()) + "\',";
					if (classNode.isHasPublicModifier())
						fileNodeInsertQuery += "HasPublicModifier:\'"
								+ String.valueOf(classNode.isHasPublicModifier()) + "\',";
					if (classNode.isHasProtectedModifier())
						fileNodeInsertQuery += "HasProtectedModifier:\'"
								+ String.valueOf(classNode.isHasProtectedModifier()) + "\',";
					if (classNode.isHasStaticModifier())
						fileNodeInsertQuery += "HasStaticModifier:\'"
								+ String.valueOf(classNode.isHasStaticModifier()) + "\',";
					if (classNode.isHasSynchronizeModifier())
						fileNodeInsertQuery += "HasSynchronizeModifier:\'"
								+ String.valueOf(classNode.isHasSynchronizeModifier()) + "\',";
					if (!classNode.getExtendsClass().equalsIgnoreCase("None")){
						fileNodeInsertQuery += "extends:\'" 
								+ classNode.getExtendsClass() + "\',";
					}
					fileNodeInsertQuery += "NumberOfMethods:\'"
							+ String.valueOf(classNode.getNumberOfMethods()) + "\',";
					fileNodeInsertQuery += "package:\'" 
							+ classNode.getPackageName() + "\',";
					fileNodeInsertQuery += "name:\'" 
							+ classNode.getName() + "\'";
					fileNodeInsertQuery += "})";
					
					// Annotation Node
					if (classNode.getAnnotatios().size() > 0) {
						for (int j=0; j < classNode.getAnnotatios().size(); j++){
							AnnotationNodeAST annotationNode =  classNode.getAnnotatios().get(j);
							fileNodeInsertQuery += ",(";
							fileNodeInsertQuery += "class" + classNode.getName() 
									+ "ann" + String.valueOf(j) + ":Annotation {";
							// Annotation node property
							fileNodeInsertQuery += "name:\'" 
									+ annotationNode.getName() + "\'";
							fileNodeInsertQuery += "})";
							
							// RELATION SHIP CLASS -> ANNOTATION
							fileNodeInsertQuery += ",(" 
									+ "class" + classNode.getName() + ")";
							
							fileNodeInsertQuery += "-[:HAS_ANNOTATION]->"; 
							fileNodeInsertQuery += "(" 
									+ "class" + classNode.getName() 
									+ "ann" + String.valueOf(j) + ")";		
						}
					}
					
					// Comments Node
					if (classNode.getComments().size() > 0) {
						for (int j=0; j < classNode.getComments().size(); j++){
							CommentsNodeAST commentNode =  classNode.getComments().get(j);
							fileNodeInsertQuery += ",(";
							fileNodeInsertQuery += "class" + classNode.getName() 
									+ "comment" + String.valueOf(j) + ":Comments {";
							// Comment node property
							fileNodeInsertQuery += "name:\'" 
									+ commentNode.getName() + "\'";
							fileNodeInsertQuery += "})";
							
							// RELATION SHIP CLASS -> COMMENT
							fileNodeInsertQuery += ",(" 
									+ "class" + classNode.getName() + ")";
							
							fileNodeInsertQuery += "-[:HAS_COMMENTS]->"; 
							fileNodeInsertQuery += "(" 
									+ "class" + classNode.getName() 
									+ "comment" + String.valueOf(j) + ")";		
						}
					}
					
					// Implements Interface Node
					if (classNode.getImpl().size() > 0) {
						for (int j=0; j < classNode.getImpl().size(); j++){
							ClassImplementsNodeAST implNode =  classNode.getImpl().get(j);
							fileNodeInsertQuery += ",(";
							fileNodeInsertQuery += "class" + classNode.getName() 
									+ "impl" + String.valueOf(j) + ":ImplementsInterface {";
							// Implements Interface node property
							fileNodeInsertQuery += "name:\'" 
									+ implNode.getName() + "\'";
							fileNodeInsertQuery += "})";
							
							// RELATION SHIP CLASS -> IMPLEMENTS_INTERFACE
							fileNodeInsertQuery += ",(" 
									+ "class" + classNode.getName() + ")";
							
							fileNodeInsertQuery += "-[:IMPLEMENTS_INTERFACE]->"; 
							fileNodeInsertQuery += "(" 
									+ "class" + classNode.getName() 
									+ "impl" + String.valueOf(j) + ")";		
						}
					}
					
					// Method Node
					if (classNode.getMethod().size() > 0) {
						for (int j=0; j < classNode.getMethod().size(); j++){
							ClassHasMethodNodeAST methodNode =  classNode.getMethod().get(j);
							fileNodeInsertQuery += ",(";
							fileNodeInsertQuery += "class" + classNode.getName() 
									+ "method" + String.valueOf(j) + ":Method {";
							
							if (methodNode.isHasFinalModifier())
								fileNodeInsertQuery += "HasFinalModifier:\'"
										+ String.valueOf(methodNode.isHasFinalModifier()) + "\',";
							if (methodNode.isHasAbstractModifier())
								fileNodeInsertQuery += "HasAbstractModifier:\'"
										+ String.valueOf(methodNode.isHasAbstractModifier()) + "\',";
							if (methodNode.isHasPrivateModifier())
								fileNodeInsertQuery += "HasPrivateModifier:\'"
										+ String.valueOf(methodNode.isHasPrivateModifier()) + "\',";
							if (methodNode.isHasPublicModifier())
								fileNodeInsertQuery += "HasPublicModifier:\'"
										+ String.valueOf(methodNode.isHasPublicModifier()) + "\',";
							if (methodNode.isHasProtectedModifier())
								fileNodeInsertQuery += "HasProtectedModifier:\'"
										+ String.valueOf(methodNode.isHasProtectedModifier()) + "\',";
							if (methodNode.isHasStaticModifier())
								fileNodeInsertQuery += "HasStaticModifier:\'"
										+ String.valueOf(methodNode.isHasStaticModifier()) + "\',";
							if (methodNode.isHasSynchronizeModifier())
								fileNodeInsertQuery += "HasSynchronizeModifier:\'"
										+ String.valueOf(methodNode.isHasSynchronizeModifier()) + "\',";
							
							fileNodeInsertQuery += "ReturningType:\'"
									+ methodNode.getReturningType() + "\',";
							fileNodeInsertQuery += "package:\'" 
									+ methodNode.getPackageName() + "\',";
							fileNodeInsertQuery += "name:\'" 
									+ methodNode.getName() + "\'";
							fileNodeInsertQuery += "})";
							
							//  Method's RelationShips 
							
							// Annotation Node
							if (methodNode.getAnnotatios().size() > 0) {
								for (int k=0; k < methodNode.getAnnotatios().size(); k++){
									AnnotationNodeAST annotationNode =  methodNode.getAnnotatios().get(k);
									fileNodeInsertQuery += ",(";
									fileNodeInsertQuery += "method" + methodNode.getName() 
											+ "ann" + String.valueOf(k) + ":Annotation {";
									// Annotation node property
									fileNodeInsertQuery += "name:\'" 
											+ annotationNode.getName() + "\'";
									fileNodeInsertQuery += "})";
									
									// RELATION SHIP METHOD -> ANNOTATION
									fileNodeInsertQuery += ",(" 
											+ "class" + classNode.getName() 
											+ "method" + String.valueOf(j)  + ")";
									
									fileNodeInsertQuery += "-[:HAS_ANNOTATION]->"; 
									fileNodeInsertQuery += "(" 
											+ "method" + methodNode.getName() 
											+ "ann" + String.valueOf(k) + ")";		
								}
							}
							
							// Comments Node
							if (methodNode.getComments().size() > 0) {
								for (int k=0; k < methodNode.getComments().size(); k++){
									CommentsNodeAST commentNode =  methodNode.getComments().get(k);
									fileNodeInsertQuery += ",(";
									fileNodeInsertQuery += "method" + methodNode.getName() 
											+ "comment" + String.valueOf(k) + ":Comments {";
									// Comment node property
									fileNodeInsertQuery += "name:\'" 
											+ commentNode.getName() + "\'";
									fileNodeInsertQuery += "})";
									
									// RELATION SHIP METHOD -> COMMENT
									fileNodeInsertQuery += ",(" 
											+ "class" + classNode.getName() 
											+ "method" + String.valueOf(j) + ")";
									
									fileNodeInsertQuery += "-[:HAS_COMMENT]->"; 
									fileNodeInsertQuery += "(" 
											+ "method" + methodNode.getName() 
											+ "comment" + String.valueOf(j) + ")";		
								}
							}
							
							// Parameter Node
							if (methodNode.getParameters().size() > 0) {
								for (int k=0; k < methodNode.getParameters().size(); k++){
									ParameterMethodNodeAST paramNode =  methodNode.getParameters().get(k);
									fileNodeInsertQuery += ",(";
									fileNodeInsertQuery += "method" + methodNode.getName() 
											+ "param" + String.valueOf(k) + ":Parameter {";
									// Parameter node property
									fileNodeInsertQuery += "name:\'" 
											+ paramNode.getName() + "\'";
									fileNodeInsertQuery += "})";
									
									// RELATION SHIP METHOD -> PARAMETER
									fileNodeInsertQuery += ",(" 
											+ "class" + classNode.getName() 
											+ "method" + String.valueOf(j) + ")";
									
									fileNodeInsertQuery += "-[:HAS_PARAMETER]->"; 
									fileNodeInsertQuery += "(" 
											+ "method" + methodNode.getName() 
											+ "param" + String.valueOf(k) + ")";		
								}
							}
							
							// Throw Method Node
							if (methodNode.getThrowsMethod().size() > 0) {
								for (int k=0; k < methodNode.getThrowsMethod().size(); k++){
									ThrowMethodNodeAST throwNode =  methodNode.getThrowsMethod().get(k);
									fileNodeInsertQuery += ",(";
									fileNodeInsertQuery += "method" + methodNode.getName() 
											+ "throw" + String.valueOf(k) + ":Throw {";
									// Throw node property
									fileNodeInsertQuery += "name:\'" 
											+ throwNode.getName() + "\'";
									fileNodeInsertQuery += "})";
									
									// RELATION SHIP METHOD -> THROW
									fileNodeInsertQuery += ",(" 
											+ "class" + classNode.getName() 
											+ "method" + String.valueOf(j) + ")";
									
									fileNodeInsertQuery += "-[:HAS_THROW]->"; 
									fileNodeInsertQuery += "(" 
											+ "method" + methodNode.getName() 
											+ "throw" + String.valueOf(k) + ")";		
								}
							}
							// RELATION SHIP CLASS -> METHOD
							fileNodeInsertQuery += ",(" 
									+ "class" + classNode.getName() + ")";
							
							fileNodeInsertQuery += "-[:HAS_METHOD]->"; 
							fileNodeInsertQuery += "(" 
									+ "class" + classNode.getName() 
									+ "method" + String.valueOf(j) + ")";		
						}
					}
					// RELATION SHIP FILE -> CLASS
					fileNodeInsertQuery += ",(" 
							+ "f" + ")";
					
					fileNodeInsertQuery += "-[:HAS_CLASS]->"; 
					fileNodeInsertQuery += "(" 
							+ "class" + classNode.getName() + ")";
				}
			}

			if (fileNodeAST.getNumberOfInterfaces() > 0) {

			}

			fileNodeInsertQuery += ";";
			logger.debug("fileNodeInsertQuery: " + fileNodeInsertQuery);

			// session.run("CREATE (a:Person {name:'Arthur', title:'King'})");

			session.close();
			driver.close();
		}
	}

	public String getHost() {
		return host;
	}

	public String getUsern() {
		return usern;
	}

	public String getPassword() {
		return password;
	}

}