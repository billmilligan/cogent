package org.cogent.model.playground;

import static org.junit.jupiter.api.Assertions.* ;

import org.cogent.model.JavaClassDefinition ;
import org.cogent.model.JavaSourceFile ;
import org.cogent.model.VisibilityModifier ;
import org.cogent.startup.Startup ;
import org.cogent.validation.MultiException ;
import org.cogent.validation.ValidationException ;
import org.junit.jupiter.api.BeforeEach ;
import org.junit.jupiter.api.Test ;

public class CogentPlayground {

	@BeforeEach
	public void setup ( ) {
		Startup.init ( ) ;
	}

	@Test
	public void badClassName ( ) throws Exception {
		JavaClassDefinition def = new JavaClassDefinition ( "A1234" ) ;
		def.setVisibility ( VisibilityModifier.PRIVATE ) ;
		JavaSourceFile jf = new JavaSourceFile ( "123!4.jarva" ) ;
		jf.setMainClass ( def ) ;
		boolean caughtValidation = false ;
		MultiException caught = null ;
		try {
			jf.validate ( ) ;
		} catch ( MultiException me ) {
			caughtValidation = true ;
			caught = me ;
		}
		assertTrue ( caughtValidation, "Caught correct exception" ) ;

		assertEquals ( 5, caught.getContents ( ).size ( ), "Correct number of validations caught" ) ;
		Exception e = caught.getContents ( ).get ( 0 ) ;
		assertNotNull ( e ) ;
		if ( ! ( e instanceof ValidationException ve ) ) {
			fail ( "Not a validation exception, " + e ) ;
		} else {
			
		}
		throw caught ;
	}
}
