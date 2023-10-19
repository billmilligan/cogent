package org.cogent.validation;


public interface Validatable {

	public default void validate ( ) {
		validate ( new ValidationContext ( ) ) ;
	}

	public void validate ( ValidationContext ctx ) ;
}
