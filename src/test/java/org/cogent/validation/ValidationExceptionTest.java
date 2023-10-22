package org.cogent.validation;

import org.cogent.io.PrintWriterSink ;
import org.cogent.messages.MessageRegistry ;
import org.junit.jupiter.api.AfterEach ;
import org.junit.jupiter.api.Test ;

import static org.cogent.model.util.TDDTrickBag.* ;
import static org.cogent.validation.TemplateType.* ;
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

	private MessageRegistry reg = new MessageRegistry ( ) {{
		this.register ( FOO, CONTEXT, "Ababba", 0 ) ;
		this.register ( FOO, FAILURE, "Zzyzzx", 0 ) ;
	}} ;

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

	@Test
	public void testContainedException ( ) throws Exception {
		ValidationContext ctx = ValidationContext.of ( reg ) ;
		ctx.pushContext ( FOO, new IllegalArgumentException ( "Fallacies!" ) ) ;
		ctx.failCurrentContext ( ) ;
		try {
			ctx.airGrievances ( ) ;
		} catch ( ValidationException t ) {
			PrintWriterSink sink = new PrintWriterSink ( ) ;
			t.printStackTrace ( sink ) ;
			String actual = String.valueOf ( sink ) ;
			assertNotNull ( actual ) ;
			assertContains ( actual, "CONTEXT" ) ;
			assertContains ( actual, "TRACE" ) ;
			assertContains ( actual, "FOO" ) ;
			assertContains ( actual, "Fallacies!" ) ;
			assertEquals ( FOO, t.getCode ( ) ) ;
		}
	}

	@Test
	public void testNestedException ( ) throws Exception {
		ValidationContext ctx = ValidationContext.of ( reg ) ;
		ctx.pushContext ( FOO ) ;
		ctx.failCurrentContext ( new IllegalArgumentException ( "Fallacies!" ) ) ;
		try {
			ctx.airGrievances ( ) ;
		} catch ( ValidationException t ) {
			PrintWriterSink sink = new PrintWriterSink ( ) ;
			t.printStackTrace ( sink ) ;
			String actual = String.valueOf ( sink ) ;
			assertNotNull ( actual ) ;
			assertContains ( actual, "CONTEXT" ) ;
			assertContains ( actual, "TRACE" ) ;
			assertContains ( actual, "FOO" ) ;
			assertNotContains ( actual, "Fallacies!" ) ;
			assertEquals ( FOO, t.getCode ( ) ) ;
			assertNotNull ( t.getCause ( ) ) ;
			Throwable cause = t.getCause ( ) ;
			assertTrue ( cause instanceof IllegalArgumentException ) ;
			assertContains ( cause.getMessage ( ), "Fallacies!" ) ;
		}
	}
}
