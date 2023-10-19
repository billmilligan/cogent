package org.cogent.model;

public class JavaClassDefinition extends JavaTypeDefinition {

	public JavaClassDefinition ( String simpleName ) {
		super ( simpleName ) ;
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
