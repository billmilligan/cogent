package org.cogent.model;

public class JavaClassDefinition extends JavaTypeDefinition {

	public JavaClassDefinition ( VisibilityModifier modifier, String simpleName ) {
		super ( modifier, simpleName ) ;
	}

	@Override
	public ImportStatement asImport ( ) {
		return new ImportStatement ( this.getFullyQualifiedName ( ) ) ;
	}

	@Override
	public String getFullyQualifiedName ( ) {
		if ( pkg.isPresent ( ) ) {
			return pkg.get ( ).getFullyQualifiedName ( ) + "." + simpleName ;
		} else {
			return simpleName ;
		}
	}

	@Override
	public Kind getKind ( ) {
		return Kind.CLASS ;
	}
}
