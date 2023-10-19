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
import org.cogent.validation.Validatable ;
import org.cogent.validation.ValidationContext ;

import lombok.Getter ;
import lombok.Setter ;

@Getter
@Setter
public class JavaSourceFile implements Writeable, JavaFileObject, FullyQualifiable, Validatable {

	private String name ;
	private JavaTypeDefinition mainClass ;
	private List <JavaTypeDefinition> subsequentClasses = new ArrayList <> ( ) ;

	public JavaSourceFile ( String name ) {
		this.name = name ;
	}

	// 404-580-6265
	@Override
	public void validate ( ValidationContext ctx ) {
		ctx.contextualize ( ( ) -> {
			ctx.contextualize ( ( ) -> {
				if ( mainClass == null ) {
					ctx.fail ( "No main class present" ) ;
				}
				mainClass.validate ( ctx ) ;
			}, "Testing main class in file", mainClass ) ;
			ctx.contextualize ( ( ) -> {
				if ( ! name.endsWith ( ".java" ) ) {
					ctx.fail ( "File name does not end in .java", name ) ;
				}
				String fileSimpleName = name.substring ( 0, name.length ( ) - ".java".length ( ) ) ;
				ctx.validateJavaIdentifier ( fileSimpleName ) ;
				String className = mainClass.getSimpleName ( ) ;
				if ( ! Objects.equals ( className, fileSimpleName ) ) {
					ctx.fail ( "File name does not match internal main class name", name, className ) ;
				}
			}, "Validate file name", name ) ;
			subsequentClasses.forEach ( c -> {
				ctx.contextualize ( ( ) -> {
					c.validate ( ctx ) ;
				}, "Testing subsequent class", c ) ;
			} ) ;
		}, "Validate Source File", this );
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
