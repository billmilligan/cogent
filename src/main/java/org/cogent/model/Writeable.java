package org.cogent.model;

import java.io.InputStream ;
import java.io.PrintWriter ;
import java.io.UncheckedIOException ;

import org.cogent.io.PrintWriterSink ;
import org.cogent.io.SupplierInputStream ;

public interface Writeable {

	public void write ( PrintWriter pw, WriteContext wc ) throws UncheckedIOException ;

	public default InputStream openInputStream ( WriteContext ctx ) {
		return new SupplierInputStream ( ( ) -> PrintWriterSink.of ( this::write, ctx ) ) ;
	}
}
