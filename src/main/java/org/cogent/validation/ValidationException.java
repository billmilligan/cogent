package org.cogent.validation;

import java.io.OutputStreamWriter ;
import java.io.PrintStream ;
import java.io.PrintWriter ;
import java.util.ArrayList ;
import java.util.List ;

import org.cogent.model.MessageRegistry ;
import org.cogent.validation.ValidationContext.Tray ;

import static org.cogent.validation.TemplateType.* ;

public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 4428956733871808677L ;
	private List <Tray> context = new ArrayList <> ( ) ;
	private ValidationCode code ;
	private MessageRegistry reg ;

	public ValidationException ( ValidationCode cd, List <ValidationContext.Tray> context, MessageRegistry reg ) { this ( cd, context, reg, null, null ) ; }
	public ValidationException ( ValidationCode cd, List <ValidationContext.Tray> context, MessageRegistry reg, String s ) { this ( cd, context, reg, s, null ) ; }
	public ValidationException ( ValidationCode cd, List <ValidationContext.Tray> context, MessageRegistry reg, Throwable t ) { this ( cd, context, reg, null, t ) ; }
	public ValidationException ( ValidationCode cd, List <ValidationContext.Tray> context, MessageRegistry reg, String s, Throwable t ) {
		super ( s, t ) ;
		this.context.addAll ( context ) ;
		this.code = cd ;
		this.reg = reg ;
	}

	public ValidationCode getCode ( ) {
		return code ;
	}

	public List <Tray> getContext ( ) {
		return context ;
	}

	public void printStackTrace ( ) {
        printStackTrace ( System.err ) ;
    }

    public void printStackTrace ( PrintStream s ) {
        printStackTrace ( new PrintWriter ( new OutputStreamWriter ( s ) ) ) ;
    }

    public void printStackTrace ( PrintWriter pw ) {
    	String indent = "\t" ;
		pw.println ( indent + " ============ CONTEXT ============ " ) ;
		for ( int i = 0 ; i < context.size ( ) ; i++ ) {
			Tray t = context.get ( i ) ;
			String message = reg.format ( t.code ( ), CONTEXT, t.message ( ) ) ;
			Throwable throwable = t.t ( ) ;
			if ( message != null ) {
				pw.print ( indent ) ;
				for ( int j = 0 ; j < i ; j++ ) {
					pw.print ( " " ) ;
				}
				pw.println ( message ) ;
			}
			if ( throwable != null ) {
				pw.println ( indent + indent + "======== INLINE EXCEPTION ======== " ) ;
				throwable.printStackTrace ( pw ) ;
			}
		}
		pw.println ( indent + " ============ TRACE ( " + code + " ) ============ " ) ;
		pw.println ( getMessage ( ) ) ;
//		super.printStackTrace ( pw ) ;
    }
}
