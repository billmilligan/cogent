package org.cogent.model.output;

import static org.junit.jupiter.api.Assertions.assertEquals ;

import org.cogent.model.output.styles.Styles ;
import org.junit.jupiter.api.Test ;

public class StringLiteralTest {

	@Test
	public void testNull ( ) {
		StringLiteral lit = new StringLiteral ( null ) ;
		assertEquals ( "null", lit.render ( Styles.defaultContext ( ) ) ) ;
	}

	@Test
	public void testNonNull ( ) {
		StringLiteral lit = new StringLiteral ( "Hello" ) ;
		assertEquals ( "\"Hello\"", lit.render ( Styles.defaultContext ( ) ) ) ;
	}
}
