package org.cogent.util.compilertools ;

public class BabyClassLoader extends ClassLoader {

	public BabyClassLoader ( ClassLoader parent ) {
		super ( "Test Monkey", parent ) ;
	}

	public Class <?> defineMyClass ( String name, byte [ ] b, int off, int len ) {
		return super.defineClass ( name, b, off, len ) ;
	}
}
