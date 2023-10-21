package org.cogent.model;

import java.io.InputStream ;
import java.io.PrintWriter ;

import org.cogent.io.PrintWriterSink ;
import org.cogent.io.SupplierInputStream ;
import org.cogent.io.WriteContext ;

import lombok.Data ;

@Data
public class JavaPackage implements Importable, Writeable, FullyQualifiable {

	private JavaPackage parent ;
	private String name ;

	@Override
	public void write ( PrintWriter pw, WriteContext wc ) {
		pw.println ( "package " + getFullyQualifiedName ( ) + wc.endStatement ( ) ) ;
	}

	@Override
	public String getFullyQualifiedName ( ) {
		if ( parent == null ) {
			return name ;
		} else {
			return parent.getFullyQualifiedName ( ) + "." + name ;
		}
	}

	@Override
	public InputStream openInputStream ( WriteContext wc ) {
		return new SupplierInputStream ( ( ) -> PrintWriterSink.of ( this::write, wc ) ) ;
	}

	@Override
	public ImportStatement asImport ( ) {
		return new ImportStatement ( getFullyQualifiedName ( ) + ".*" ) ;
	}

	@Override
	public String getSimpleName ( ) {
		return name ;
	}
}
