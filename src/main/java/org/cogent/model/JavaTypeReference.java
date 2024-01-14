package org.cogent.model;

import java.io.PrintWriter ;
import java.util.ArrayList ;
import java.util.Arrays;
import java.util.List ;

import org.cogent.io.WriteContext ;

public class JavaTypeReference implements Importable, Writeable {

	private JavaPackage pkg ;
	private String simpleName ;
	private List <GenericVariable> vars = new ArrayList <> ( ) ;

	public JavaTypeReference ( String fqdn, SourceClassRegistry reg, GenericVariable ... gvars ) {
		String pkgName = packageNameOf ( fqdn ) ;
		pkg = JavaPackage.packageOf ( pkgName, reg ) ;
		simpleName = simpleNameOf ( fqdn ) ;
		vars.addAll ( Arrays.asList ( gvars ) ) ;
	}

	private String packageNameOf ( String fqdn ) {
		int idx = fqdn.lastIndexOf ( "." ) ;
		if ( idx == -1 ) {
			return "" ;
		} else {
			return fqdn.substring ( 0, idx ) ;
		}
	}

	private String simpleNameOf ( String fqdn ) {
		int idx = fqdn.lastIndexOf ( "." ) ;
		if ( idx == -1 ) {
			return fqdn ;
		} else {
			return fqdn.substring ( idx + 1 ) ;
		}
	}

	@Override
	public void write ( PrintWriter pw, WriteContext wc ) {
		pw.write ( simpleName ) ;
		/* You're not ready for this but your kids will love it
		if ( ! vars.isEmpty ( ) ) {
			pw.write ( ' ' ) ;
			pw.write ( '<' ) ;
//			vars // let's not worry about this yet
			pw.write ( '>' ) ;
		}
		*/
	}

	@Override
	public String toString ( ) {
		return "Type Reference to " + simpleName ;
	}

	@Override
	public ImportStatement asImport ( ) {
		if ( pkg.getFullyQualifiedName ( ).isBlank ( ) ) {
			return new ImportStatement ( simpleName ) ;
		} else {
			return new ImportStatement ( pkg.getFullyQualifiedName ( ) + "." + simpleName ) ;
		}
	}

	public JavaPackage getPkg ( ) {
		return pkg ;
	}

	public String getSimpleName ( ) {
		return simpleName ;
	}
}
