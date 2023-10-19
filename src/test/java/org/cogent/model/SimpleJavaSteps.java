package org.cogent.model;

import org.cogent.model.compilertools.BabyClassLoader ;
import org.cogent.model.compilertools.OverridingJavaFileManager ;
import org.cogent.model.util.JavaIdentifierTrickBag ;
import org.cogent.model.util.NarrativeScope ;
import org.cogent.model.util.StrongKey ;

import io.cucumber.java.After ;
import io.cucumber.java.en.Then ;
import io.cucumber.java.en.When ;

import java.io.StringWriter ;
import java.lang.reflect.Modifier ;
import java.nio.charset.Charset ;
import java.util.ArrayList ;
import java.util.HashSet ;
import java.util.List ;
import java.util.Locale ;
import java.util.Set ;

import javax.tools.DiagnosticCollector ;
import javax.tools.JavaCompiler ;
import javax.tools.JavaCompiler.CompilationTask ;
import javax.tools.JavaFileObject ;
import javax.tools.ToolProvider ;

import static org.cogent.model.SimpleJavaSteps.Key.* ;
import static org.junit.jupiter.api.Assertions.* ;

public class SimpleJavaSteps {

	static enum Key implements StrongKey {
		SELECTED_DEFINITION, EXPECTED_TYPE_NAME, EXPECTED_TYPE_MODIFIERS,
		EXPECTED_TOP_LEVEL_COMMENTS, EXPECTED_PACKAGE, EXPECTED_TYPE_VISIBILITY,
		EXPECTED_GENERIC_VARIABLES, EXPECTED_TYPE_SUPERTYPE, EXPECTED_INTERFACES,
		SOURCE_TO_COMPILE, CLASSLOADER, ACTUAL_CLASS
	}

	private NarrativeScope <Key> scope = new NarrativeScope <> ( ) ;

	@After
	public void clear ( ) {
		scope.clear ( ) ;
	}

	@When ( "I set up a Java class with a valid name" )
	public void setupClassDefWithValidName ( ) {
		String validTypeName = JavaIdentifierTrickBag.validLatinJavaTypeName ( ) ;
		scope.set ( EXPECTED_TYPE_NAME, validTypeName ) ;
		JavaTypeDefinition def = new JavaClassDefinition ( validTypeName ) ;
		scope.set ( SELECTED_DEFINITION, def ) ;
	}

	@When ( "the Java type definition has no modifiers" )
	public void selectedDefHasNoModifiers ( ) {
		scope.set ( EXPECTED_TYPE_MODIFIERS, new HashSet <> ( ) ) ;
	}

	@When ( "the Java type definition has no top-level comment" )
	public void selectedDefHasNoTopComments ( ) {
		scope.set ( EXPECTED_TOP_LEVEL_COMMENTS, new ArrayList <> ( ) );
	}

	@When ( "the Java type definition has no package" )
	public void selectedDefHasNoPackage ( ) {
		scope.set ( EXPECTED_PACKAGE, null ) ;
	}

	@When ( "the Java type definition is public" )
	public void selectedDefIsPublic ( ) {
		JavaTypeDefinition def = scope.get ( SELECTED_DEFINITION ) ;
		def.setVisibility ( VisibilityModifier.PUBLIC ) ;
		scope.set ( EXPECTED_TYPE_VISIBILITY, VisibilityModifier.PUBLIC ) ;
	}

	@When ( "the Java type definition has no generic variables" )
	public void selectedDefHasNoGenericVariables ( ) {
		scope.set ( EXPECTED_GENERIC_VARIABLES, new ArrayList <> ( ) ) ;
	}

	@When ( "the Java type definition has no supertype declared" )
	public void selectedDefHasNoSupertype ( ) {
		scope.set ( EXPECTED_TYPE_SUPERTYPE, null ) ;
	}

	@When ( "the Java type definition implements no interfaces" )
	public void selectedDefHasNoInterfaces ( ) {
		scope.set ( EXPECTED_INTERFACES, new ArrayList <> ( ) ) ;
	}

