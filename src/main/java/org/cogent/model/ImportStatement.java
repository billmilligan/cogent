package org.cogent.model;

import java.io.PrintWriter ;

public class ImportStatement implements Writeable {

	private String name ;

	public ImportStatement ( String name ) {
		this.name = name ;
	}

	@Override
	public void write ( PrintWriter pw, WriteContext wc ) {
		pw.println ( "import " + name + " ;" ) ;
	}

	@Override
	public String toString ( ) {
		return "ImportStatement of " + name ;
	}
}
