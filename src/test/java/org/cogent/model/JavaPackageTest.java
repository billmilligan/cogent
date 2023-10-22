package org.cogent.model;

import org.cogent.io.PrintWriterSink ;
import org.cogent.io.WriteContext ;
import org.junit.jupiter.api.Test ;

import java.io.BufferedReader ;
import java.io.InputStream ;
import java.io.InputStreamReader ;

import static org.junit.jupiter.api.Assertions.* ;
import static org.cogent.util.TDDTrickBag.* ;

public class JavaPackageTest {

	@Test
	public void testBasics ( ) throws Exception {
		String endln = System.getProperty ( "line.separator" ) ;
		SourceClassRegistry czzReg = new SourceClassRegistry ( ) ;
		WriteContext wc = new WriteContext ( ) ;
		JavaPackage p = JavaPackage.packageOf ( "org.arugula.foo.bar.baz", czzReg ) ;
		PrintWriterSink sink = new PrintWriterSink ( ) ;
		p.write ( sink, wc ) ;
		String actual = sink.getContents ( ) ;
		assertEquals ( "package org.arugula.foo.bar.baz ;" + endln, actual ) ;

		sink = new PrintWriterSink ( ) ;
		ImportStatement asImport = p.asImport ( ) ;
		// total aside -- let's check that toString() doesn't suck:
		String importStatementStr = asImport.toString ( ) ;
		assertNotContains ( importStatementStr, "@" ) ;
		asImport.write ( sink, wc ) ;
		actual = sink.getContents ( ) ;
		assertEquals ( "import org.arugula.foo.bar.baz.* ;" + endln, actual ) ;
		assertEquals ( "baz", p.getName ( ) ) ;
		assertEquals ( "baz", p.getSimpleName ( ) ) ;
		assertEquals ( "org.arugula.foo.bar.baz", p.getFullyQualifiedName ( ) ) ;
		assertEquals ( "org.arugula.foo.bar", p.getParent ( ).getFullyQualifiedName ( ) ) ;

		JavaPackage pp = JavaPackage.packageOf ( "org.arugula.foo.bar", czzReg ) ;
		assertTrue ( pp == p.getParent ( ), "Parent is identical to found because internalize worked") ;
	}

	@Test
	public void testOpenInputStream ( ) throws Exception {
		SourceClassRegistry czzReg = new SourceClassRegistry ( ) ;
		WriteContext wc = new WriteContext ( ) ;
		JavaPackage p = JavaPackage.packageOf ( "org.arugula.foo.bar.baz", czzReg ) ;
		InputStream in = p.openInputStream ( wc ) ;
		BufferedReader r = new BufferedReader ( new InputStreamReader ( in ) ) ;
		String actual = r.readLine ( ) ;
		assertEquals ( "package org.arugula.foo.bar.baz ;", actual ) ;
	}
}
