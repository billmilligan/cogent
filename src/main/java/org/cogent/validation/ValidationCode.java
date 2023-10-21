package org.cogent.validation;

import org.cogent.messages.Code ;
import org.cogent.model.FullyQualifiable ;

public interface ValidationCode extends FullyQualifiable, Code {

	public String name ( ) ;

	public default String getSimpleName ( ) {
		return name ( ) ;
	}

	public default String getFullyQualifiedName ( ) {
		String fqn = getClass ( ).getSimpleName ( ) ;
		if ( fqn.indexOf ( '$' ) != -1 ) {
			fqn = fqn.substring ( fqn.indexOf ( '$' ) + 1 ) ;
		}
		return fqn + ":" + name ( ) ;
	}
}
