package org.cogent.validation ;

import java.util.ArrayList ;
import java.util.Arrays ;
import java.util.List ;
import java.util.Stack ;

import org.cogent.messages.MessageRegistry ;
import org.cogent.startup.Starter ;
import org.cogent.startup.StarterContext ;

import static org.cogent.validation.ValidationContext.NamingValidationCode.* ;
import static org.cogent.validation.TemplateType.* ;

public class ValidationContext {

	public static enum NamingValidationCode implements ValidationCode {
		JAVA_IDENTIFIER, BLANK_IDENTIFIER, STARTING_CHARACTER,
		SUBSEQUENT_CHARACTER
	}

	public static class NamingValidationMessages implements Starter {
		@Override
		public void start ( StarterContext ctx ) {
			ctx.registerMessage ( JAVA_IDENTIFIER, CONTEXT, "Validating Name {0}", 1 ) ;
			ctx.registerMessage ( JAVA_IDENTIFIER, FAILURE, "Failed to validate name {0}", 1 ) ;
			ctx.registerMessage ( BLANK_IDENTIFIER, CONTEXT, "Validating Java identifier for blank", 0 ) ;
			ctx.registerMessage ( BLANK_IDENTIFIER, FAILURE, "Null or blank is an invalid Java identifier", 0 ) ;
			ctx.registerMessage ( STARTING_CHARACTER, CONTEXT, "Testing identifier starting character {0}", 1 ) ;
			ctx.registerMessage ( STARTING_CHARACTER, FAILURE, "Invalid starting character {0}", 1 ) ;
			ctx.registerMessage ( SUBSEQUENT_CHARACTER, CONTEXT, "Testing subsequent character at position {0}", 1 ) ;
			ctx.registerMessage ( SUBSEQUENT_CHARACTER, FAILURE, "Invalid subsequent character {1} at position {0}", 2 ) ;
		}
	}

	private Stack <Tray> context = new Stack <> ( ) ;
	private List <ValidationException> problems = new ArrayList <> ( ) ; 
	private MessageRegistry registry ;

	protected ValidationContext ( MessageRegistry reg ) {
		this.registry = reg ;
	}

	public static ValidationContext create ( ) {
		return of ( MessageRegistry.INSTANCE ) ;
	}

	public static ValidationContext of ( MessageRegistry reg ) {
		return new ValidationContext ( reg ) ;
	}

	public void contextualize ( ValidationCode cd, Runnable r, Object ... ss ) {
		pushContext ( cd, ss ) ;
		r.run ( ) ;
		popContext ( ) ;
	}

	public void pushContext ( ValidationCode cd, Object ... ss ) {
		context.push ( new Tray ( cd, ss, null ) ) ;
	}

	public void pushContext ( ValidationCode cd, Throwable t, Object ... ss ) {
		context.push ( new Tray ( cd, ss, t ) ) ;
	}

	private String assemble ( ValidationCode cd, TemplateType type, Object [ ] ss ) {
		return registry.format ( cd, type, ss ) ;
	}

	public void popContext ( ) {
		context.pop ( ) ;
	}

	// Because once you pop, you actually *can* stop ...
	public boolean canPop ( ) {
		return ! context.isEmpty ( ) ;
	}

	public void validateJavaIdentifier ( String name ) {
		contextualize ( JAVA_IDENTIFIER, ( ) -> {
			if ( name == null || name.isBlank ( ) ) {
				fail ( BLANK_IDENTIFIER ) ;
			} else {
				char c = name.charAt ( 0 ) ;
				contextualize ( STARTING_CHARACTER, ( ) -> {
					if ( ! Character.isJavaIdentifierStart ( c ) ) {
						failCurrentContext ( ) ;
					}
				}, c ) ;
				for ( int i = 1 ; i < name.length ( ) ; i++ ) {
					char s = name.charAt ( i ) ;
					contextualize ( SUBSEQUENT_CHARACTER, ( ) -> {
						if ( ! Character.isJavaIdentifierPart ( s ) ) {
							failCurrentContext ( s ) ;
						}
					}, i ) ;
				}
			}
		}, name ) ;
	}

	public void failCurrentContext ( Object ... messages ) {
		failCurrentContext ( null, messages ) ;
	}

	public void failCurrentContext ( Throwable t, Object ... messages ) {
		Tray current = context.peek ( ) ;
		List <Object> stuff = new ArrayList <> ( Arrays.asList ( current.message ) ) ;
		stuff.addAll ( Arrays.asList ( messages ) ) ;
		addProblem ( new ValidationException ( current.code, stuff.toArray ( ), context, registry, assemble ( current.code, FAILURE, stuff.toArray ( ) ) , t ) ) ;
	}

	public void fail ( ValidationCode cd, Object ... messages ) {
		fail ( cd, null, messages ) ;
	}

	public void fail ( ValidationCode cd, Throwable t, Object ... messages ) {
		int cap = registry.messageCountFor ( cd, CONTEXT ) ;
		Object [ ] contextMessages = new Object [ cap ] ;
		System.arraycopy ( messages, 0, contextMessages, 0, cap ) ;
		pushContext ( cd, t, contextMessages ) ;
		addProblem ( new ValidationException ( cd, messages, context, registry, assemble ( cd, FAILURE, messages ) , t ) ) ;
		popContext ( ) ;
	}

	private void addProblem ( ValidationException ve ) {
		try {
			throw ve ; // get that stack trace
		} catch ( ValidationException e ) {
			problems.add ( e ) ;
		}
	}

	public static record Tray ( ValidationCode code, Object [ ] message, Throwable throwable ) { ; }

	public void airGrievances ( ) {
		try {
			if ( problems.isEmpty ( ) ) {
				return ;
			} else if ( problems.size ( ) == 1 ) {
				throw problems.get ( 0 ) ;
			} else {
				throw new MultiException ( new ArrayList <> ( problems ) ) ;
			}
		} finally {
			problems.clear ( ) ;
		}
	}
}
