package org.cogent.model;


public enum VisibilityModifier {

	PUBLIC, PROTECTED, PRIVATE, PACKAGE_PROTECTED ;

	@Override
	public String toString ( ) {
		switch ( this ) {
			case PUBLIC:
			case PROTECTED:
			case PRIVATE:	return name ( ).toLowerCase ( ) ;
			default:		return "" ;
		}
	}
}
