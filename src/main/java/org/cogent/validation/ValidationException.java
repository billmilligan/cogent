package org.cogent.validation;

import java.util.ArrayList ;
import java.util.List ;

import org.cogent.validation.ValidationContext.Tray ;

public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 4428956733871808677L ;
	private List <Tray> context = new ArrayList <> ( ) ;

	public ValidationException ( List <ValidationContext.Tray> context ) { this ( context, null, null ) ; }
	public ValidationException ( List <ValidationContext.Tray> context, String s ) { this ( context, s, null ) ; }
	public ValidationException ( List <ValidationContext.Tray> context, Throwable t ) { this ( context, null, t ) ; }
	public ValidationException ( List <ValidationContext.Tray> context, String s, Throwable t ) {
		super ( s, t ) ;
		this.context.addAll ( context ) ;
	}
	public List <Tray> getContext ( ) {
		return context ;
	}
}