	@When ( "I compile the type definition into a class" )
	public void compileSelectedDef ( ) throws Exception {
		JavaTypeDefinition def = scope.get ( SELECTED_DEFINITION ) ;
		JavaSourceFile jf = new JavaSourceFile ( def.getFullyQualifiedName ( ) + ".java" ) ;
		jf.setMainClass ( def ) ;
		scope.set ( SOURCE_TO_COMPILE, jf ) ;
	}

	@Then ( "the class will compile without errors" )
	public void compileWithoutErrors ( ) throws Exception {
		JavaSourceFile jf = scope.get ( SOURCE_TO_COMPILE ) ;
		BabyClassLoader cl = scope.set ( CLASSLOADER, new BabyClassLoader ( getClass ( ).getClassLoader ( ) ) ) ;

		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler ( ) ;
		DiagnosticCollector <JavaFileObject> diagnostics = new DiagnosticCollector <JavaFileObject> ( ) ;
		OverridingJavaFileManager fm = new OverridingJavaFileManager ( compiler.getStandardFileManager ( diagnostics, Locale.US, Charset.defaultCharset ( ) ), cl ) ;
		fm.addSource ( jf ) ;
		StringWriter sw = new HairTriggerStringWriter ( ) ;
		CompilationTask task = compiler.getTask ( sw, fm, diagnostics, List.of ( ), List.of ( ), List.of ( jf ) ) ;
		boolean called = task.call ( ) ;
		String fatalErrors = sw.toString ( ) ;
		assertTrue ( called, "Successfully called compile" ) ;
		assertEquals ( "", fatalErrors, "No fatal errors" ) ;
		assertTrue ( diagnostics.getDiagnostics ( ).isEmpty ( ), "No problems of any kind" ) ;

		Class <?> actual = cl.loadClass ( jf.getMainClass ( ).getFullyQualifiedName ( ) ) ;
		assertNotNull ( actual, "Class found!" ) ;
		scope.set ( ACTUAL_CLASS, actual ) ;
	}

	@Then ( "the class will have the expected name" )
	public void correctTypeName ( ) throws Exception {
		Class <?> actual = scope.get ( ACTUAL_CLASS ) ;
		String typeName = scope.get ( EXPECTED_TYPE_NAME ) ;
		assertEquals ( typeName, actual.getName ( ), "Name matches, with package" ) ;
	}

	@Then ( "the class will have the correct visibility modifier" )
	public void correctVisibility ( ) {
		Class <?> actual = scope.get ( ACTUAL_CLASS ) ;
		VisibilityModifier expected = scope.get ( EXPECTED_TYPE_VISIBILITY ) ;
		int mod = actual.getModifiers ( ) ;
		switch ( expected ) {
			case PUBLIC:
				assertTrue ( Modifier.isPublic ( mod ), "Is public as expected" ) ;
				break ;
			case PROTECTED:
				assertTrue ( Modifier.isProtected ( mod ), "Is protecc as expecc" ) ;
				break ;
			case PRIVATE:
				assertTrue ( Modifier.isPrivate ( mod ), "Is private as expected" ) ;
				break ;
			case PACKAGE_PROTECTED:
				assertFalse ( Modifier.isPublic ( mod ), "Not public" ) ;
				assertFalse ( Modifier.isProtected ( mod ), "Not protected" ) ;
				assertFalse ( Modifier.isPrivate ( mod ), "Not private" ) ;
				break ;
		}
	}

	@Then ( "the class supertype will be correct" )
	public void correctSupertype ( ) {
		Class <?> actual = scope.get ( ACTUAL_CLASS ) ;
		String expectedSupertypeName = scope.get ( EXPECTED_TYPE_SUPERTYPE ) ;
		Class <?> superType = actual.getSuperclass ( ) ;
		if ( expectedSupertypeName == null ) {
			expectedSupertypeName = Object.class.getName ( ) ;
		}
		assertEquals ( expectedSupertypeName, superType.getName ( ), "Correct fully qualified name on supertype" ) ;
	}

