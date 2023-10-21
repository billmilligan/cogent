package org.cogent.playground;

import java.util.ArrayList ;
import java.util.HashMap ;
import java.util.List ;
import java.util.Map ;
import java.util.stream.Collectors ;

import org.cogent.model.JavaClassDefinition ;
import org.cogent.model.JavaSourceFile ;
import org.cogent.model.VisibilityModifier ;
import org.cogent.startup.Startup ;
import org.cogent.validation.MultiException ;
import org.cogent.validation.ValidationCode ;
import org.cogent.validation.ValidationException ;
import org.junit.jupiter.api.BeforeEach ;
import org.junit.jupiter.api.Test ;

import static org.junit.jupiter.api.Assertions.* ;
import static org.cogent.model.JavaSourceFile.SourceFileValidation.* ;
import static org.cogent.validation.ValidationException.SystemValidationCode.* ;
import static org.cogent.validation.ValidationContext.NamingValidationCode.* ;

@SuppressWarnings ( "unused" )
public class ValidationPlaygroundTest {

	private static final boolean BARF = false ;

	@BeforeEach
	public void setup ( ) {
		Startup.init ( ) ;
	}

	@Test
	public void workingOutValidation ( ) throws Exception {
		JavaClassDefinition def = new JavaClassDefinition ( "A1234" ) ;
		def.setVisibility ( VisibilityModifier.PRIVATE ) ;
		JavaSourceFile jf = new JavaSourceFile ( "123!4#.jarva" ) ;
		jf.setMainClass ( def ) ;

		Map <ValidationCode, List <Exception>> issues = runValidations ( ( ) -> jf.validate ( ) ) ;
		assertEquals ( 6, sizeof ( issues ), "Correct number of validations caught" ) ;
		assertCorrectSingleValidationProblem ( issues, MAIN_CLASS_WRONG_VISIBILITY, "private" ) ;
		assertCorrectSingleValidationProblem ( issues, NOT_JAVA_FILE, "jarva", "123!4#.jarva" ) ;
		assertCorrectSingleValidationProblem ( issues, STARTING_CHARACTER, "1" ) ;
		assertCorrectSingleValidationProblem ( issues, MAIN_CLASS_NOT_MATCHING_FILE, "123!4#.jarva", "A1234" ) ;

		List <ValidationException> nextCharProblems = assertMultipleValidationProblems ( issues, SUBSEQUENT_CHARACTER ) ;
		assertEquals ( 2, nextCharProblems.size ( ), "Correct number of subsequent problems" ) ;
		assertCorrectSingleValidationProblem ( nextCharProblems.get ( 0 ), "3", "!" ) ;
		assertCorrectSingleValidationProblem ( nextCharProblems.get ( 1 ), "5", "#" ) ;
	}

	private int sizeof ( Map <ValidationCode, List <Exception>> issues ) {
		return ( int  ) issues.values ( ).stream ( ).map ( s -> s.size ( ) ).collect ( Collectors.summarizingInt ( i -> i ) ).getSum ( ) ;
	}

	private Map <ValidationCode, List <Exception>> runValidations ( Runnable r ) {
		boolean caughtValidation = false ;
		MultiException caught = null ;
		try {
			r.run ( ) ;
		} catch ( MultiException me ) {
			caughtValidation = true ;
			caught = me ;
		}
		assertTrue ( caughtValidation, "Caught correct exception type" ) ;
		Map <ValidationCode, List <Exception>> issues = caught.getContents ( ).stream ( ).collect ( Collectors.toMap (
				ValidationException::codeOf, x -> List.of ( x ), ( y, z ) -> combine ( y, z ) ) ) ;
		if ( BARF ) {
			throw caught ;
		} else {
			return issues ;
		}
	}

	private void assertCorrectSingleValidationProblem ( Map <ValidationCode, List <Exception>> issues, ValidationCode problem, String ... problemDetail ) {
		assertTrue ( issues.containsKey ( problem ) ) ;
		List <Exception> excs = issues.get ( problem ) ;
		assertEquals ( 1, excs.size ( ), "Only one problem of this type" ) ;
		assertTrue ( excs.get ( 0 ) instanceof ValidationException, "Correct exception type" ) ;
		ValidationException wrongness = ( ValidationException ) excs.get ( 0 ) ;
		for ( int i = 0 ; i < problemDetail.length ; i++ ) {
			assertEquivalent ( problemDetail [ i ], wrongness.getParams ( ) [ i ], "Correctly identified problem" ) ;
		}
	}

	private void assertCorrectSingleValidationProblem ( ValidationException wrongness, String ... problemDetail ) {
		for ( int i = 0 ; i < problemDetail.length ; i++ ) {
			assertEquivalent ( problemDetail [ i ], wrongness.getParams ( ) [ i ], "Correctly identified problem" ) ;
		}
	}

	private List <ValidationException> assertMultipleValidationProblems ( Map <ValidationCode, List <Exception>> issues, ValidationCode problem ) {
		assertTrue ( issues.containsKey ( problem ) ) ;
		List <Exception> excs = issues.get ( problem ) ;
		List <ValidationException> retVal = new ArrayList <> ( ) ;
		assertTrue ( excs.size ( ) > 1, "More than one problem of this type" ) ;
		for ( Exception e : excs ) {
			assertTrue ( e instanceof ValidationException, "Correct exception type" ) ;
			retVal.add ( ( ValidationException ) e ) ;
		}

		return retVal ;
	}

	private void assertEquivalent ( Object o1, Object o2, String message ) {
		assertEquals ( String.valueOf ( o1 ), String.valueOf ( o2 ), message ) ;
	}

	private <T> List <T> combine ( List <T> y, List <T> z ) {
		List <T> retVal = new ArrayList <> ( y ) ;
		retVal.addAll ( z ) ;
		return retVal ;
	}
}
