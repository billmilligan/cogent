package org.cogent.model.output;


public class StringLiteral extends Element implements Literal <String> {

	private String value ;

	public StringLiteral ( String value ) {
		this.value = value ;
	}

	@Override
	public String render ( OutputContext ctx ) {
		return value == null ? "null" : ( "\"" + value + "\"" ) ;
	}
}
