package org.cogent.model;

import static org.cogent.model.TypeModifier.* ;
import static org.junit.jupiter.api.Assertions.* ;

import org.junit.jupiter.api.Test ;

public class TypeModifierTest {

	@Test
	public void testToString ( ) {
		assertEquals ( "abstract", ABSTRACT.toString ( ) ) ;
		assertEquals ( "final", FINAL.toString ( ) ) ;
		assertEquals ( "static", STATIC.toString ( ) ) ;
		assertEquals ( "sealed", SEALED.toString ( ) ) ;
		assertEquals ( "non-sealed", NON_SEALED.toString ( ) ) ;
	}
}