	@Then ( "the class package will be correct" )
	public void correctPackage ( ) {
		Class <?> actual = scope.get ( ACTUAL_CLASS ) ;
		String expectedPackageName = scope.get ( EXPECTED_PACKAGE ) ;
		if ( expectedPackageName == null ) {
			expectedPackageName = "" ;
		}
		assertEquals ( expectedPackageName, actual.getPackageName ( ), "Correct package name" ) ;
	}

	@Then ( "the class will have the expected modifiers" )
	public void correctClassModifiers ( ) {
		Class <?> actual = scope.get ( ACTUAL_CLASS ) ;
		Set <String> expectedModifiers = scope.get ( EXPECTED_TYPE_MODIFIERS ) ;
		int actualModifiers = actual.getModifiers ( ) ;
		assertModifiersCorrectExcludingVisibility ( expectedModifiers, actualModifiers, "class " + actual.getName ( ) ) ;
	}

	private void assertModifiersCorrectExcludingVisibility ( Set <String> expectedModifiers, int actualModifiers, String message ) {
		boolean isAbstract = Modifier.isAbstract ( actualModifiers ) ;
		boolean isFinal = Modifier.isFinal ( actualModifiers ) ;
		boolean isInterface = Modifier.isInterface ( actualModifiers ) ;
		boolean isNative = Modifier.isNative ( actualModifiers ) ;
		boolean isStatic = Modifier.isStatic ( actualModifiers ) ;
		boolean isStrict = Modifier.isStrict ( actualModifiers ) ;
		boolean isSynchronized = Modifier.isSynchronized ( actualModifiers ) ;
		boolean isTransient = Modifier.isTransient ( actualModifiers ) ;
		boolean isVolatile = Modifier.isVolatile ( actualModifiers ) ;
		assertEquals ( isAbstract, expectedModifiers.contains ( "abstract" ), "Is abstract or not as expected for " + message ) ;
		assertEquals ( isFinal, expectedModifiers.contains ( "final" ), "Is final or not as expected for " + message ) ;
		assertEquals ( isInterface, expectedModifiers.contains ( "interface" ), "Is interface or not as expected for " + message ) ;
		assertEquals ( isNative, expectedModifiers.contains ( "native" ), "Is native or not as expected for " + message ) ;
		assertEquals ( isStatic, expectedModifiers.contains ( "static" ), "Is static or not as expected for " + message ) ;
		assertEquals ( isStrict, expectedModifiers.contains ( "strictfp" ), "Is strictfp or not as expected for " + message ) ;
		assertEquals ( isSynchronized, expectedModifiers.contains ( "synchronized" ), "Is synchronized or not as expected for " + message ) ;
		assertEquals ( isTransient, expectedModifiers.contains ( "transient" ), "Is transient or not as expected for " + message ) ;
		assertEquals ( isVolatile, expectedModifiers.contains ( "volatile" ), "Is volatile or not as expected for " + message ) ;
	}

	@Then ( "I can instantiate the class with a no-arg constructor" )
	public void instantiation ( ) throws Exception {
		Class <?> actual = scope.get ( ACTUAL_CLASS ) ;
		Object newInstance = actual.getDeclaredConstructor ( ).newInstance ( ) ;
		assertNotNull ( newInstance, "New class instantiated!" ) ;
	}

	/*
	 * If need be, I can set breakpoints to get context and trace info
	 * on specific messages.
	 */
	private static class HairTriggerStringWriter extends StringWriter {
		@Override
	    public void write(String str) {
			super.write ( str ) ;
	    }
		@Override
	    public void write(int c) {
	        super.write ( c ) ;
	    }
		@Override
	    public void write(char cbuf[], int off, int len) {
			super.write ( cbuf, off, len ) ;
		}
		@Override
	    public void write(String str, int off, int len)  {
	    	super.write ( str, off, len ) ;
	    }
	}
}
