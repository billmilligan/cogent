package org.cogent.model.output;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.cogent.io.StringFactory;

public class Line {

	private List <Element> elts = new ArrayList <> ( ) ;

	public Line effectiveLine ( OutputContext ctx ) {
		return this ;
	}

	public Line nextLine ( OutputContext ctx ) {
		return null ;
	}

	public void render ( StringFactory sf, OutputContext ctx ) {
		sf.append ( ctx.render ( elts ) ) ;
	}

	public void add ( Element ... es ) {
		add ( Arrays.asList ( es ) ) ;
	}

	public void add ( List <Element>  es ) {
		elts.addAll ( es ) ;
	}

	public List <Element> getElements ( ) {
		return Collections.unmodifiableList ( elts ) ;
	}
}
