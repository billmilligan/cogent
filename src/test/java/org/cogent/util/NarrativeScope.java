package org.cogent.util;

import java.util.HashMap ;
import java.util.Map ;

public class NarrativeScope <K extends StrongKey> {

	private Map <K, Object> scope = new HashMap <> ( ) ;

	public <T> T set ( K k, T val ) {
		scope.put ( k, val ) ;
		return val ;
	}

	@SuppressWarnings ( "unchecked" )
	public <T> T get ( K k ) {
		return ( T ) scope.get ( k ) ;
	}

	public void clear ( ) {
		scope.clear ( ) ;
	}
}
