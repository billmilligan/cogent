package org.cogent.io ;

import java.io.ByteArrayInputStream ;
import java.io.IOException ;
import java.io.InputStream ;
import java.util.function.Supplier ;

public class SupplierInputStream extends InputStream {

	private Supplier <? extends CharSequence> entry ;
	private boolean started = false ;
	private InputStream internal ;

	public SupplierInputStream ( Supplier <? extends CharSequence> entry ) {
		this.entry = entry ;
	}

	@Override
	public int read ( ) throws IOException {
		ensureOpen ( ) ;
		return internal.read ( ) ;
	}

	@Override
	public int read ( byte b [ ], int off, int len ) throws IOException {
		ensureOpen ( ) ;
		return internal.read ( b, off, len ) ;
	}

	@Override
	public int read ( byte b [ ] ) throws IOException {
		ensureOpen ( ) ;
		return internal.read ( b ) ;
	}

	@Override
	public byte [ ] readAllBytes ( ) throws IOException {
		ensureOpen ( ) ;
		return internal.readAllBytes ( ) ;
	}

	@Override
	public byte [ ] readNBytes ( int n ) throws IOException {
		ensureOpen ( ) ;
		return internal.readNBytes ( n ) ;
	}

	@Override
	public int readNBytes ( byte [ ] bs, int off, int n ) throws IOException {
		ensureOpen ( ) ;
		return internal.readNBytes ( bs, off, n ) ;
	}

	private void ensureOpen ( ) {
		if ( ! started ) {
			started = true ;
			internal = new ByteArrayInputStream ( entry.get ( ).toString ( ).getBytes ( ) ) ;
		}
	}
}
