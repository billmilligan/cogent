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

import lombok.Data ;

@Data
public abstract class JavaTypeDefinition implements Importable, Writeable, FullyQualifiable, Validatable {

	protected Optional <JavaPackage> pkg = Optional.empty ( ) ;
	protected List <Importable> imports = new ArrayList <> ( ) ;
	protected List <Comment> topLevelComments = new ArrayList <> ( ) ;
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
				addln ( "", "Blank 1" ) ;
			}
			imports.forEach ( p -> add ( p, wc ) ) ;
			if ( ! imports.isEmpty ( ) ) {
				addln ( "", "Blank 2" ) ;
			}
			topLevelComments.forEach ( p -> add ( p, wc ) ) ;
			add ( visibility.toString ( ), "Visibility on " + simpleName ) ;
			modifiers.forEach ( p -> add ( " " + p.toString ( ), "Modifier on " + simpleName ) ) ;
			add ( " ", "Blank 3" ) ;
			add ( getKind ( ).toString ( ), "Kind on " + simpleName ) ;
			add ( " ", "Blank 4" ) ;
			add ( simpleName, "Name of " + simpleName ) ;
			superClass.ifPresent ( p -> {
				add ( " extends ", "Extends of " + simpleName ) ;
				add ( p, wc ) ;
			} ) ;
			if ( ! implementsTypes.isEmpty ( ) ) {
				add ( " ", "Blank 5" ) ;
				boolean first = true ;
				for ( JavaTypeReference ref : implementsTypes ) {
					if ( ! first ) {
						add ( ", ", "Interface separator" ) ;
					} else {
						first = true ;
					}
					add ( ref, wc ) ;
				}
			}
			addln ( " {", "Begin class for " + simpleName ) ;
			contents.forEach ( p -> add ( p, wc ) ) ;
			addln ( "}", "End class for " + simpleName ) ;
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
