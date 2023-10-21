package org.cogent.io;

import static org.junit.jupiter.api.Assertions.* ;

import java.io.PrintWriter ;

import org.junit.jupiter.api.Test ;

public class PrintWriterSinkTest {

	private static final String newln = System.getProperty ( "line.separator" ) ;

	@Test
	public void ofTest ( ) throws Throwable {
		TestObject e = new TestObject ( ) ;
		String actual = String.valueOf ( PrintWriterSink.of ( e::printSomething, new WriteContext ( ) ) ) ;
		String expected = "\tSomething!" ;
		assertEquals ( expected, actual ) ;
	}

	@Test
	public void testBasics ( ) throws Throwable {
		try ( PrintWriterSink pw = new PrintWriterSink ( ) ) {
			pw.println ( "Hello Worlds!") ;
			String actual = pw.getContents ( ) ;
			String expected = "Hello Worlds!" + newln ;
			assertEquals ( expected, actual, "Correct including system-dependent newln" ) ;
	
			int expectedLen = expected.length ( ) ;
			assertEquals ( expectedLen, pw.length ( ), "Correct length" ) ;
	
			for ( int i = 0 ; i <= pw.length ( ) ; i++ ) {
				if ( i < pw.length ( ) ) {
					assertEquals ( expected.charAt ( i ), pw.charAt ( i ), "Char at is right" ) ;
				} else {
					try {
						pw.charAt ( i ) ;
						fail ( "Fail to act like a proper char sequence" ) ;
					} catch ( IndexOutOfBoundsException ioobe ) {
						;
					}
				}
			}
			actual = String.valueOf ( pw.subSequence ( 0, 5 ) ) ;
			assertEquals ( "Hello", actual, "Correct subsequence" ) ;
	
			pw.println ( "Another one!" ) ; // won't change anything yet
			actual = pw.getContents ( ) ;
			expected = "Hello Worlds!" + newln ;
			assertEquals ( expected, actual, "Has not changed yet" ) ;
	
			pw.reset ( ) ;
			actual = pw.getContents ( ) ;
			expected = "Hello Worlds!" + newln + "Another one!" + newln ;
		}
	}

	private static class TestObject {
		public void printSomething ( PrintWriter pw, WriteContext ctx ) {
			ctx.inc ( ) ;
			pw.print ( ctx.getIndent ( ) + "Something!" ) ;
		}
	}
}
