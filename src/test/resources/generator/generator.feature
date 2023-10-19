#Author: public@billmilligan.com
Feature: Code Generation

  Scenario: Vanillamost Generation
		When I set up a Java class with a valid name
		And the Java type definition has no modifiers
		And the Java type definition has no top-level comment
		And the Java type definition has no package
		And the Java type definition is public
		And the Java type definition has no generic variables
		And the Java type definition has no supertype declared
		And the Java type definition implements no interfaces
		And I compile the type definition into a class
		Then the class will compile without errors
		And the class will have the expected name
		And the class will have the correct visibility modifier
		And the class supertype will be correct
		And the class package will be correct
		And the class will have the expected modifiers
		And I can instantiate the class with a no-arg constructor

  Scenario: Generate abstract class
		When I set up a Java class with a valid name
		And the Java type definition is set as abstract
		And the Java type definition has no top-level comment
		And the Java type definition has no package
		And the Java type definition is public
		And the Java type definition has no generic variables
		And the Java type definition has no supertype declared
		And the Java type definition implements no interfaces
		And I compile the type definition into a class
		Then the class will compile without errors
		And the class will have the expected name
		And the class will have the correct visibility modifier
		And the class supertype will be correct
		And the class package will be correct
		And the class will have the expected modifiers
		And I cannot instantiate the class with a no-arg constructor because it is not a concrete type

  Scenario: Generate final class
		When I set up a Java class with a valid name
		And the Java type definition is set as final
		And the Java type definition has no top-level comment
		And the Java type definition has no package
		And the Java type definition is public
		And the Java type definition has no generic variables
		And the Java type definition has no supertype declared
		And the Java type definition implements no interfaces
		And I compile the type definition into a class
		Then the class will compile without errors
		And the class will have the expected name
		And the class will have the correct visibility modifier
		And the class supertype will be correct
		And the class package will be correct
		And the class will have the expected modifiers
		And I can instantiate the class with a no-arg constructor
