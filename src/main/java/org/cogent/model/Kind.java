package org.cogent.model;


public enum Kind {

	CLASS, INTERFACE, ANNOTATION, ENUM, RECORD ;

	@Override
	public String toString ( ) {
		switch ( this ) {
			case ANNOTATION:	return "@interface" ;
			default:
				return name ( ).toLowerCase ( ) ;
		}
	}
}
