package org.cogent.model;
import java.io.IOException ;
import java.io.InputStream ;
import java.io.PrintWriter ;
import java.io.UncheckedIOException ;
import java.util.ArrayList ;
import java.util.List ;
import java.util.Optional ;
import java.util.Set ;
import java.util.TreeSet ;

import org.cogent.io.SubsequentInputStream ;
import org.cogent.io.WriteContext ;
import org.cogent.validation.Validatable ;
import org.cogent.validation.ValidationContext ;

import lombok.Getter ;
import lombok.Setter ;

@Getter
@Setter
public abstract class JavaTypeDefinition implements Importable, Writeable, FullyQualifiable, Validatable {

	protected Optional <JavaPackage> pkg = Optional.empty ( ) ;
	protected List <Importable> imports = new ArrayList <> ( ) ;
	protected List <Comment> topLevelComments = new ArrayList <> ( ) ;
	protected List <JavaAnnotationReference> annotations = new ArrayList <> ( ) ;
	protected VisibilityModifier visibility ;
	protected String simpleName ;
	protected Set <TypeModifier> modifiers = new TreeSet <> ( ) ;
	protected Optional <JavaTypeReference> superClass = Optional.empty ( ) ;
	protected List <JavaTypeReference> implementsTypes = new ArrayList <> ( ) ;
	protected List <GenericVariable> classLevelGenerics = new ArrayList <> ( ) ;
	protected List <JavaClassContent> contents = new ArrayList <> ( ) ;

	public JavaTypeDefinition ( String simpleName ) {
		this.simpleName = simpleName ;
	}

	public abstract Kind getKind ( ) ;

	@Override
	public String toString ( ) {
		return getKind ( ) + " " + getFullyQualifiedName ( ) ;
	}

	@Override
	public void validate ( ValidationContext ctx ) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public InputStream openInputStream ( WriteContext wc ) {
		return new SubsequentInputStream ( ) {{
			pkg.ifPresent ( p -> add ( p, wc ) ) ;
			if ( pkg.isPresent ( ) ) {
				addln ( "" ) ;
			}
			imports.forEach ( p -> add ( p, wc ) ) ;
			if ( ! imports.isEmpty ( ) ) {
				addln ( "" ) ;
			}
			topLevelComments.forEach ( p -> add ( p, wc ) ) ;
			add ( visibility.toString ( ) ) ;
			modifiers.forEach ( p -> add ( " " + p.toString ( ) ) ) ;
			add ( " " ) ;
			add ( getKind ( ).toString ( ) ) ;
			add ( " " ) ;
			add ( simpleName ) ;
			superClass.ifPresent ( p -> {
				add ( " extends " ) ;
				add ( p, wc ) ;
			} ) ;
			if ( ! implementsTypes.isEmpty ( ) ) {
				add ( " " ) ;
				boolean first = true ;
				for ( JavaTypeReference ref : implementsTypes ) {
					if ( ! first ) {
						add ( ", " ) ;
					} else {
						first = true ;
					}
					add ( ref, wc ) ;
				}
			}
			addln ( " {" ) ;
			contents.forEach ( p -> add ( p, wc ) ) ;
			addln ( "}" ) ;
		}} ;
	}

	@Override
	public void write ( PrintWriter pw, WriteContext wc ) throws UncheckedIOException {
		try ( InputStream in = openInputStream ( wc ) ) {
			for ( int c = in.read ( ) ; c != -1 ; c = in.read ( ) ) {
				pw.write ( c ) ;
			}
		} catch ( IOException ioe ) {
			throw new UncheckedIOException ( ioe ) ;
		}
	}
}
