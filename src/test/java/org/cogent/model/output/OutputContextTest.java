package org.cogent.model.output;

import static org.junit.jupiter.api.Assertions.* ;

import java.util.LinkedHashMap ;
import java.util.List ;
import java.util.Map ;

import org.cogent.io.StringFactory ;
import org.junit.jupiter.api.Test ;

public class OutputContextTest {

	private static final String endln = System.getProperty ( "line.separator" ) ; // system-dependent

	@Test
	public void testIndentationLevelsTabs ( ) {
		StyleConfig cfg = StyleConfig.builder ( )
				.indentation ( StyleConfig.IndentationConfig.builder ( )
						.type ( StyleConfig.IndentationType.Tabs )
						.tabSize ( 0 )
						.build ( ) )
				.build ( ) ;
		OutputContext ctx = new OutputContext ( cfg ) ;
		String expected = "\"Hello\"" + endln ;
		String actual = ctx.render ( List.of ( new StringLiteral ( "Hello" ) ) ).toString ( ) ;
		assertEquals ( expected, actual ) ;

		ctx.indent ( ) ;
		expected = "\t\"Hello\"" + endln ;
		actual = ctx.render ( List.of ( new StringLiteral ( "Hello" ) ) ).toString ( ) ;
		assertEquals ( expected, actual ) ;

		ctx.indent ( ) ;
		expected = "\t\t\"Hello\"" + endln ;
		actual = ctx.render ( List.of ( new StringLiteral ( "Hello" ) ) ).toString ( ) ;
		assertEquals ( expected, actual ) ;

		ctx.unindent ( ) ;
		expected = "\t\"Hello\"" + endln ;
		actual = ctx.render ( List.of ( new StringLiteral ( "Hello" ) ) ).toString ( ) ;
		assertEquals ( expected, actual ) ;

		ctx.unindent ( ) ;
		expected = "\"Hello\"" + endln ;
		actual = ctx.render ( List.of ( new StringLiteral ( "Hello" ) ) ).toString ( ) ;
		assertEquals ( expected, actual ) ;

		try {
			ctx.unindent ( ) ;
			fail ( "Failed to fail on unindenting too far" ) ;
		} catch ( IllegalStateException ise ) {
			;
		}
	}

	@Test
	public void testIndentationLevelsSpaces ( ) throws Exception {
		List <String> zeroIndents =
				List.of (	"\"Hello\"" + endln,
						"\"Hello\"" + endln,
						"\"Hello\"" + endln,
						"\"Hello\"" + endln,
						"\"Hello\"" + endln,
						"\"Hello\"" + endln,
						"\"Hello\"" + endln,
						"\"Hello\"" + endln,
						"\"Hello\"" + endln,
						"\"Hello\"" + endln,
						"\"Hello\"" + endln ) ;
		List <String> oneIndents =
				List.of ( "\"Hello\"" + endln,
						" \"Hello\"" + endln,
						"  \"Hello\"" + endln,
						"   \"Hello\"" + endln,
						"    \"Hello\"" + endln,
						"     \"Hello\"" + endln,
						"      \"Hello\"" + endln,
						"       \"Hello\"" + endln,
						"        \"Hello\"" + endln,
						"         \"Hello\"" + endln,
						"          \"Hello\"" + endln ) ;
		List <String> twoIndents =
				List.of ( "\"Hello\"" + endln,
					"  \"Hello\"" + endln,
					"    \"Hello\"" + endln,
					"      \"Hello\"" + endln,
					"        \"Hello\"" + endln,
					"          \"Hello\"" + endln,
					"            \"Hello\"" + endln,
					"              \"Hello\"" + endln,
					"                \"Hello\"" + endln,
					"                  \"Hello\"" + endln,
					"                    \"Hello\"" + endln ) ;
		Map <String, List <String>> setups = new LinkedHashMap <> ( ) ; // Can't use Map.of because order is unpredictable
		setups.put ( "Zero indents", zeroIndents ) ;
		setups.put ( "One indent", oneIndents ) ;
		setups.put ( "Two indents", twoIndents ) ;
		int indent = 0 ;
		for ( Map.Entry <String, List <String>> me : setups.entrySet ( ) ) {
			for ( int i = 0 ; i < me.getValue ( ).size ( ) ; i++ ) {
				StyleConfig cfg = StyleConfig.builder ( )
						.indentation ( StyleConfig.IndentationConfig.builder ( )
								.type ( StyleConfig.IndentationType.Spaces )
								.tabSize ( i )
								.build ( ) )
						.build ( ) ;
				OutputContext ctx = new OutputContext ( cfg ) ;
				for ( int j = 0 ; j < indent ; j++ ) {
					ctx.indent ( ) ;
				}
				StringFactory sf = ctx.render ( List.of ( new StringLiteral ( "Hello" ) ) ) ;
				String expected = me.getValue ( ).get ( i ) ;
				String actual = sf.toString ( ) ;
				if ( ! expected.equals ( actual ) ) {
					System.out.println ( "Arfe on " + me.getKey ( ) + " and at " + i + " with indent = " +  indent ) ;
				}
				assertEquals ( expected, actual, me.getKey ( ) + " correct for spaces with tabsize = " + i ) ;
			}
			indent++ ;
		}
	}
}
