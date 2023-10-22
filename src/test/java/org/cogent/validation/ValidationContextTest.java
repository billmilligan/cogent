package org.cogent.validation ;

import org.junit.jupiter.api.Test ;

import java.util.EmptyStackException ;
import java.util.List ;

import org.cogent.io.PrintWriterSink ;
import org.cogent.messages.MessageRegistry ;
import org.cogent.validation.ValidationContext.Tray ;

import static java.util.Arrays.* ;
import static org.cogent.util.TDDTrickBag.* ;
import static org.cogent.validation.TemplateType.* ;
import static org.cogent.validation.ValidationContext.NamingValidationCode.* ;
import static org.cogent.validation.ValidationContextTest.TestKey.* ;
import static org.junit.jupiter.api.Assertions.* ;

public class ValidationContextTest {

	public static enum TestKey implements ValidationCode {
		FOO, BAR, BAZ, QUUX
	}

	private MessageRegistry reg = new MessageRegistry ( ) {

		{
			this.register ( FOO, CONTEXT, "This is the foo context {0}, {1}.", 2 ) ;
			this.register ( FOO, FAILURE, "This is the foo failure {0}, {1}, {2}.", 3 ) ;
			this.register ( BAR, CONTEXT, "This is the bar context {0}, {1}.", 2 ) ;
			this.register ( BAR, FAILURE, "This is the bar failure {0}, {1}, {2}.", 3 ) ;
			this.register ( BAZ, CONTEXT, "This is the baz context {0}, {1}.", 2 ) ;
			this.register ( BAZ, FAILURE, "This is the baz failure {0}, {1}, {2}.", 3 ) ;
			this.register ( QUUX, CONTEXT, "This is the quux context {0}, {1}.", 2 ) ;
			this.register ( QUUX, FAILURE, "This is the quux failure {0}, {1}.", 2 ) ;
		}
	} ;

	// also tests failCurrentContext
	@Test
	public void testBasics ( ) throws Exception {
		ValidationContext ctx = ValidationContext.of ( reg ) ;
		ctx.pushContext ( FOO, "P0", "P1" ) ;
		ctx.airGrievances ( ) ;
		// Still here!
		ctx.failCurrentContext ( "P2" ) ;
		try {
			ctx.airGrievances ( ) ;
		} catch ( ValidationException ve ) {
			// make sure we're catching the correct type
			PrintWriterSink sink = new PrintWriterSink ( ) ;
			ve.printStackTrace ( sink ) ;
			String actual = sink.getContents ( ) ;
			assertContains ( actual, "CONTEXT" ) ;
			assertContains ( actual, "TRACE" ) ;
			assertContains ( actual, "( FOO )" ) ;
			int contextLoc = actual.indexOf ( "CONTEXT" ) ;
			int dividerLoc = actual.indexOf ( "TRACE" ) ;
			assertContains ( actual, "This is the foo context \"P0\", \"P1\"." ) ;
			assertContains ( actual, "This is the foo failure \"P0\", \"P1\", \"P2\"." ) ;
			int contextContent = actual.indexOf ( "foo context" ) ;
			int traceLoc = actual.indexOf ( "foo failure" ) ;
			assertTrue ( contextContent > contextLoc, "Context line appears after word CONTEXT" ) ;
			assertTrue ( contextContent < dividerLoc, "Context line appears before word TRACE" ) ;
			assertTrue ( traceLoc > dividerLoc, "Failure line appears after word TRACE" ) ;
		}
	}

