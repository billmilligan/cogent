package org.cogent.validation;


public interface Validatable {

	public default void validate ( ) {
		ValidationContext ctx = new ValidationContext ( ) ;
		validate ( ctx ) ;
		ctx.airGrievances ( ) ;
	}

	public void validate ( ValidationContext ctx ) ;
}
