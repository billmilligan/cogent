package org.cogent.model.output.styles;

import java.util.List ;

import org.cogent.model.output.OutputContext ;
import org.cogent.model.output.StyleConfig ;
import org.cogent.model.output.StyleConfig.IndentationConfig ;
import org.cogent.model.output.StyleConfig.IndentationType ;

public class Styles {

	public static final StyleConfig bill ;

	static {
		bill = StyleConfig.builder ( )
			.name ( "bill" )
			.indentation ( IndentationConfig.builder ( )
				.type ( IndentationType.Tabs )
				.tabSize ( 4 )
				.build ( ) )
			.build ( ) ;
	}

	public static OutputContext defaultContext ( ) {
		return bill ( ) ;
	}

	public static OutputContext randomContext ( ) {
		return bill ( ) ;
	}

	public static OutputContext bill ( ) {
		return new OutputContext ( bill ) ;
	}

	public List <OutputContext> all ( ) {
		return List.of ( bill ( ) ) ;
	}
}
