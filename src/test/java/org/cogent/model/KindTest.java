package org.cogent.model;

import static org.cogent.model.Kind.* ;
import static org.junit.jupiter.api.Assertions.* ;

import org.junit.jupiter.api.Test ;

// It's very nice.
public class KindTest {

	@Test
	public void testToString ( ) {
		assertEquals ( "@interface", ANNOTATION.toString ( ) ) ;
		assertEquals ( "class", CLASS.toString ( ) ) ;
		assertEquals ( "enum", ENUM.toString ( ) ) ;
		assertEquals ( "interface", INTERFACE.toString ( ) ) ;
		assertEquals ( "record", RECORD.toString ( ) ) ;
	}
}
