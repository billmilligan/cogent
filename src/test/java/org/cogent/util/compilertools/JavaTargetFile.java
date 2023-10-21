package org.cogent.util.compilertools;

import java.io.ByteArrayOutputStream ;
import java.io.IOException ;
import java.io.InputStream ;
import java.io.OutputStream ;
import java.io.Reader ;
import java.io.Writer ;
import java.net.URI ;

import javax.lang.model.element.Modifier ;
import javax.lang.model.element.NestingKind ;
import javax.tools.JavaFileObject ;

import org.cogent.model.JavaSourceFile ;

public class JavaTargetFile implements JavaFileObject {

	private ByteArrayOutputStream baos ;
	private String name ;
	private BabyClassLoader cl ;
	private JavaSourceFile source ;

	public JavaTargetFile ( String className, BabyClassLoader cl, JavaSourceFile source ) {
		this.name = className ;
		this.cl = cl ;
		this.source = source ;
	}

	@Override
	public URI toUri ( ) {
		return null ;
	}

	@Override
	public String getName ( ) {
		return name ;
	}

	@Override
	public InputStream openInputStream ( ) throws IOException {
		throw new UnsupportedOperationException ( ) ;
	}

	@Override
	public OutputStream openOutputStream ( ) throws IOException {
		baos = new ByteArrayOutputStream ( ) {
			@Override
			public void close ( ) {
				byte [ ] contents = toByteArray ( ) ;
				cl.defineMyClass ( name, contents, 0, contents.length ) ;
			}
		} ;
		return baos ;
	}

	@Override
	public Reader openReader ( boolean ignoreEncodingErrors ) throws IOException {
		throw new UnsupportedOperationException ( ) ;
	}

	@Override
	public CharSequence getCharContent ( boolean ignoreEncodingErrors ) throws IOException {
		if ( baos != null ) {
			return new String ( baos.toByteArray ( ) ) ;
		} else {
			return null ;
		}
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
		return Kind.CLASS ;
	}

	@Override
	public boolean isNameCompatible ( String simpleName, Kind kind ) {
		if ( kind != Kind.CLASS ) {
			return false ;
		}
		if ( simpleName == null || simpleName.isBlank ( ) ) {
			return false ;
		} else {
			if ( ! Character.isJavaIdentifierStart ( simpleName.charAt ( 0 ) ) ) {
				return false ;
			}
			for ( int i = 1 ; i < simpleName.length ( ) ; i++ ) {
				if ( Character.isJavaIdentifierPart ( simpleName.charAt ( i ) ) ) {
					return false ;
				}
			}
		}
		return true ;
	}

	@Override
	public NestingKind getNestingKind ( ) {
		return NestingKind.TOP_LEVEL ;
	}

	@Override
	public Modifier getAccessLevel ( ) {
		switch ( source.getMainClass ( ).getVisibility ( ) ) {
			case PUBLIC:	return Modifier.PUBLIC ;
			case PROTECTED:	return Modifier.PROTECTED ;
			case PRIVATE:	return Modifier.PRIVATE ;
			default:		return null ;
		}
	}
}
