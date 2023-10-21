package org.cogent.io;

import java.io.PrintWriter ;
import java.io.StringWriter ;
import java.util.function.BiConsumer ;

public class PrintWriterSink extends PrintWriter implements CharSequence {

	private StringWriter sw ;
	private String contents ;
	private boolean contented = false ;

	public PrintWriterSink ( ) {
		super ( new StringWriter ( ) ) ;
		this.sw = ( StringWriter ) this.out ;
	}

	public String getContents ( ) {
		ensureContents ( ) ;
		return contents ;
	}

	public void reset ( ) {
		contented = false ;
	}

	private void ensureContents ( ) {
		if ( ! contented ) {
			contents = sw.toString ( ) ;
			contented = true ;
		}
	}

	@Override
	public String toString ( ) {
		ensureContents ( ) ;
		return contents ;
	}

	@Override
	public int length ( ) {
		ensureContents ( ) ;
		return contents.length ( ) ;
	}

	@Override
	public char charAt ( int index ) {
		ensureContents ( ) ;
		return contents.charAt ( index ) ;
	}

	@Override
	public CharSequence subSequence ( int start, int end ) {
		ensureContents ( ) ;
		return contents.subSequence ( start, end ) ;
	}

	public static CharSequence of ( BiConsumer <PrintWriter, WriteContext> pw, WriteContext wc ) {
		PrintWriterSink pew = new PrintWriterSink ( ) ;
		pw.accept ( pew, wc ) ;
		return pew ;
	}
}
