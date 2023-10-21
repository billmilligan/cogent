package org.cogent.messages;


import org.junit.jupiter.api.AfterEach ;
import org.junit.jupiter.api.Test ;

import static org.cogent.messages.MessageRegistryTest.MessageRole.* ;
import static org.cogent.messages.MessageRegistryTest.RandomCode.* ;
import static org.junit.jupiter.api.Assertions.* ;

import java.text.MessageFormat ;
import java.time.LocalDate ;

public class MessageRegistryTest {

	// Don't use singleton, so as not to
	// corrupt other tests
	private MessageRegistry reg = new MessageRegistry ( ) { ; } ;

	static enum RandomCode implements Code {
		FOO, BAR, BAZ, QUUX
	}

	static enum MessageRole implements MessageTemplateType {
		FORWARD, REVERSE, NEUTRAL
	}

	@AfterEach
	public void clear ( ) {
		reg.clear ( ) ;
	}

	@Test
	public void testSimple ( ) throws Exception {
		reg.register ( FOO, FORWARD, "This is a param {0} okay?", 1 ) ;
		String actual = reg.format ( FOO, FORWARD, "ARUGULA" ) ;
		String expected = "This is a param \"ARUGULA\" okay?" ;
		assertEquals ( expected, actual, "Correct output for simple registration with one String param" ) ;
	}

	@Test
	public void testWrongParamCountForRegistration ( ) throws Exception {
		reg.register ( FOO, FORWARD, "This is a param {0} okay?", 2 ) ;
		try {
			reg.format ( FOO, FORWARD, "ARUGULA" ) ;
			fail ( "Failed to throw correct exception" ) ;
		} catch ( IllegalArgumentException iae ) {
			; // GOOD!
		}
	}

	@Test
	public void testTooManyParamCountForTemplate ( ) throws Exception {
		reg.register ( FOO, FORWARD, "This is a param {0} okay?", 2 ) ;
		String actual = reg.format ( FOO, FORWARD, "ARUGULA", "ESCAROLE" ) ;
		String expected = "This is a param \"ARUGULA\" okay?" ;
		assertEquals ( expected, actual, "In spite of not enough params, the format works okay (but the extra words are cut off)" ) ;
	}

	@Test
	public void testTooFewParamCountForTemplate ( ) throws Exception {
		reg.register ( FOO, FORWARD, "This is a param {0} okay {1}?", 1 ) ;
		String actual = reg.format ( FOO, FORWARD, "ARUGULA" ) ;
		String expected = "This is a param \"ARUGULA\" okay {1}?" ;
		assertEquals ( expected, actual, "In spite of too many params, the format works okay (but the params aren't filled in)" ) ;
	}

	@Test
	public void testMessageTemplateToStringDoesNotSuck ( ) throws Exception {
		String template = "This is a param {0} okay {1}?" ;
		reg.register ( FOO, FORWARD, template, 1 ) ;
		MessageFormat actual = reg.formatFor ( FOO, FORWARD ) ;
		assertEquals ( template, actual.toString ( ), "By default MessageFormat has a really crappy toString() but I fix it here." ) ;

		actual = reg.formatFor ( BAR, FORWARD ) ;
		assertNull ( actual, "templateFor doesn't die if it can't find something" ) ;
	}

	@Test
	public void testMultiRegisterAndFormatWithoutConflict ( ) throws Exception {
		reg.register ( FOO, FORWARD, "This is a forward message {0}.", 1 ) ;
		reg.register ( FOO, REVERSE, "{0}, This is a reverse message.", 1 ) ;
		reg.register ( FOO, NEUTRAL, "{0}, This is a neutral message {1}.", 2 ) ;
		reg.register ( BAR, FORWARD, "This is a barward message {0}.", 1 ) ;
		reg.register ( BAR, REVERSE, "{0}, This is a reverse barward message.", 1 ) ;

		String actual = reg.format ( FOO, FORWARD, "ARUGULA" ) ;
		String expected = "This is a forward message \"ARUGULA\"." ;
		assertEquals ( expected, actual, "Correct message" ) ;

		actual = reg.format ( FOO, REVERSE, "ARUGULA" ) ;
		expected = "\"ARUGULA\", This is a reverse message." ;
		assertEquals ( expected, actual, "Correct message" ) ;

		actual = reg.format ( FOO, NEUTRAL, "ARUGULA", "ESCAROLE" ) ;
		expected = "\"ARUGULA\", This is a neutral message \"ESCAROLE\"." ;
		assertEquals ( expected, actual, "Correct message" ) ;

		actual = reg.format ( BAR, FORWARD, "ARUGULA" ) ;
		expected = "This is a barward message \"ARUGULA\"." ;
		assertEquals ( expected, actual, "Correct message" ) ;

		actual = reg.format ( BAR, REVERSE, "ARUGULA" ) ;
		expected = "\"ARUGULA\", This is a reverse barward message." ;
		assertEquals ( expected, actual, "Correct message" ) ;
	}

	@Test
	public void testFormatWithoutRegistration ( ) throws Exception {
		String actual = reg.format ( FOO, FORWARD, "ARUGULA", "ESCAROLE" ) ;
		String expected = "FOO:FORWARD:  \"ARUGULA\", \"ESCAROLE\"" ;
		assertEquals ( expected, actual, "Correct message in spite of no registration" ) ;
	}

	@Test
	public void testFormatWithoutArgs ( ) throws Exception {
		reg.register ( FOO, FORWARD, "This is without params, okay?", 0 ) ;
		String actual = reg.format ( FOO, FORWARD ) ;
		String expected = "This is without params, okay?" ;
		assertEquals ( expected, actual, "Correct output for simple registration with zero String params" ) ;
	}

	@Test
	public void testFormatWithNullArg ( ) throws Exception {
		reg.register ( FOO, FORWARD, "This is a param {0} okay?", 1 ) ;
		String actual = reg.format ( FOO, FORWARD, ( Object ) null ) ;
		String expected = "This is a param null okay?" ;
		assertEquals ( expected, actual, "Correct output for simple registration with one null param" ) ;
	}

	@Test
	public void testFormatWithNonStringArg ( ) throws Exception {
		LocalDate ld = LocalDate.of ( 2015, 10, 21 ) ; // Back to the Future Day!
		reg.register ( FOO, FORWARD, "This is a param {0} okay?", 1 ) ;
		String actual = reg.format ( FOO, FORWARD, ld ) ;
		String expected = "This is a param \"2015-10-21\" okay?" ;
		assertEquals ( expected, actual, "Correct output for simple registration with one String param" ) ;
	}
}
