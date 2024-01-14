package org.cogent.model;

import org.junit.jupiter.api.Test ;

import static org.cogent.util.JavaIdentifierTrickBag.* ;
import static org.cogent.util.TDDTrickBag.* ;
import static org.junit.jupiter.api.Assertions.* ;

import java.util.Optional;

import org.cogent.io.PrintWriterSink ;
import org.cogent.io.StringFactory;
import org.cogent.io.WriteContext ;

public class JavaTypeDefinitionTest {

	private static final String endln = System.getProperty ( "line.separator" ) ;

	@Test
	public void testBasics ( ) throws Exception {
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
		assertEquals ( VisibilityModifier.PUBLIC, def.getVisibility ( ) ) ;
	}

	@Test
	public void testPkg ( ) throws Exception {
		SourceClassRegistry czzReg = new SourceClassRegistry ( ) ;
		String assignedPkgName = generatePackageName ( ) ;
		JavaPackage pkg = JavaPackage.packageOf ( assignedPkgName, czzReg ) ;
		Kind k = roll ( Kind.class ) ;
		String name = validLatinJavaTypeName ( ) ;
		JavaTypeDefinition def = new JavaTypeDefinition ( VisibilityModifier.PUBLIC, name ) {
			@Override
			public Kind getKind ( ) {
				return k ;
			}
		} ;
		def.setPkg ( Optional.of ( pkg ) ) ;

		PrintWriterSink sink = new PrintWriterSink ( ) ;
		WriteContext wc = new WriteContext ( ) ;
		def.write ( sink, wc ) ;
		String actual = sink.getContents ( ) ;
		StringFactory expected = new StringFactory ( ) ;
		expected.append ( "package " ) ;
		expected.append ( assignedPkgName ) ;
		expected.appendln ( " ;" ) ;
		expected.appendln ( "" ) ;
		expected.append ( "public " ) ;
		expected.append ( k ) ;
		expected.append ( " " ) ;
		expected.append ( name ) ;
		expected.appendln ( " {" ) ;
		expected.appendln ( "}" ) ;
		assertEquals ( expected.toString ( ), actual, "Match!" ) ;
		assertEquals ( assignedPkgName + "." + name, def.getFullyQualifiedName ( ) ) ;
		assertEquals ( name, def.getSimpleName ( ) ) ;
		ImportStatement importStatement = def.asImport ( ) ;
		assertEquals ( "import " + assignedPkgName + "." + name + " ;" + endln, importStatement.toString ( ) ) ;
		assertEquals ( VisibilityModifier.PUBLIC, def.getVisibility ( ) ) ;
	}

	@Test
	public void testSuperclass ( ) throws Exception {
		String name = validLatinJavaTypeName ( ) ;
		Kind k = roll ( Kind.CLASS, Kind.INTERFACE ) ;
		JavaTypeDefinition def = new JavaTypeDefinition ( VisibilityModifier.PUBLIC, name ) {
			@Override
			public Kind getKind ( ) {
				return k ;
			}
		} ;
		SourceClassRegistry reg = new SourceClassRegistry ( ) ;
		String parentClassName = validLatinJavaFQDN ( ) ;
		JavaTypeReference superRef = new JavaTypeReference ( parentClassName, reg ) ;
		def.setSuperClass ( Optional.of ( superRef ) ) ;

		PrintWriterSink sink = new PrintWriterSink ( ) ;
		WriteContext wc = new WriteContext ( ) ;
		def.write ( sink, wc ) ;
		String actual = sink.getContents ( ) ;
		StringFactory expected = new StringFactory ( ) ;
		expected.append ( "import " ) ;
		expected.append ( parentClassName ) ;
		expected.appendln ( " ;" ) ;
		expected.appendln ( "" ) ;
		expected.append ( "public " ) ;
		expected.append ( k ) ;
		expected.append ( " " ) ;
		expected.append ( name ) ;
		expected.append ( " extends " ) ;
		expected.append ( superRef.getSimpleName ( ) ) ;
		expected.appendln ( " {" ) ;
		expected.appendln ( "}" ) ;
		assertEquals ( expected.toString ( ), actual, "Match!" ) ;
		assertEquals ( name, def.getFullyQualifiedName ( ) ) ;
		assertEquals ( name, def.getSimpleName ( ) ) ;
		ImportStatement importStatement = def.asImport ( ) ;
		assertEquals ( "import " + name + " ;" + endln, importStatement.toString ( ) ) ;
		assertEquals ( VisibilityModifier.PUBLIC, def.getVisibility ( ) ) ;
	}

	private String generatePackageName ( ) {
		StringFactory sf = new StringFactory ( ) ;
		sf.append ( roll ( "com", "org", "bill", "io" ) ) ;
		int numComponents = roll ( 2, 6 ) ;
		for ( int i = 1 ; i < numComponents ; i++ ) {
			sf.append ( "." ) ;
			sf.append ( validLatinJavaNontypeName ( ) ) ;
		}
		return sf.toString ( ) ;
	}
}
