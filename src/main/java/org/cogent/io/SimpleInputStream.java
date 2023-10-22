package org.cogent.io;

import java.io.IOException ;
import java.io.InputStream ;

public class SimpleInputStream extends InputStream {

	private CharSequence seq ;
	private int index ;

	public SimpleInputStream ( CharSequence seq ) {
		this.seq = seq ;
		this.index = 0 ;
	}

	@Override
	public int read ( ) throws IOException {
		if ( this.index >= seq.length ( ) ) {
			return -1 ;
		} else {
			return seq.charAt ( index++ ) ;
		}
	}
}
