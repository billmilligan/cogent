package org.cogent.model;


public enum VisibilityModifier {

	PUBLIC, PROTECTED, PRIVATE, PACKAGE_PROTECTED ;

	@Override
	public String toString ( ) {
		switch ( this ) {
			case PUBLIC: return "public" ;
			case PROTECTED: return "protected" ;
			case PRIVATE: return "private" ;
			default:	return "" ;
		}
	}
}
