package org.cogent.validation;


import org.junit.jupiter.api.Test ;
import org.cogent.io.PrintWriterSink ;
import org.cogent.messages.MessageRegistry ;
import org.junit.jupiter.api.AfterEach ;

import static org.cogent.model.util.TDDTrickBag.* ;
import static org.cogent.validation.MultiExceptionTest.TestKey.* ;
import static org.cogent.validation.TemplateType.* ;
import static org.junit.jupiter.api.Assertions.* ;

import java.io.ByteArrayOutputStream ;
import java.io.PrintStream ;
import java.util.Collections ;
import java.util.List ;
import java.util.regex.Matcher ;
import java.util.regex.Pattern ;

public class MultiExceptionTest {

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
		this.register ( QUUX, CONTEXT, "This is the quux context {0}, {1}.", 2 ) ;
		this.register ( QUUX, FAILURE, "This is the quux failure {0}, {1}.", 2 ) ;
	}} ;

	@AfterEach
	public void reset ( ) {
		reg.clear ( ) ;
	}

	@Test
	public void testTrace ( ) throws Exception {
		ValidationContext ctx = assembleRicketyValidationContext ( ) ;
		MultiException me = null ;
		try {
			ctx.airGrievances ( ) ;
		} catch ( MultiException x ) {
			me = x ;
		}
		assertNotNull ( me ) ;

		PrintWriterSink sink = new PrintWriterSink ( ) ;
		me.printStackTrace ( sink ) ;
		String actual = sink.getContents ( ) ;
		validateActualException ( me, actual ) ;
	}

	private void validateActualException ( MultiException me, String actual ) {
		assertNotNull ( actual ) ;
		assertContains ( actual, "= MULTIPLE EXCEPTIONS =" ) ;
		assertContains ( actual, "Exception Count: 7" ) ;
		Pattern p = Pattern.compile ( "EXCEPTION (\\d)" ) ;
		Matcher m = p.matcher ( actual ) ;
		int exceptionsFound = 0 ;
		while ( m.find ( ) ) {
			exceptionsFound++ ;
		}
		assertEquals ( 7, exceptionsFound, "Right number!" ) ;
		assertEquals ( actual.trim ( ), me.toString ( ).trim ( ), "toString() is nice and convenient here" ) ;
	}

	private ValidationContext assembleRicketyValidationContext ( ) {
		ValidationContext ctx = ValidationContext.of ( reg ) ;
		ctx.pushContext ( FOO, "P0", "P1" ) ;
		ctx.failCurrentContext ( "P2" ) ;
		ctx.pushContext ( BAR, "P3", "P4" ) ;
		ctx.failCurrentContext ( "P5" ) ;
		ctx.pushContext ( BAZ, "P6", "P7" ) ;
		ctx.failCurrentContext ( "P8" ) ;
		ctx.pushContext ( QUUX, "P9", "PA" ) ;
		ctx.failCurrentContext ( new IllegalStateException ( "Your own private Idaho" ) ) ;
		ctx.popContext ( ) ;
		ctx.failCurrentContext ( "PB" ) ;
		ctx.popContext ( ) ;
		ctx.failCurrentContext ( "PC" ) ;
		ctx.popContext ( ) ;
		ctx.failCurrentContext ( "PD" ) ;
		ctx.popContext ( ) ;
		return ctx ;
	}

	@Test
	public void testTraceAtErr ( ) throws Exception {
		ValidationContext ctx = assembleRicketyValidationContext ( ) ;
		ByteArrayOutputStream bs = new ByteArrayOutputStream ( ) ;
		PrintStream oldErr = System.err ;
		PrintStream newErr = new PrintStream ( bs ) ;
		System.setErr ( newErr ) ;
		try {
			MultiException me = null ;
			try {
				ctx.airGrievances ( ) ;
			} catch ( MultiException x ) {
				me = x ;
			}
			me.printStackTrace ( ) ;
			String actual = new String ( bs.toByteArray ( ) ) ;
			validateActualException ( me, actual ) ;
		} finally {
			System.setErr ( oldErr ) ;
		}
	}

	@Test
	public void testEmptiesWithNull ( ) throws Exception {
		MultiException me = new MultiException ( null ) ;
		PrintWriterSink sink = new PrintWriterSink ( ) ;
		me.printStackTrace ( sink ) ;
		String actual = sink.getContents ( ) ;
		assertEquals ( "No bundled exceptions", actual.trim ( ) ) ;
	}

	@Test
	public void testEmptiesWithEmpty ( ) throws Exception {
		MultiException me = new MultiException ( Collections.emptyList ( ) ) ;
		PrintWriterSink sink = new PrintWriterSink ( ) ;
		me.printStackTrace ( sink ) ;
		String actual = sink.getContents ( ) ;
		assertEquals ( "No bundled exceptions", actual.trim ( ) ) ;
	}

	@Test
	public void testSolitary ( ) throws Exception {
		Exception e = new IllegalStateException ( "Home of Florida Man" ) ;
		try {
			throw e ;
		} catch ( Exception x ) {
			e = x ;
		}
		MultiException me = new MultiException ( List.of ( e ) ) ;
		PrintWriterSink sink = new PrintWriterSink ( ) ;
		me.printStackTrace ( sink ) ;
		String actual = sink.getContents ( ) ;
		assertContains ( actual, "IllegalStateException" ) ;
		assertContains ( actual, "Florida Man" ) ;
		assertContains ( actual, "\tat " ) ;
		assertNotContains ( actual, "MultipleException" ) ;
	}
}
