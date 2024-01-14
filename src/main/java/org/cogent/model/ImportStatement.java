package org.cogent.model;

import java.io.PrintWriter ;

import org.cogent.io.PrintWriterSink ;
import org.cogent.io.WriteContext ;

public class ImportStatement implements Writeable, Comparable <ImportStatement> {

	private String name ;

	public ImportStatement ( String name ) {
		this.name = name ;
	}

	@Override
	public void write ( PrintWriter pw, WriteContext wc ) {
		pw.println ( "import " + name + wc.endStatement ( ) ) ;
	}

	@Override
	public String toString ( ) {
		PrintWriterSink sink = new PrintWriterSink ( ) ;
		write ( sink, new WriteContext ( ) ) ;
		return sink.getContents ( ) ;
	}

	@Override
	public int compareTo ( ImportStatement that ) {
		return this.name.compareTo ( that.name ) ;
	}
}
