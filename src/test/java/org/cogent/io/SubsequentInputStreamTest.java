package org.cogent.io;

import java.io.ByteArrayInputStream ;
import java.io.InputStream ;
import java.io.PrintWriter ;
import java.io.UncheckedIOException ;

import org.cogent.model.Writeable ;
import org.junit.jupiter.api.Test ;

import static org.junit.jupiter.api.Assertions.* ;

public class SubsequentInputStreamTest {

	@Test
	public void testBasics ( ) throws Exception {
		try ( SubsequentInputStream sis = new SubsequentInputStream ( ) ) {
			sis.add ( "SimpleString;" ) ;
			sis.add ( new ByteArrayInputStream ( "InputStreamBytes;".getBytes ( ) ) ) ;
			InputStreamable is = new InputStreamable ( ) ;
			sis.add ( is, this::open ) ;
			sis.add ( new TestWriteable ( ), new WriteContext ( ) );
			sis.addln ( "AndInTheEnd" ) ;
			String actual = new String ( sis.readAllBytes ( ) ) ;
			String expected = "SimpleString;InputStreamBytes;InputStreamable;\tWriteable ;AndInTheEnd" + System.getProperty ( "line.separator" ) ;
			assertEquals ( expected, actual, "All input means worked!" ) ;
		}
	}

	private InputStream open ( InputStreamable in ) {
		return in.open ( ) ;
	}

	private static final class InputStreamable {
		public InputStream open ( ) {
			return new ByteArrayInputStream ( "InputStreamable;".getBytes ( ) ) ;
		}
	}

	private static final class TestWriteable implements Writeable {
		@Override
		public void write ( PrintWriter pw, WriteContext wc ) throws UncheckedIOException {
			wc.inc ( ) ;
			pw.print ( wc.getIndent ( ) + "Writeable" + wc.endStatement ( ) ) ;
		}		
	}
}
