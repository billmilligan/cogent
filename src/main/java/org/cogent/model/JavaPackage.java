package org.cogent.model;

import java.io.InputStream ;
import java.io.PrintWriter ;

import org.cogent.io.PrintWriterSink ;
import org.cogent.io.SupplierInputStream ;
import org.cogent.io.WriteContext ;

import lombok.Getter ;

@Getter
public class JavaPackage implements Importable, Writeable, FullyQualifiable {

	private JavaPackage parent ;
	private String name ;

	public JavaPackage ( String name ) {
		this ( null, name ) ;
	}

	public JavaPackage ( JavaPackage parent, String name ) {
		this.parent = parent ;
		this.name = name ;
	}

	public static JavaPackage packageOf ( String name, SourceClassRegistry reg ) {
		if ( reg.containsPackage ( name ) ) {
			return reg.getPackage ( name ) ;
		} else {
			if ( name.contains ( "." ) ) {
				String parentPart = name.substring ( 0, name.lastIndexOf ( '.' ) ) ;
				JavaPackage parent = packageOf ( parentPart, reg ) ;
				JavaPackage retVal = new JavaPackage ( parent, name.substring ( name.lastIndexOf ( '.' ) + 1 ) ) ;
				return reg.registerPackage ( name, retVal ) ;
			} else {
				return reg.registerPackage ( name, new JavaPackage ( name ) ) ;
			}
		}
	}

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
