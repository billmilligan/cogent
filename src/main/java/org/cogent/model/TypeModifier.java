package org.cogent.model;


public enum TypeModifier {

	STATIC, ABSTRACT, FINAL, SEALED, NON_SEALED ;

	@Override
	public String toString ( ) {
		switch ( this ) {
			case NON_SEALED:	return "non-sealed" ;
			default:			return name ( ).toLowerCase ( ) ;
		}
	}
}
