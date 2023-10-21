package org.cogent.validation;

import java.io.OutputStreamWriter ;
import java.io.PrintStream ;
import java.io.PrintWriter ;
import java.util.ArrayList ;
import java.util.List ;

import org.cogent.messages.MessageRegistry ;
import org.cogent.startup.Starter ;
import org.cogent.startup.StarterContext ;
import org.cogent.validation.ValidationContext.Tray ;

import static org.cogent.validation.TemplateType.* ;

public class ValidationException extends RuntimeException {

	public static enum SystemValidationCode implements ValidationCode {
		UNKNOWN
	}

	public static class ValidationExceptionStarter implements Starter {
		@Override
		public void start ( StarterContext ctx ) {
			ctx.registerMessage ( SystemValidationCode.UNKNOWN, TemplateType.CONTEXT, "Unknown context", 0 ) ;
			ctx.registerMessage ( SystemValidationCode.UNKNOWN, TemplateType.FAILURE, "Unknown error", 0 ) ;
		}
	}

	public static ValidationCode codeOf ( Exception e ) {
		if ( e instanceof ValidationException ve ) {
			return ve.code ;
		} else {
			return SystemValidationCode.UNKNOWN ;
		}
	}

	private static final long serialVersionUID = 4428956733871808677L ;
	private List <Tray> context = new ArrayList <> ( ) ;
	private ValidationCode code ;
	private MessageRegistry reg ;
	private Object [ ] params ;

	public ValidationException ( ValidationCode cd, Object [ ] params, List <ValidationContext.Tray> context, MessageRegistry reg ) { this ( cd, params, context, reg, null, null ) ; }
	public ValidationException ( ValidationCode cd, Object [ ] params, List <ValidationContext.Tray> context, MessageRegistry reg, String s ) { this ( cd, params, context, reg, s, null ) ; }
	public ValidationException ( ValidationCode cd, Object [ ] params, List <ValidationContext.Tray> context, MessageRegistry reg, Throwable t ) { this ( cd, params, context, reg, null, t ) ; }
	public ValidationException ( ValidationCode cd, Object [ ] params, List <ValidationContext.Tray> context, MessageRegistry reg, String s, Throwable t ) {
		super ( s, t ) ;
		this.context.addAll ( context ) ;
		this.code = cd ;
		this.reg = reg ;
		this.params = params ;
	}

	public Object [ ] getParams ( ) {
		return params ;
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
		pw.flush ( ) ;
//		super.printStackTrace ( pw ) ;
    }
}
