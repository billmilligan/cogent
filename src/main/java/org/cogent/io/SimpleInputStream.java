package org.cogent.io;

import java.io.IOException ;
import java.io.InputStream ;

public class SimpleInputStream extends InputStream {

	private CharSequence seq ;
	private int index ;
	private String name ;

	public SimpleInputStream ( CharSequence seq, String name ) {
		this.seq = seq ;
		this.index = 0 ;
		this.name = name ;
	}

	@Override
	public int read ( ) throws IOException {
		try {
			if ( this.index >= seq.length ( ) ) {
				return -1 ;
			} else {
				return seq.charAt ( index++ ) ;
			}
		} catch ( RuntimeException re ) {
			throw new IOException ( "Failed read on " + name, re ) ;
		}
	}
}