	// also tests fail
	@Test
	public void testDouble ( ) throws Exception {
		ValidationContext ctx = ValidationContext.of ( reg ) ;
		ctx.pushContext ( FOO, "P0", "P1" ) ;
		ctx.pushContext ( BAR, "P3", "P4" ) ;
		ctx.fail ( BAZ, "P5", "P6", "P7" ) ;
		try {
			ctx.airGrievances ( ) ;
		} catch ( ValidationException ve ) {
			// make sure we're catching the correct type
			PrintWriterSink sink = new PrintWriterSink ( ) ;
			ve.printStackTrace ( sink ) ;
			String actual = sink.getContents ( ) ;
			assertContains ( actual, "CONTEXT" ) ;
			assertContains ( actual, "TRACE" ) ;
			assertContains ( actual, "( BAZ )" ) ;
			int contextLoc = actual.indexOf ( "CONTEXT" ) ;
			int dividerLoc = actual.indexOf ( "TRACE" ) ;
			assertContains ( actual, "This is the foo context \"P0\", \"P1\"." ) ;
			assertContains ( actual, "This is the bar context \"P3\", \"P4\"." ) ;
			assertContains ( actual, "This is the baz context \"P5\", \"P6\"." ) ;
			assertContains ( actual, "This is the baz failure \"P5\", \"P6\", \"P7\"." ) ;
			int fooContent = actual.indexOf ( "foo context" ) ;
			int fooFail = actual.indexOf ( "foo failure" ) ;
			int barContent = actual.indexOf ( "bar context" ) ;
			int barFail = actual.indexOf ( "bar failure" ) ;
			int bazContent = actual.indexOf ( "baz context" ) ;
			int bazFail = actual.indexOf ( "baz failure" ) ;
			assertTrue ( bazContent > contextLoc, "Context line appears after word CONTEXT" ) ;
			assertTrue ( bazContent < dividerLoc, "Context line appears before word TRACE" ) ;
			assertTrue ( bazFail > dividerLoc, "Failure line appears after word TRACE" ) ;
			assertTrue ( fooContent < barContent, "Foo comes before Bar" ) ;
			assertTrue ( barContent < bazContent, "Foo comes before Bar" ) ;
			assertEquals ( -1, fooFail, "No Foo Failure to Fight" ) ;
			assertEquals ( -1, barFail, "No Bar Failure to Fight" ) ;
		}
	}

	@Test
	public void testPop ( ) throws Exception {
		ValidationContext ctx = ValidationContext.of ( reg ) ;
		ctx.pushContext ( FOO, "P0", "P1" ) ;
		ctx.pushContext ( BAR, "P3", "P4" ) ;
		ctx.pushContext ( BAZ, "P5", "P6" ) ;
		ctx.popContext ( ) ;
		ctx.fail ( QUUX, "P7", "P8" ) ;
		try {
			ctx.airGrievances ( ) ;
		} catch ( ValidationException ve ) {
			// make sure we're catching the correct type
			PrintWriterSink sink = new PrintWriterSink ( ) ;
			ve.printStackTrace ( sink ) ;
			String actual = sink.getContents ( ) ;
			assertContains ( actual, "CONTEXT" ) ;
			assertContains ( actual, "TRACE" ) ;
			assertContains ( actual, "( QUUX )" ) ;
			int contextLoc = actual.indexOf ( "CONTEXT" ) ;
			int dividerLoc = actual.indexOf ( "TRACE" ) ;
			assertContains ( actual, "This is the foo context \"P0\", \"P1\"." ) ;
			assertContains ( actual, "This is the bar context \"P3\", \"P4\"." ) ;
			assertContains ( actual, "This is the quux context \"P7\", \"P8\"." ) ;
			assertContains ( actual, "This is the quux failure \"P7\", \"P8\"." ) ;
			assertNotContains ( actual, ( "baz" ) ) ;
			int fooContent = actual.indexOf ( "foo context" ) ;
			int fooFail = actual.indexOf ( "foo failure" ) ;
			int barContent = actual.indexOf ( "bar context" ) ;
			int barFail = actual.indexOf ( "bar failure" ) ;
			int quuxContent = actual.indexOf ( "quux context" ) ;
			int quuxFail = actual.indexOf ( "quux failure" ) ;
			assertTrue ( quuxContent > contextLoc, "Context line appears after word CONTEXT" ) ;
			assertTrue ( quuxContent < dividerLoc, "Context line appears before word TRACE" ) ;
			assertTrue ( quuxFail > dividerLoc, "Failure line appears after word TRACE" ) ;
			assertTrue ( fooContent < barContent, "Foo comes before Bar" ) ;
			assertTrue ( barContent < quuxContent, "Foo comes before Bar" ) ;
			assertEquals ( -1, fooFail, "No Foo Failure to Fight" ) ;
			assertEquals ( -1, barFail, "No Bar Failure to Fight" ) ;
			Object [ ] params = ve.getParams ( ) ;
			assertEquals ( 2, params.length ) ;
			assertEquals ( "P7", params [ 0 ] ) ;
			assertEquals ( "P8", params [ 1 ] ) ;
		}
	}

