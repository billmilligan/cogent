package org.cogent.model.output;

import org.cogent.io.StringFactory ;

public class IntegerLiteralElement extends Element implements Literal <Integer> {

	private Integer value ;

	public IntegerLiteralElement ( Integer value ) {
		this.value = value ;
	}

	@Override
	public CharSequence render ( OutputContext ctx ) {
		if ( value < 0 ) {
			StringFactory sf = new StringFactory ( "-" ) ;
			if ( ctx.getCfg ( ).whitespace ( ).expression ( ).unaryOperator ( ).afterPrefixOperator ( ) ) {
				sf.append ( " " ) ;
			}
			return sf.append ( value ) ;
		} else {
			return String.valueOf ( value ) ;
		}
	}
}
