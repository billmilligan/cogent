package org.cogent.startup;

import java.util.ArrayList ;
import java.util.List ;

import org.cogent.model.JavaSourceFile.SourceFileValidationMessages ;
import org.cogent.validation.ValidationContext.NamingValidationMessages ;
import org.cogent.validation.ValidationException.ValidationExceptionStarter ;

public class Startup {

	private static final Startup setup = new Startup ( ) ;

	private List <Starter> starters = new ArrayList <> ( ) ;

	static {
		setup.register ( new ValidationExceptionStarter ( ) ) ;
		setup.register ( new NamingValidationMessages ( ) ) ;
		setup.register ( new SourceFileValidationMessages ( ) ) ;
		StarterContext ctx = new StarterContext ( ) ;
		setup.go ( ctx ) ;
	}

	public void register ( Starter s ) {
		starters.add ( s ) ;
	}

	private void go ( StarterContext ctx ) {
		starters.forEach ( s -> s.start ( ctx ) ) ;
	}

	public static void init ( ) { ; }
}