	@Test
	public void testMultiFail ( ) throws Exception {
		ValidationContext ctx = ValidationContext.of ( reg ) ;
		assertFalse ( ctx.canPop ( ) ) ;
		ctx.pushContext ( FOO, "P0", "P1" ) ;
		assertTrue ( ctx.canPop ( ) ) ;
		ctx.failCurrentContext ( "P2" ) ;
		ctx.pushContext ( BAR, "P3", "P4" ) ;
		assertTrue ( ctx.canPop ( ) ) ;
		ctx.failCurrentContext ( "P5" ) ;
		ctx.pushContext ( BAZ, "P6", "P7" ) ;
		assertTrue ( ctx.canPop ( ) ) ;
		ctx.failCurrentContext ( "P8" ) ;
		ctx.pushContext ( QUUX, "P9", "PA" ) ;
		assertTrue ( ctx.canPop ( ) ) ;
		ctx.failCurrentContext ( new IllegalStateException ( "Your own private Idaho" ) ) ;
		ctx.popContext ( ) ;
		assertTrue ( ctx.canPop ( ) ) ;
		ctx.failCurrentContext ( "PB" ) ;
		ctx.popContext ( ) ;
		assertTrue ( ctx.canPop ( ) ) ;
		ctx.failCurrentContext ( "PC" ) ;
		ctx.popContext ( ) ;
		assertTrue ( ctx.canPop ( ) ) ;
		ctx.failCurrentContext ( "PD" ) ;
		ctx.popContext ( ) ;
		assertFalse ( ctx.canPop ( ) ) ;
		try {
			ctx.popContext ( ) ;
			fail ( "Should have popped beyond the point where we could have stopped" ) ;
		} catch ( EmptyStackException ese ) {
			; // Quoite awl roite
		}
		MultiException me = null ;
		try {
			ctx.airGrievances ( ) ;
		} catch ( MultiException x ) {
			me = x ;
		}
		assertNotNull ( me, "Caught the right exception" ) ;
		List <? extends Exception> contents = me.getContents ( ) ;
		assertEquals ( 7, contents.size ( ), "Correct number of contained exceptions" ) ;

		assertTrue ( contents.get ( 0 ) instanceof ValidationException, "Correct internal type" ) ;
		ValidationException ve = ( ValidationException ) contents.get ( 0 ) ;
		assertEquals ( FOO, ve.getCode ( ), "Correct code received at this level" ) ;
		assertEquals ( asList ( "P0", "P1", "P2" ), asList ( ve.getParams ( ) ) ) ;
		assertNull ( ve.getCause ( ), "No internal cause" ) ;
		List <Tray> context = ve.getContext ( ) ;
		assertEquals ( 1, context.size ( ) ) ;
		Tray t = context.get ( 0 ) ;
		assertEquals ( FOO, t.code ( ) ) ;
		assertEquals ( asList ( "P0", "P1" ), asList ( t.message ( ) ) ) ;
		assertNull ( t.throwable ( ) ) ;

		assertTrue ( contents.get ( 1 ) instanceof ValidationException, "Correct internal type" ) ;
		ve = ( ValidationException ) contents.get ( 1 ) ;
		assertEquals ( BAR, ve.getCode ( ), "Correct code received at this level" ) ;
		assertEquals ( asList ( "P3", "P4", "P5" ), asList ( ve.getParams ( ) ) ) ;
		assertNull ( ve.getCause ( ), "No internal cause" ) ;
		context = ve.getContext ( ) ;
		assertEquals ( 2, context.size ( ) ) ;
		t = context.get ( 0 ) ;
		assertEquals ( FOO, t.code ( ) ) ;
		assertEquals ( asList ( "P0", "P1" ), asList ( t.message ( ) ) ) ;
		assertNull ( t.throwable ( ) ) ;
		t = context.get ( 1 ) ;
		assertEquals ( BAR, t.code ( ) ) ;
		assertEquals ( asList ( "P3", "P4" ), asList ( t.message ( ) ) ) ;
		assertNull ( t.throwable ( ) ) ;

		assertTrue ( contents.get ( 2 ) instanceof ValidationException, "Correct internal type" ) ;
		ve = ( ValidationException ) contents.get ( 2 ) ;
		assertEquals ( BAZ, ve.getCode ( ), "Correct code received at this level" ) ;
		assertEquals ( asList ( "P6", "P7", "P8" ), asList ( ve.getParams ( ) ) ) ;
		assertNull ( ve.getCause ( ), "No internal cause" ) ;
		context = ve.getContext ( ) ;
		assertEquals ( 3, context.size ( ) ) ;
		t = context.get ( 0 ) ;
		assertEquals ( FOO, t.code ( ) ) ;
		assertEquals ( asList ( "P0", "P1" ), asList ( t.message ( ) ) ) ;
		assertNull ( t.throwable ( ) ) ;
		t = context.get ( 1 ) ;
		assertEquals ( BAR, t.code ( ) ) ;
		assertEquals ( asList ( "P3", "P4" ), asList ( t.message ( ) ) ) ;
		assertNull ( t.throwable ( ) ) ;
		t = context.get ( 2 ) ;
		assertEquals ( BAZ, t.code ( ) ) ;
		assertEquals ( asList ( "P6", "P7" ), asList ( t.message ( ) ) ) ;
		assertNull ( t.throwable ( ) ) ;

		assertTrue ( contents.get ( 3 ) instanceof ValidationException, "Correct internal type" ) ;
		ve = ( ValidationException ) contents.get ( 3 ) ;
		assertEquals ( QUUX, ve.getCode ( ), "Correct code received at this level" ) ;
		assertEquals ( asList ( "P9", "PA" ), asList ( ve.getParams ( ) ) ) ;
		assertNotNull ( ve.getCause ( ), "Internal cause!" ) ;
		Throwable cause = ve.getCause ( ) ;
		assertTrue ( cause instanceof IllegalStateException, "Right internal cause" ) ;
		assertContains ( cause.getMessage ( ), "Idaho" ) ;
		context = ve.getContext ( ) ;
		assertEquals ( 4, context.size ( ) ) ;
		t = context.get ( 0 ) ;
		assertEquals ( FOO, t.code ( ) ) ;
		assertEquals ( asList ( "P0", "P1" ), asList ( t.message ( ) ) ) ;
		assertNull ( t.throwable ( ) ) ;
		t = context.get ( 1 ) ;
		assertEquals ( BAR, t.code ( ) ) ;
		assertEquals ( asList ( "P3", "P4" ), asList ( t.message ( ) ) ) ;
		assertNull ( t.throwable ( ) ) ;
		t = context.get ( 2 ) ;
		assertEquals ( BAZ, t.code ( ) ) ;
		assertEquals ( asList ( "P6", "P7" ), asList ( t.message ( ) ) ) ;
		assertNull ( t.throwable ( ) ) ;
		t = context.get ( 3 ) ;
		assertEquals ( QUUX, t.code ( ) ) ;
		assertEquals ( asList ( "P9", "PA" ), asList ( t.message ( ) ) ) ;
		assertNull ( t.throwable ( ) ) ;

		assertTrue ( contents.get ( 4 ) instanceof ValidationException, "Correct internal type" ) ;
		ve = ( ValidationException ) contents.get ( 4 ) ;
		assertEquals ( BAZ, ve.getCode ( ), "Correct code received at this level" ) ;
		assertEquals ( asList ( "P6", "P7", "PB" ), asList ( ve.getParams ( ) ) ) ;
		assertNull ( ve.getCause ( ), "No internal cause" ) ;
		context = ve.getContext ( ) ;
		assertEquals ( 3, context.size ( ) ) ;
		t = context.get ( 0 ) ;
		assertEquals ( FOO, t.code ( ) ) ;
		assertEquals ( asList ( "P0", "P1" ), asList ( t.message ( ) ) ) ;
		assertNull ( t.throwable ( ) ) ;
		t = context.get ( 1 ) ;
		assertEquals ( BAR, t.code ( ) ) ;
		assertEquals ( asList ( "P3", "P4" ), asList ( t.message ( ) ) ) ;
		assertNull ( t.throwable ( ) ) ;
		t = context.get ( 2 ) ;
		assertEquals ( BAZ, t.code ( ) ) ;
		assertEquals ( asList ( "P6", "P7" ), asList ( t.message ( ) ) ) ;
		assertNull ( t.throwable ( ) ) ;

		assertTrue ( contents.get ( 5 ) instanceof ValidationException, "Correct internal type" ) ;
		ve = ( ValidationException ) contents.get ( 5 ) ;
		assertEquals ( BAR, ve.getCode ( ), "Correct code received at this level" ) ;
		assertEquals ( asList ( "P3", "P4", "PC" ), asList ( ve.getParams ( ) ) ) ;
		assertNull ( ve.getCause ( ), "No internal cause" ) ;
		context = ve.getContext ( ) ;
		assertEquals ( 2, context.size ( ) ) ;
		t = context.get ( 0 ) ;
		assertEquals ( FOO, t.code ( ) ) ;
		assertEquals ( asList ( "P0", "P1" ), asList ( t.message ( ) ) ) ;
		assertNull ( t.throwable ( ) ) ;
		t = context.get ( 1 ) ;
		assertEquals ( BAR, t.code ( ) ) ;
		assertEquals ( asList ( "P3", "P4" ), asList ( t.message ( ) ) ) ;
		assertNull ( t.throwable ( ) ) ;

		assertTrue ( contents.get ( 6 ) instanceof ValidationException, "Correct internal type" ) ;
		ve = ( ValidationException ) contents.get ( 6 ) ;
		assertEquals ( FOO, ve.getCode ( ), "Correct code received at this level" ) ;
		assertEquals ( asList ( "P0", "P1", "PD" ), asList ( ve.getParams ( ) ) ) ;
		assertNull ( ve.getCause ( ), "No internal cause" ) ;
		context = ve.getContext ( ) ;
		assertEquals ( 1, context.size ( ) ) ;
		t = context.get ( 0 ) ;
		assertEquals ( FOO, t.code ( ) ) ;
		assertEquals ( asList ( "P0", "P1" ), asList ( t.message ( ) ) ) ;
		assertNull ( t.throwable ( ) ) ;
	}

