package org.cogent.io;

import org.cogent.io.StringFactory.PlaceHolder ;
import org.junit.jupiter.api.Test ;

import static org.junit.jupiter.api.Assertions.* ;

public class StringFactoryTest {

	@Test
	public void testBasics ( ) throws Exception {
		String endln = System.getProperty ( "line.separator" ) ;
		StringFactory sf = new StringFactory ( ) ;
		sf.append ( "append;" ) ;
		sf.append ( ( ) -> "supplier;" ) ;
		PlaceHolder mark = sf.placeholder ( ) ;
		sf.appendln ( "appendWithEndLn;" ) ;
		sf.appendln ( 2 ) ;
		StringFactory internal = mark.getFactory ( ) ;
		internal.append ( 123456789, 9 ) ;
		internal.append ( ";" ) ;
		PlaceHolder markTwain = internal.placeholder ( ) ;
		internal.append ( ( ) -> Math.PI, 12 ) ;
		internal.append ( ";" ) ;
		markTwain.getFactory ( ).appendln ( ( ) -> Math.E, 10 ) ;
		PlaceHolder ignoreThisOne = markTwain.getFactory ( ).placeholder ( ) ;
		assertNotNull ( ignoreThisOne ) ;

		StringBuilder expected = new StringBuilder ( ) ;
		expected.append ( "append;supplier;123456789;" ) ;
		expected.append ( Math.E ) ;
		expected.append ( endln ) ;
		expected.append ( Math.PI ) ;
		expected.append ( ";appendWithEndLn;" ) ;
		expected.append ( endln ) ;
		expected.append ( "2" ) ;
		expected.append ( endln ) ;

		assertTrue ( sf.getLength ( ) >= expected.length ( ), "So long as it's reasonable" ) ;
		assertEquals ( expected.toString ( ), sf.toString ( ), "Full match!" ) ;
	}

	@Test
	public void testObjectConstructor ( ) {
		StringFactory sf = new StringFactory ( "Hello!" ) ;
		assertEquals ( "Hello!", sf.toString ( ) ) ;
	}

	@Test
	public void testSupplierConstructor ( ) {
		String endln = System.getProperty ( "line.separator" ) ;
		StringFactory sf = new StringFactory ( ( ) -> "Hello!" ) ;
		assertEquals ( "Hello!", sf.toString ( ) ) ;
		sf.appendln ( ( ) -> " There!" ) ;
		assertEquals ( "Hello! There!" + endln, sf.toString ( ) ) ;
	}

	@Test
	public void testNullSupplierConstructor ( ) {
		StringFactory sf = new StringFactory ( null ) ;
		assertEquals ( "null", sf.toString ( ) ) ;
	}

	@Test
	public void testNullAppend ( ) {
		StringFactory sf = new StringFactory ( ) ;
		sf.append ( ( Object ) null ) ;
		assertEquals ( "null", sf.toString ( ) ) ;
	}
}
