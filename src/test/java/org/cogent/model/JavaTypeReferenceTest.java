package org.cogent.model;

import org.junit.jupiter.api.Test;

import static org.cogent.util.JavaIdentifierTrickBag.* ;
import static org.junit.jupiter.api.Assertions.* ;

import org.cogent.io.PrintWriterSink;
import org.cogent.io.WriteContext;

public class JavaTypeReferenceTest {

	private static final String endln = System.getProperty ( "line.separator" ) ;

	@Test
	public void testFullyQualifiedReferenceImport ( ) {
		SourceClassRegistry reg = new SourceClassRegistry ( ) ;
		String fqdn = validLatinJavaFQDN ( ) ;
		JavaTypeReference ref = new JavaTypeReference ( fqdn, reg ) ;
		ImportStatement imp = ref.asImport ( ) ;
		assertEquals ( "import " + fqdn + " ;" + endln, imp.toString ( ) ) ;
		assertFQDNContainedFully ( fqdn, ref ) ;
	}

	@Test
	public void testFullyQualifiedReferenceWrite ( ) {
		SourceClassRegistry reg = new SourceClassRegistry ( ) ;
		String fqdn = validLatinJavaFQDN ( ) ;
		String className = fqdn.substring ( fqdn.lastIndexOf ( "." ) + 1 ) ;
		JavaTypeReference ref = new JavaTypeReference ( fqdn, reg ) ;
		PrintWriterSink sink = new PrintWriterSink ( ) ;
		ref.write ( sink, new WriteContext ( ) ) ;
		assertEquals ( sink.toString ( ), className ) ;
	}

	@Test
	public void testFullyQualifiedReferenceToString ( ) {
		SourceClassRegistry reg = new SourceClassRegistry ( ) ;
		String fqdn = validLatinJavaFQDN ( ) ;
		JavaTypeReference ref = new JavaTypeReference ( fqdn, reg ) ;
		String className = fqdn.substring ( fqdn.lastIndexOf ( "." ) + 1 ) ;
		assertEquals ( "Type Reference to " + className, ref.toString ( ) ) ;
	}

	private void assertFQDNContainedFully ( String fqdn, JavaTypeReference ref) {
		assertTrue ( fqdn.startsWith ( ref.getPkg ( ).getFullyQualifiedName ( ) ), "First part of ref is correct" ) ;
		assertTrue ( fqdn.endsWith ( ref.getSimpleName ( ) ), "End part of ref is correct" ) ;
		if ( fqdn.equals ( ref.getSimpleName ( ) ) ) {
			assertEquals ( fqdn.length ( ), ref.getSimpleName ( ).length ( ) + ref.getPkg ( ).getFullyQualifiedName ( ).length ( ), "Middle does not exist!" ) ;
		} else {
			assertEquals ( fqdn.length ( ), ref.getSimpleName ( ).length ( ) + ref.getPkg ( ).getFullyQualifiedName ( ).length ( ) + 1, "Middle part of ref is only one character long" ) ;
		}
	}

	@Test
	public void testSimpleQualifiedReferenceImport ( ) {
		SourceClassRegistry reg = new SourceClassRegistry ( ) ;
		String fqdn = validLatinJavaTypeName ( ) ;
		JavaTypeReference ref = new JavaTypeReference ( fqdn, reg ) ;
		ImportStatement imp = ref.asImport ( ) ;
		assertEquals ( "import " + fqdn + " ;" + endln, imp.toString ( ) ) ;
		assertFQDNContainedFully ( fqdn, ref ) ;
	}
}
