package org.cogent.validation;

import java.io.OutputStreamWriter ;
import java.io.PrintStream ;
import java.io.PrintWriter ;
import java.util.List ;

import org.cogent.io.PrintWriterSink ;

public class MultiException extends RuntimeException {

	private static final long serialVersionUID = 6744739926263584793L ;

	private List <? extends Exception> contents ;

	public MultiException ( List <? extends Exception> contents ) {
		this.contents = contents ;
	}

	public List <? extends Exception> getContents ( ) {
		return contents;
	}

	@Override
	public String toString ( ) {
		PrintWriterSink pws = new PrintWriterSink ( ) ;
		printStackTrace ( pws ) ;
		return String.valueOf ( pws ) ;
	}

	public void printStackTrace ( ) {
        printStackTrace ( System.err ) ;
    }

    public void printStackTrace ( PrintStream s ) {
        printStackTrace ( new PrintWriter ( new OutputStreamWriter ( s ) ) ) ;
    }

    public void printStackTrace ( PrintWriter pw ) {
    	if ( contents == null || contents.isEmpty ( ) ) {
    		pw.println ( "No bundled exceptions" ) ;
    	} else if ( contents.size ( ) == 1 ) {
    		contents.get ( 0 ).printStackTrace ( pw ) ;
    	} else {
    		pw.println ( " ============== MULTIPLE EXCEPTIONS ============== " ) ;
    		pw.println ( " Exception Count: " + contents.size ( ) ) ;
    		int i = 0 ;
    		for ( Exception element : contents ) {
   	    		pw.println ( " ============== EXCEPTION " + i++ + " ============== " ) ;
    			element.printStackTrace ( pw ) ;
    			pw.println ( ) ;
    		}
    	}
    }
}
