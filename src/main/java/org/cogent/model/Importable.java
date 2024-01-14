package org.cogent.model;


public interface Importable extends Writeable, Comparable <Importable> {

	public ImportStatement asImport ( ) ;

	public default int compareTo ( Importable that ) {
		ImportStatement is1 = asImport ( ) ;
		ImportStatement is2 = asImport ( ) ;
		return is1.compareTo ( is2 ) ;
	}
}
