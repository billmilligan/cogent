package org.cogent.validation;

import static org.junit.jupiter.api.Assertions.* ;

import org.junit.jupiter.api.Test ;

public class ValidationCodeTest {

	public static enum TestValidationCode implements ValidationCode {
		FOO, BAR, BAZ
	}

	@Test
	public void testBasic ( ) {
		ValidationCode cd = TestValidationCode.BAR ;
		assertEquals ( "BAR", cd.name ( ) ) ;
		assertEquals ( "BAR", cd.getSimpleName ( ) ) ;
		assertEquals ( "TestValidationCode:BAR", cd.getFullyQualifiedName ( ) ) ;
	}
}
