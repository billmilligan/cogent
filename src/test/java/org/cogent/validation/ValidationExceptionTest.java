package org.cogent.validation;

import org.cogent.io.PrintWriterSink ;
import org.cogent.messages.MessageRegistry ;
import org.junit.jupiter.api.AfterEach ;
import org.junit.jupiter.api.Test ;

import static org.cogent.model.util.TDDTrickBag.* ;
import static org.cogent.validation.ValidationException.SystemValidationCode.* ;
import static org.cogent.validation.ValidationExceptionTest.TestKey.* ;
import static org.junit.jupiter.api.Assertions.* ;

import java.io.ByteArrayOutputStream ;
import java.io.PrintStream ;
import java.util.Collections ;

/*
 * Yeah, I know it&apos;s a little unusual to test
 * exceptions but this is an odd case for which it&apos;s
 * important that the ergonomics are nice.
 */
public class ValidationExceptionTest {

	public static enum TestKey implements ValidationCode {
		FOO, BAR, BAZ, QUUX
	}

	private MessageRegistry reg = new MessageRegistry ( ) { ; } ;

	@AfterEach
	public void reset ( ) {
		reg.clear ( ) ;
	}

	@Test
	public void testBasics ( ) throws Exception {
		ValidationException t = buildTrace ( new ValidationException ( FOO, new Object [ 0 ], Collections.emptyList ( ), reg ) ) ;
		PrintWriterSink sink = new PrintWriterSink ( ) ;
		t.printStackTrace ( sink ) ;
		String actual = String.valueOf ( sink ) ;
		assertNotNull ( actual ) ;
		assertContains ( actual, "CONTEXT" ) ;
		assertContains ( actual, "TRACE" ) ;
		assertContains ( actual, "FOO" ) ;
		assertEquals ( FOO, t.getCode ( ) ) ;
	}

	private <T extends Exception> T buildTrace ( T t ) {
		try {
			throw ( Exception ) t ;
		} catch ( Exception ve ) {
			; // it's fine.  Just need to generate a stack trace!
			return t ;
		}
	}

	@Test
	public void testSysErr ( ) throws Exception {
		ByteArrayOutputStream bs = new ByteArrayOutputStream ( ) ;
		PrintStream newErr = new PrintStream ( bs ) ;
		PrintStream oldErr = System.err ;
		try {
			System.setErr ( newErr ) ;
			ValidationException t = buildTrace ( new ValidationException ( FOO, new Object [ 0 ], Collections.emptyList ( ), reg ) ) ;
			t.printStackTrace ( ) ;
			System.err.flush ( ) ;
			String actual = new String ( bs.toByteArray ( ) ) ;
			assertTrue ( actual.length ( ) > 0, "Non-trivial contents" ) ;
			assertContains ( actual, "CONTEXT" ) ;
			assertContains ( actual, "TRACE" ) ;
			assertContains ( actual, "FOO" ) ;
			assertEquals ( FOO, t.getCode ( ) ) ;
		} finally {
			System.setErr ( oldErr ) ;
		}
	}

	@Test
	public void testCodeOf ( ) throws Exception {
		Exception t = buildTrace ( new ValidationException ( FOO, new Object [ 0 ], Collections.emptyList ( ), reg ) ) ;
		Exception s = buildTrace ( new IllegalStateException ( "Franklin" ) ) ;
		assertEquals ( FOO, ValidationException.codeOf ( t ) ) ;
		assertEquals ( UNKNOWN, ValidationException.codeOf ( s ) ) ;
	}
}
