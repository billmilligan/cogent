package org.cogent.model;


public enum TypeModifier {

	STATIC, ABSTRACT, FINAL ;

	@Override
	public String toString ( ) {
		return name ( ).toLowerCase ( ) ;
	}
}
