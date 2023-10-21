package org.cogent.model;

import java.io.ByteArrayOutputStream ;
import java.io.IOException ;
import java.io.InputStream ;
import java.io.InputStreamReader ;
import java.io.OutputStream ;
import java.io.PrintWriter ;
import java.io.Reader ;
import java.io.Writer ;
import java.net.URI ;
import java.net.URISyntaxException ;
import java.util.ArrayList ;
import java.util.List ;
import java.util.Objects ;

import javax.lang.model.element.Modifier ;
import javax.lang.model.element.NestingKind ;
import javax.tools.JavaFileObject ;

import org.cogent.io.SubsequentInputStream ;
import org.cogent.startup.Starter ;
import org.cogent.startup.StarterContext ;
import org.cogent.validation.Validatable ;
import org.cogent.validation.ValidationCode ;
import org.cogent.validation.ValidationContext ;

import lombok.Getter ;
import lombok.Setter ;

import static org.cogent.model.JavaSourceFile.SourceFileValidation.* ;
import static org.cogent.validation.TemplateType.* ;

@Getter
@Setter
public class JavaSourceFile implements Writeable, JavaFileObject, FullyQualifiable, Validatable {

	public static enum SourceFileValidation implements ValidationCode {
		SOURCE_FILE, MAIN_CLASS, SUBSEQUENT_CLASS, FILE_NAME, NO_MAIN_CLASS,
		MAIN_CLASS_WRONG_VISIBILITY, MAIN_CLASS_NOT_STATIC, NOT_JAVA_FILE,
		MAIN_CLASS_NOT_MATCHING_FILE
	}

	public static class SourceFileValidationMessages implements Starter {
		@Override
		public void start ( StarterContext ctx ) {
			ctx.registerMessage ( SOURCE_FILE, CONTEXT, "Validating Source File {0}", 1 ) ;
			ctx.registerMessage ( SOURCE_FILE, FAILURE, "Failed to validate source file {0}", 1 ) ;
			ctx.registerMessage ( MAIN_CLASS, CONTEXT, "Validating Main Class {0} In File", 1 ) ;
			ctx.registerMessage ( MAIN_CLASS, FAILURE, "Failed to validate class {0}", 1 ) ;
			ctx.registerMessage ( SUBSEQUENT_CLASS, CONTEXT, "Validating Subsequent Class {0} In File", 1 ) ;
			ctx.registerMessage ( SUBSEQUENT_CLASS, FAILURE, "Failed to validate class {0}", 1 ) ;
			ctx.registerMessage ( FILE_NAME, CONTEXT, "Validate file name {0}", 1 ) ;
			ctx.registerMessage ( FILE_NAME, FAILURE, "Failed to validate file name {0}", 1 ) ;
			ctx.registerMessage ( NO_MAIN_CLASS, CONTEXT, "Validate main class exists", 0 ) ;
			ctx.registerMessage ( NO_MAIN_CLASS, FAILURE, "Failed to find main class", 0 ) ;
			ctx.registerMessage ( MAIN_CLASS_WRONG_VISIBILITY, CONTEXT, "Validating main class in file has acceptable visibility (is currently {0})", 1 ) ;
			ctx.registerMessage ( MAIN_CLASS_WRONG_VISIBILITY, FAILURE, "Main class in a file must be public or package protected but instead is {0}", 1 ) ;
			ctx.registerMessage ( MAIN_CLASS_NOT_STATIC, CONTEXT, "Validating main class in file is not static", 0 ) ;
			ctx.registerMessage ( MAIN_CLASS_NOT_STATIC, FAILURE, "Main class is static but should not be", 0 ) ;
			ctx.registerMessage ( NOT_JAVA_FILE, CONTEXT, "Validating file extension on {1} should end in .java and has extension of {0}", 2 ) ;
			ctx.registerMessage ( NOT_JAVA_FILE, FAILURE, "File name {1} does not end in .java but instead in {0}", 2 ) ;
			ctx.registerMessage ( MAIN_CLASS_NOT_MATCHING_FILE, CONTEXT, "Validating file name {0} matches internal main class name of {1}", 2 ) ;
			ctx.registerMessage ( MAIN_CLASS_NOT_MATCHING_FILE, FAILURE, "File name {0} does not match internal main class name of {1}", 2 ) ;
		}
	}

	private String name ;
	private JavaTypeDefinition mainClass ;
	private List <JavaTypeDefinition> subsequentClasses = new ArrayList <> ( ) ;

	public JavaSourceFile ( String name ) {
		this.name = name ;
	}

	@Override
	public String toString ( ) {
		return "Java Source File: " + name ;
	}

