package org.cogent.validation;

import java.util.Stack ;

import org.cogent.io.StringFactory ;

public class ValidationContext {

	private Stack <Tray> context = new Stack <> ( ) ;

	public void contextualize ( Runnable r, Object ... ss ) {
		pushContext ( ss ) ;
		r.run ( ) ;
		popContext ( ) ;
	}

	public void pushContext ( Object ... ss ) {
		context.push ( new Tray ( assemble ( ss ), null ) ) ;
	}

	public void pushContext ( Throwable t ) {
		context.push ( new Tray ( null, t ) ) ;
	}

	public void pushContext ( Throwable t, Object ... ss ) {
		context.push ( new Tray ( assemble ( ss ), t ) ) ;
	}

	private String assemble ( Object ... ss ) {
		StringFactory sf = new StringFactory ( ) ;
		boolean first = true ;
		for ( Object s : ss ) {
			if ( first ) {
				first = false ;
			} else {
				sf.append ( ", " ) ;
			}
			sf.append ( s == null ? "null" : quote ( s ) ) ;
		}
		return sf.toString ( ) ;
	}

	private String quote ( Object s ) {
		return "\"" + s + "\"" ;
	}

	public void popContext ( ) {
		context.pop ( ) ;
	}

	public void validateJavaIdentifier ( String name ) {
		contextualize ( ( ) -> {
			if ( name == null || name.isBlank ( ) ) {
				fail ( "Null or blank is an invalid Java identifier" ) ;
			} else {
				char c = name.charAt ( 0 ) ;
				contextualize ( ( ) -> {
					if ( ! Character.isJavaIdentifierStart ( c ) ) {
						fail ( "Invalid starting character" ) ;
					}
				}, "Validate first character", c ) ;
				for ( int i = 1 ; i < name.length ( ) ; i++ ) {
					char s = name.charAt ( i ) ;
					contextualize ( ( ) -> {
						if ( ! Character.isJavaIdentifierPart ( s ) ) {
							fail ( "Invalid subsequent character" ) ;
						}
					}, "Validate subsequent character", i, s ) ;
				}
			}
		}, "Validate Java Identifier", name ) ;
	}

	public void fail ( Object ... messages ) {
		throw new ValidationException ( context, assemble ( messages ) ) ;
	}

	public void fail ( Throwable t ) {
		throw new ValidationException ( context, t ) ;
	}

	public void fail ( Throwable t, Object ... messages ) {
		throw new ValidationException ( context, assemble ( messages ) , t ) ;
	}

	public static record Tray ( String message, Throwable t ) { ; }
}
