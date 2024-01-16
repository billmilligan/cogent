package org.cogent.model.output ;

import java.util.ArrayList ;
import java.util.List ;

import org.cogent.io.StringFactory ;

import lombok.Getter ;
import lombok.Setter ;

@Getter
@Setter
public class OutputContext {

	private StyleConfig cfg ;
	private int indentationLevel = 0 ;

	public OutputContext ( StyleConfig cfg ) {
		this.cfg = cfg ;
	}

	public void indent ( ) {
		indentationLevel++ ;
	}

	public void unindent ( ) {
		--indentationLevel ;
		if ( indentationLevel < 0 ) {
			throw new IllegalStateException ( "Cannot have negative indent" ) ;
		}
	}

	public StringFactory render ( List <Element> elts ) {
		List <Element> localElements = new ArrayList <> ( elts ) ;
		formatForWhitespace ( localElements ) ;
		StringFactory sf = new StringFactory ( ) ;
		renderIndentation ( sf ) ;
		elts.forEach ( e -> sf.append ( e.render ( this ) ) ) ;
		renderNewline ( sf ) ;
		return sf ;
	}

	private void formatForWhitespace ( List <Element> localElements ) {
		// TODO Auto-generated method stub
		
	}

	private void renderNewline ( StringFactory sf ) {
		sf.append ( System.getProperty ( "line.separator" ) ) ;
	}

	private void renderIndentation ( StringFactory sf ) {
		String indentationStr = switch ( cfg.indentation ( ).type ( ) ) {
			case Tabs -> "\t" ;
			case Spaces -> tabspace ( cfg.indentation ( ).tabSize ( ) ) ;
		} ;
		for ( int i = 0 ; i < indentationLevel ; i++ ) {
			sf.append ( indentationStr ) ;
		}
	}

	private String tabspace ( int tabSize ) {
		return switch ( tabSize ) {
			case 0 -> "" ;
			case 1 -> " " ;
			case 2 -> "  " ;
			case 3 -> "   " ;
			case 4 -> "    " ;
			case 5 -> "     " ;
			case 6 -> "      " ;
			case 7 -> "       " ;
			case 8 -> "        " ;
			default -> repeat ( " ", tabSize ) ;
		} ;
	}

	private String repeat ( String s, int tabSize ) {
		StringBuilder sb = new StringBuilder ( ) ;
		for ( int i = 0 ; i < tabSize ; i++ ) {
			sb.append ( s ) ;
		}
		return sb.toString ( ) ;
	}
}
