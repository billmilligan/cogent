package org.cogent.model;

import org.junit.jupiter.api.Test ;

import static org.cogent.util.JavaIdentifierTrickBag.* ;
import static org.cogent.util.TDDTrickBag.* ;
import static org.junit.jupiter.api.Assertions.* ;

import org.cogent.io.PrintWriterSink ;
import org.cogent.io.WriteContext ;

public class JavaTypeDefinitionTest {

	@Test
	public void testBasics ( ) throws Exception {
		String endln = System.getProperty ( "line.separator" ) ;
		String name = validLatinJavaTypeName ( ) ;
		Kind k = roll ( Kind.class ) ;
		JavaTypeDefinition def = new JavaTypeDefinition ( VisibilityModifier.PUBLIC, name ) {
			@Override
			public Kind getKind ( ) {
				return k ;
			}
		} ;

		PrintWriterSink sink = new PrintWriterSink ( ) ;
		WriteContext wc = new WriteContext ( ) ;
		def.write ( sink, wc ) ;
		String actual = sink.getContents ( ) ;
		StringBuilder expected = new StringBuilder ( ) ;
		expected.append ( "public " ) ;
		expected.append ( k ) ;
		expected.append ( " " ) ;
		expected.append ( name ) ;
		expected.append ( " {" ) ;
		expected.append ( endln ) ;
		expected.append ( "}" ) ;
		expected.append ( endln ) ;
		assertEquals ( expected.toString ( ), actual, "Match!" ) ;
		assertEquals ( name, def.getFullyQualifiedName ( ) ) ;
		assertEquals ( name, def.getSimpleName ( ) ) ;
		ImportStatement importStatement = def.asImport ( ) ;
		assertEquals ( "import " + name + " ;" + endln, importStatement.toString ( ) ) ;
	}
}