	@Test
	public void validateJavaIdentifiers ( ) throws Exception {
		ValidationContext ctx = ValidationContext.of ( reg ) ;
		ctx.validateJavaIdentifier ( "NothingWrongWithThis" ) ;
		ctx.airGrievances ( ) ;
		ctx.validateJavaIdentifier ( "orThis" ) ;
		ctx.airGrievances ( ) ;
		assertFalse ( ctx.canPop ( ), "In spite of running two validations, we are now context-free" ) ;

		ctx.validateJavaIdentifier ( "" ) ;
		try {
			ctx.airGrievances ( ) ;
		} catch ( ValidationException ve ) {
			assertEquals ( BLANK_IDENTIFIER, ve.getCode ( ) ) ;
			assertEquals ( 0, ve.getParams ( ).length, "No params" ) ;
			assertNull ( ve.getCause ( ) ) ;
			List <Tray> context = ve.getContext ( ) ;
			assertEquals ( 2, context.size ( ) ) ;
			Tray t = context.get ( 0 ) ;
			assertEquals ( JAVA_IDENTIFIER, t.code ( ) ) ;
			assertEquals ( 1, t.message ( ).length, "One param" ) ;
			assertEquals ( "", t.message ( ) [ 0 ] ) ;
			assertNull ( t.throwable ( ) ) ;
			t = context.get ( 1 ) ;
			assertEquals ( BLANK_IDENTIFIER, t.code ( ) ) ;
			assertEquals ( 0, t.message ( ).length, "No params" ) ;
			assertNull ( t.throwable ( ) ) ;
		}

		ctx.validateJavaIdentifier ( null ) ;
		try {
			ctx.airGrievances ( ) ;
		} catch ( ValidationException ve ) {
			assertEquals ( BLANK_IDENTIFIER, ve.getCode ( ) ) ;
			assertEquals ( 0, ve.getParams ( ).length, "No params" ) ;
			assertNull ( ve.getCause ( ) ) ;
			List <Tray> context = ve.getContext ( ) ;
			assertEquals ( 2, context.size ( ) ) ;
			Tray t = context.get ( 0 ) ;
			assertEquals ( JAVA_IDENTIFIER, t.code ( ) ) ;
			assertEquals ( 1, t.message ( ).length, "One param" ) ;
			assertEquals ( null, t.message ( ) [ 0 ] ) ;
			assertNull ( t.throwable ( ) ) ;
			t = context.get ( 1 ) ;
			assertEquals ( BLANK_IDENTIFIER, t.code ( ) ) ;
			assertEquals ( 0, t.message ( ).length, "No params" ) ;
			assertNull ( t.throwable ( ) ) ;
		}

		ctx.validateJavaIdentifier ( "98765" ) ;
		try {
			ctx.airGrievances ( ) ;
		} catch ( ValidationException ve ) {
			assertEquals ( STARTING_CHARACTER, ve.getCode ( ) ) ;
			assertEquals ( 1, ve.getParams ( ).length ) ;
			assertEquals ( '9', ve.getParams ( ) [ 0 ] ) ;
			assertNull ( ve.getCause ( ) ) ;
			List <Tray> context = ve.getContext ( ) ;
			assertEquals ( 2, context.size ( ) ) ;
			Tray t = context.get ( 0 ) ;
			assertEquals ( JAVA_IDENTIFIER, t.code ( ) ) ;
			assertEquals ( 1, t.message ( ).length, "One param" ) ;
			assertEquals ( "98765", t.message ( ) [ 0 ] ) ;
			assertNull ( t.throwable ( ) ) ;
			t = context.get ( 1 ) ;
			assertEquals ( STARTING_CHARACTER, t.code ( ) ) ;
			assertEquals ( 1, t.message ( ).length ) ;
			assertEquals ( '9', t.message ( ) [ 0 ] ) ;
			assertNull ( t.throwable ( ) ) ;
		}

		ctx.validateJavaIdentifier ( "A987&65" ) ;
		try {
			ctx.airGrievances ( ) ;
		} catch ( ValidationException ve ) {
			assertEquals ( SUBSEQUENT_CHARACTER, ve.getCode ( ) ) ;
			assertEquals ( 2, ve.getParams ( ).length ) ;
			assertEquals ( 4, ve.getParams ( ) [ 0 ] ) ;
			assertEquals ( '&', ve.getParams ( ) [ 1 ] ) ;
			assertNull ( ve.getCause ( ) ) ;
			List <Tray> context = ve.getContext ( ) ;
			assertEquals ( 2, context.size ( ) ) ;
			Tray t = context.get ( 0 ) ;
			assertEquals ( JAVA_IDENTIFIER, t.code ( ) ) ;
			assertEquals ( 1, t.message ( ).length, "One param" ) ;
			assertEquals ( "A987&65", t.message ( ) [ 0 ] ) ;
			assertNull ( t.throwable ( ) ) ;
			t = context.get ( 1 ) ;
			assertEquals ( SUBSEQUENT_CHARACTER, t.code ( ) ) ;
			assertEquals ( 1, t.message ( ).length ) ;
			assertEquals ( 4, t.message ( ) [ 0 ] ) ;
			assertNull ( t.throwable ( ) ) ;
		}

		ctx.validateJavaIdentifier ( "A987&6^5" ) ;
		try {
			ctx.airGrievances ( ) ;
		} catch ( MultiException me ) {
			assertEquals ( 2, me.getContents ( ).size ( ), "Correct number of internal exceptions" ) ;
			assertNull ( me.getCause ( ) ) ;
			ValidationException ve = ( ValidationException ) me.getContents ( ).get ( 0 ) ;
			assertEquals ( SUBSEQUENT_CHARACTER, ve.getCode ( ) ) ;
			assertEquals ( 2, ve.getParams ( ).length ) ;
			assertEquals ( 4, ve.getParams ( ) [ 0 ] ) ;
			assertEquals ( '&', ve.getParams ( ) [ 1 ] ) ;
			assertNull ( ve.getCause ( ) ) ;
			List <Tray> context = ve.getContext ( ) ;
			assertEquals ( 2, context.size ( ) ) ;
			Tray t = context.get ( 0 ) ;
			assertEquals ( JAVA_IDENTIFIER, t.code ( ) ) ;
			assertEquals ( 1, t.message ( ).length, "One param" ) ;
			assertEquals ( "A987&6^5", t.message ( ) [ 0 ] ) ;
			assertNull ( t.throwable ( ) ) ;
			t = context.get ( 1 ) ;
			assertEquals ( SUBSEQUENT_CHARACTER, t.code ( ) ) ;
			assertEquals ( 1, t.message ( ).length ) ;
			assertEquals ( 4, t.message ( ) [ 0 ] ) ;
			assertNull ( t.throwable ( ) ) ;

			ve = ( ValidationException ) me.getContents ( ).get ( 1 ) ;
			assertEquals ( SUBSEQUENT_CHARACTER, ve.getCode ( ) ) ;
			assertEquals ( 2, ve.getParams ( ).length ) ;
			assertEquals ( 6, ve.getParams ( ) [ 0 ] ) ;
			assertEquals ( '^', ve.getParams ( ) [ 1 ] ) ;
			assertNull ( ve.getCause ( ) ) ;
			context = ve.getContext ( ) ;
			assertEquals ( 2, context.size ( ) ) ;
			t = context.get ( 0 ) ;
			assertEquals ( JAVA_IDENTIFIER, t.code ( ) ) ;
			assertEquals ( 1, t.message ( ).length, "One param" ) ;
			assertEquals ( "A987&6^5", t.message ( ) [ 0 ] ) ;
			assertNull ( t.throwable ( ) ) ;
			t = context.get ( 1 ) ;
			assertEquals ( SUBSEQUENT_CHARACTER, t.code ( ) ) ;
			assertEquals ( 1, t.message ( ).length ) ;
			assertEquals ( 6, t.message ( ) [ 0 ] ) ;
			assertNull ( t.throwable ( ) ) ;
		}
	}
}
