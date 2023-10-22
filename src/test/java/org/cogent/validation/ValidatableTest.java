package org.cogent.validation;

import static org.cogent.util.TDDTrickBag.* ;

import org.junit.jupiter.api.Test ;

public class ValidatableTest {

	@Test
	public void testValidatablePass ( ) {
		class Passable implements Validatable {
			@Override
			public void validate ( ValidationContext ctx ) {
				// Happy joy joy!
			}
		}

		Passable p = new Passable ( ) ;
		p.validate ( ) ;
	}

	@Test
	public void testValidatableFail ( ) {
		class Impassable implements Validatable {
			@Override
			public void validate ( ValidationContext ctx ) {
				throw new IllegalStateException ( "You shall not pass!!!!!" ) ;
			}
		}

		Impassable p = new Impassable ( ) ;
		try {
			p.validate ( ) ;
		} catch ( IllegalStateException iae ) {
			assertContains ( iae.getMessage ( ), "shall not" ) ;
		}
	}
}
