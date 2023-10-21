package org.cogent.io;

import java.util.HashMap ;
import java.util.Map ;

public class WriteContext {

	private String indent = "\t" ;
	private int indentationLevel = 0 ;
	private Map <Integer, String> indents = new HashMap <> ( ) ;

	public String getIndent ( ) {
		if ( ! indents.containsKey ( indentationLevel ) ) {
			indents.put ( indentationLevel, fillIndents ( ) ) ;
		}
		return indents.get ( indentationLevel ) ;
	}

	public void inc ( ) {
		indentationLevel++ ;
	}

	public void dec ( ) {
		--indentationLevel ;
	}

	private String fillIndents ( ) {
		StringBuilder sb = new StringBuilder ( ) ;
		for ( int i = 0 ; i < indentationLevel ; i++ ) {
			sb.append ( indent ) ;
		}
		return sb.toString ( ) ;
	}

	public String endStatement ( ) {
		return " ;" ;
	}
}
