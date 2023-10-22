package org.cogent.util ;

import static org.cogent.util.TDDTrickBag.* ;

public class JavaIdentifierTrickBag {

//	private String αρετη ; // TODO, add some stuff for more UTF-8

	public static String validLatinJavaNontypeName ( ) {
		StringBuilder sb = new StringBuilder ( ) ;
		int idLen = rollDice ( 1, 20 ) ;
		for ( int i = 0 ; i < idLen ; i++ ) {
			if ( i == 0 ) {
				sb.append ( Character.toLowerCase ( validLatinJavaIdentifierStart ( ) ) ) ;
			} else {
				sb.append ( validLatinJavaIdentifierPart ( ) ) ;
			}
		}
		return sb.toString ( ) ;
	}

	public static String validLatinJavaTypeName ( ) {
		StringBuilder sb = new StringBuilder ( ) ;
		int idLen = rollDice ( 1, 20 ) ;
		for ( int i = 0 ; i < idLen ; i++ ) {
			if ( i == 0 ) {
				sb.append ( Character.toUpperCase ( validLatinJavaIdentifierStart ( ) ) ) ;
			} else {
				sb.append ( validLatinJavaIdentifierPart ( ) ) ;
			}
		}
		return sb.toString ( ) ;
	}

	private static char validLatinJavaIdentifierStart ( ) {
		int choice = rollDice ( 0, 53 ) ;
		if ( choice < 26 ) {
			return ( char ) ( 'A' + choice ) ;
		} else if ( choice < 52 ) {
			return ( char ) ( 'a' + choice - 26) ;
		} else if ( choice == 52 ) {
			return '_' ;
		} else {
			return '$' ;
		}
	}

	private static char validLatinJavaIdentifierPart ( ) {
		int choice = rollDice ( 0, 63 ) ;
		if ( choice < 26 ) {
			return ( char ) ( 'A' + choice ) ;
		} else if ( choice < 52 ) {
			return ( char ) ( 'a' + choice - 26 ) ;
		} else if ( choice == 52 ) {
			return '_' ;
		} else if ( choice == 53 ){
			return '$' ;
		} else {
			return ( char ) ( '0' + ( choice - 54 ) ) ;
		}
	}
}
