package org.cogent.io;

import java.util.ArrayList ;
import java.util.List ;
import java.util.function.Supplier ;
import java.util.stream.Collectors ;

public class StringFactory {

	private static final int DEFAULT_APPROX_LEN = 25 ;
	private List <Entry> entries = new ArrayList <> ( ) ;

	public StringFactory ( ) { ; }

	public StringFactory ( Object o ) {
		append ( o ) ;
	}

	public StringFactory ( Supplier <?> s ) {
		append ( s ) ;
	}

	public StringFactory append ( Object o ) {
		return append ( o, DEFAULT_APPROX_LEN ) ;
	}

	public StringFactory append ( Supplier <?> s ) {
		return append ( s, DEFAULT_APPROX_LEN ) ;
	}

	public StringFactory appendln ( Object o ) {
		if ( o instanceof CharSequence s ) {
			return appendln ( s, s.length ( ) ) ;
		} else {
			return appendln ( o, DEFAULT_APPROX_LEN ) ;
		}
	}

	public StringFactory appendln ( Supplier <?> s ) {
		return appendln ( s, DEFAULT_APPROX_LEN ) ;
	}

	public StringFactory append ( Object o, int approxLen ) {
		entries.add ( new Fixed ( o, approxLen ) ) ;
		return this ;
	}

	public StringFactory append ( Supplier <?> s, int approxLen ) {
		entries.add ( new Supplied ( s, approxLen ) ) ;
		return this ;
	}

	public StringFactory appendln ( Object o, int approxLen ) {
		entries.add ( new Fixed ( o, approxLen ) ) ;
		endln ( ) ;
		return this ;
	}

	public StringFactory appendln ( Supplier <?> s, int approxLen ) {
		entries.add ( new Supplied ( s, approxLen ) ) ;
		endln ( ) ;
		return this ;
	}

	private void endln ( ) {
		append ( System.getProperty ( "line.separator" ), 2 ) ;
	}

	public int getLength ( ) {
		return ( int ) entries.stream ( ).map ( t -> t.getLength ( ) ).collect ( Collectors.summarizingInt ( i -> i ) ).getSum ( ) ;
	}

	public PlaceHolder placeholder ( ) {
		PlaceHolder m = new PlaceHolder ( ) ;
		entries.add ( m ) ;
		return m ;
	}

	@Override
	public String toString ( ) {
		return build ( new StringBuilder ( getLength ( ) ), this ).toString ( ) ;
	}

	private StringBuilder build ( StringBuilder sb, StringFactory sf ) {
		for ( Entry e : sf.entries ) {
			if ( e instanceof Fixed f ) {
				sb.append ( f.asString ( ) ) ;
			} else if ( e instanceof PlaceHolder m ) {
				build ( sb, m.getFactory ( ) ) ;
			}
		}
		return sb ;
	}

	private static interface Entry {
		public int getLength ( ) ;
	} ;

	public static class PlaceHolder implements Entry {
		private StringFactory internalFactory = new StringFactory ( ) ;

		public StringFactory getFactory ( ) {
			return internalFactory ;
		}

		public int getLength ( ) {
			return internalFactory.getLength ( ) ;
		}
	}

	private static class Fixed implements Entry {
		private Object val ;
		private int approxLen ;

		public Fixed ( Object o, int approxLen ) {
			val = o ;
			this.approxLen = approxLen ;
		}

		public String asString ( ) {
			return val == null ? "null" : String.valueOf ( val ) ;
		}

		@Override
		public int getLength ( ) {
			return approxLen ;
		}
	}

	private static class Supplied extends Fixed {

		private Supplier <?> supplier ;

		public Supplied ( Supplier <?> o, int approxLen ) {
			super ( o, approxLen ) ;
			this.supplier = o ;
		}

		@Override
		public String asString ( ) {
			return supplier == null ? "null" : String.valueOf ( supplier.get ( ) ) ;
		}
	}
}
