package org.cogent.model;

import java.util.HashMap ;
import java.util.Map ;

public class SourceClassRegistry {

	private Map <String, JavaPackage> packageRegistry = new HashMap <> ( ) ;

	public boolean containsPackage ( String name ) {
		return packageRegistry.containsKey ( name ) ;
	}

	public JavaPackage getPackage ( String name ) {
		return packageRegistry.get ( name ) ;
	}

	public JavaPackage registerPackage ( String name, JavaPackage retVal ) {
		packageRegistry.put ( name, retVal ) ;
		return retVal ;
	}
}
