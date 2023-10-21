package org.cogent.model;

import java.io.PrintWriter ;
import java.util.ArrayList ;
import java.util.List ;

import org.cogent.io.WriteContext ;

public class JavaTypeReference implements Writeable {

	private String name ;
	private List <GenericVariable> vars = new ArrayList <> ( ) ;

	@Override
	public void write ( PrintWriter pw, WriteContext wc ) {
		pw.write ( name ) ;
		if ( ! vars.isEmpty ( ) ) {
			pw.write ( ' ' ) ;
			pw.write ( '<' ) ;
//			vars // let's not worry about this yet
			pw.write ( '>' ) ;
		}
	}

	@Override
	public String toString ( ) {
		return "Type Reference to " + name ;
	}
}
