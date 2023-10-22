package org.cogent.io;

import java.io.IOException ;
import java.io.InputStream ;
import java.util.LinkedList ;
import java.util.Queue ;
import java.util.function.Function ;

import org.cogent.model.Writeable ;

public class SubsequentInputStream extends InputStream {

	private InputStream current ;
	private Queue <Streamer <?>> substreams = new LinkedList <> ( ) ;

	public <T> void add ( T t, Function <? super T, InputStream> fx ) {
		substreams.add ( new Streamer <> ( t, fx ) ) ;
	}

	public <T> void add ( InputStream in ) {
		substreams.add ( new Streamer <> ( in, q -> q ) ) ;
	}

	public <T> void add ( Writeable w, WriteContext wc ) {
		substreams.add ( new Streamer <> ( w, q -> q.openInputStream ( wc ) ) ) ;
	}

	public void add ( CharSequence cs ) {
		substreams.add ( new Streamer <> ( cs, q -> new SimpleInputStream ( cs ) ) ) ;
	}

	public void addln ( CharSequence cs ) {
		add ( cs ) ;
		add ( System.getProperty ( "line.separator" ) ) ;
	}

	@Override
	public int read ( ) throws IOException {
		if ( current == null ) {
			if ( substreams.isEmpty ( ) ) {
				return -1 ;
			} else {
				current = nextStream ( ) ;
			}
		}
		int candidate = current.read ( ) ;
		if ( candidate == -1 ) {
			current = null ;
			return read ( ) ;
		} else {
			return candidate ;
		}
	}

	private <T> InputStream nextStream ( ) {
		@SuppressWarnings ( "unchecked" )
		Streamer <T> t = ( Streamer <T> ) substreams.poll ( ) ;
		return t.fx.apply ( t.o ) ;
	}

	private static record Streamer <T> ( T o, Function <? super T, InputStream> fx ) { ; }
}
