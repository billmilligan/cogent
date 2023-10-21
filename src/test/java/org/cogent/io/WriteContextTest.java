package org.cogent.io;

import org.junit.jupiter.api.Test ;

import static org.junit.jupiter.api.Assertions.* ;

public class WriteContextTest {

	@Test
	public void indentationTest ( ) throws Exception {
		WriteContext ctx = new WriteContext ( ) ;
		assertEquals ( "", ctx.getIndent ( ) ) ;
		ctx.inc ( ) ;
		assertEquals ( "\t", ctx.getIndent ( ) ) ;
		ctx.inc ( ) ;
		assertEquals ( "\t\t", ctx.getIndent ( ) ) ;
		ctx.dec ( ) ;
		assertEquals ( "\t", ctx.getIndent ( ) ) ;
		ctx.dec ( ) ;
		assertEquals ( "", ctx.getIndent ( ) ) ;
		ctx.dec ( ) ;
		assertEquals ( "", ctx.getIndent ( ), "If we go negative, so be it" ) ;
	}

	@Test
	public void endStatementTest ( ) throws Exception {
		WriteContext ctx = new WriteContext ( ) ;
		assertEquals ( " ;", ctx.endStatement ( ) ) ;
	}
}
