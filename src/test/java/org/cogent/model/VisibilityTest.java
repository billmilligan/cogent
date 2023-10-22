package org.cogent.model;

import static org.cogent.model.VisibilityModifier.* ;
import static org.junit.jupiter.api.Assertions.* ;

import org.junit.jupiter.api.Test ;

public class VisibilityTest {

	@Test
	public void testToString ( ) {
		assertEquals ( "public", PUBLIC.toString ( ) ) ;
		assertEquals ( "protected", PROTECTED.toString ( ) ) ;
		assertEquals ( "private", PRIVATE.toString ( ) ) ;
		assertEquals ( "", PACKAGE_PROTECTED.toString ( ) ) ;
	}
}