	// 404-580-6265
	@Override
	public void validate ( ValidationContext ctx ) {
		ctx.contextualize ( SOURCE_FILE, ( ) -> {
			ctx.contextualize ( MAIN_CLASS, ( ) -> {
				if ( mainClass == null ) {
					ctx.fail ( NO_MAIN_CLASS ) ;
				}
				mainClass.validate ( ctx ) ;
				VisibilityModifier mainVisibility = mainClass.getVisibility ( ) ;
				if ( mainVisibility != VisibilityModifier.PUBLIC && mainVisibility != VisibilityModifier.PACKAGE_PROTECTED ) {
					ctx.fail ( MAIN_CLASS_WRONG_VISIBILITY, mainVisibility ) ;
				}
				boolean mainClassIsStatic = mainClass.getModifiers ( ).contains ( TypeModifier.STATIC ) ;
				if ( mainClassIsStatic ) {
					ctx.fail ( MAIN_CLASS_NOT_STATIC ) ;
				}
			}, mainClass ) ;
			ctx.contextualize ( FILE_NAME, ( ) -> {
				String extension = extensionOf ( name ) ;
				if ( ! extension.equals ( "java" ) ) {
					ctx.fail ( NOT_JAVA_FILE, extension, name ) ;
				}
				String fileSimpleName = name.substring ( 0, name.length ( ) - extension.length ( ) - 1 ) ;
				ctx.validateJavaIdentifier ( fileSimpleName ) ;
				String className = mainClass.getSimpleName ( ) ;
				if ( ! Objects.equals ( className, fileSimpleName ) ) {
					ctx.fail ( MAIN_CLASS_NOT_MATCHING_FILE, name, className ) ;
				}
			}, name ) ;
			subsequentClasses.forEach ( c -> {
				ctx.contextualize ( SUBSEQUENT_CLASS, ( ) -> {
					c.validate ( ctx ) ;
				}, c ) ;
			} ) ;
		}, this );
	}

	private String extensionOf ( String name ) {
		int pos = name.lastIndexOf ( '.' ) ;
		if ( pos != -1 && pos != name.length ( ) - 1 ) {
			return name.substring ( pos + 1 ) ;
		} else {
			return "" ;
		}
	}

	@Override
	public void write ( PrintWriter pw, WriteContext wc ) {
		mainClass.write ( pw, wc ) ;
		subsequentClasses.forEach ( c -> c.write ( pw, wc ) ) ;
	}

	@Override
	public URI toUri ( ) {
		try {
			return new URI ( "aitchteeteepee://" + name ) ;
		} catch ( URISyntaxException e ) {
			throw new RuntimeException ( e ) ;
		}
	}

	@Override
	public String getName ( ) {
		return name ;
	}

	@Override
	public InputStream openInputStream ( WriteContext wc ) {
		return new SubsequentInputStream ( ) {{
			add ( mainClass, t -> t.openInputStream ( wc ) ) ;
			subsequentClasses.forEach ( t -> add ( t, q -> q.openInputStream ( wc ) ) ) ;
		}} ;
	}

	@Override
	public OutputStream openOutputStream ( ) throws IOException {
		throw new UnsupportedOperationException ( ) ;
	}

	@Override
	public Reader openReader ( boolean ignoreEncodingErrors ) throws IOException {
		validate ( ) ;
		return new InputStreamReader ( openInputStream ( ) ) ;
	}

	@Override
	public CharSequence getCharContent ( boolean ignoreEncodingErrors ) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream ( ) ;
		try ( InputStream in = openInputStream ( ) ) {
			for ( int c = in.read ( ) ; c != -1 ; c = in.read ( ) ) {
				baos.write ( c ) ;
			}
		}
		return new String ( baos.toByteArray ( ) ) ;
	}

	@Override
	public Writer openWriter ( ) throws IOException {
		throw new UnsupportedOperationException ( ) ;
	}

	@Override
	public long getLastModified ( ) {
		return System.currentTimeMillis ( ) ;
	}

	@Override
	public boolean delete ( ) {
		return false ;
	}

	@Override
	public Kind getKind ( ) {
		return Kind.SOURCE ;
	}

	@Override
	public boolean isNameCompatible ( String simpleName, Kind kind ) {
		if ( kind != Kind.SOURCE ) {
			return false ;
		}
		if ( simpleName == null || simpleName.isBlank ( ) ) {
			return false ;
		}
		char first = simpleName.charAt ( 0 ) ;
		if ( ! Character.isJavaIdentifierStart ( first ) ) {
			return false ;
		}
		for ( int i = 1 ; i < simpleName.length ( ) ; i++ ) {
			if ( ! Character.isJavaIdentifierPart ( simpleName.charAt ( i ) ) ) {
				return false ;
			}
		}
		return true ;
	}

	@Override
	public NestingKind getNestingKind ( ) {
		return null ;
	}

	@Override
	public Modifier getAccessLevel ( ) {
		VisibilityModifier visibility = mainClass.getVisibility ( ) ;
		switch ( visibility ) {
			case PUBLIC: return Modifier.PUBLIC ;
			case PROTECTED: return Modifier.PROTECTED ;
			case PACKAGE_PROTECTED: return Modifier.DEFAULT ;
			case PRIVATE: return Modifier.PRIVATE ;
		}
		throw new IllegalStateException ( "No access level" ) ;
	}

	@Override
	public InputStream openInputStream ( ) throws IOException {
		return openInputStream ( new WriteContext ( ) ) ;
	}

	@Override
	public String getSimpleName ( ) {
		return mainClass.getSimpleName ( ) ;
	}

	public String getPackageName ( ) {
		if ( mainClass.getPkg ( ).isEmpty ( ) ) {
			return "" ;
		} else {
			return mainClass.getPkg ( ).get ( ).getFullyQualifiedName ( ) ;
		}
	}

	@Override
	public String getFullyQualifiedName ( ) {
		return mainClass.getFullyQualifiedName ( ) ;
	}
}
