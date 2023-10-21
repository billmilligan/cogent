package org.cogent.validation;

import org.junit.jupiter.api.Test ;

import static org.cogent.model.util.TDDTrickBag.* ;
import static org.cogent.validation.TemplateType.* ;
import static org.cogent.validation.ValidationContextTest.TestKey.* ;
import static org.junit.jupiter.api.Assertions.* ;

import org.cogent.io.PrintWriterSink ;
import org.cogent.messages.MessageRegistry ;

public class ValidationContextTest {

	public static enum TestKey implements ValidationCode {
		FOO, BAR, BAZ, QUUX
	}

	private MessageRegistry reg = new MessageRegistry ( ) {{
		this.register ( FOO, CONTEXT, "This is the foo context {0}, {1}.", 2 ) ;
		this.register ( FOO, FAILURE, "This is the foo failure {0}, {1}, {2}.", 3 ) ;
		this.register ( BAR, CONTEXT, "This is the bar context {0}, {1}.", 2 ) ;
		this.register ( BAR, FAILURE, "This is the bar failure {0}, {1}, {2}.", 3 ) ;
		this.register ( BAZ, CONTEXT, "This is the baz context {0}, {1}.", 2 ) ;
		this.register ( BAZ, FAILURE, "This is the baz failure {0}, {1}, {2}.", 3 ) ;
	}} ;

	@Test
	public void testBasics ( ) throws Exception {
		ValidationContext ctx = ValidationContext.of ( reg ) ;
		ctx.pushContext ( FOO, "P0", "P1" ) ;
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
			assertContains ( actual, "FOO" ) ;
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
			assertContains ( actual, "BAZ" ) ;
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
}
