package org.cogent.model.compilertools;

import static javax.tools.StandardLocation.* ;

import java.io.IOException ;
import java.util.ArrayList ;
import java.util.HashMap ;
import java.util.LinkedHashMap ;
import java.util.List ;
import java.util.Map ;

import javax.tools.FileObject ;
import javax.tools.ForwardingJavaFileManager ;
import javax.tools.JavaFileManager ;
import javax.tools.JavaFileObject ;
import javax.tools.JavaFileObject.Kind ;

import org.cogent.model.JavaSourceFile ;

public class OverridingJavaFileManager extends ForwardingJavaFileManager <JavaFileManager> {

	private Map <Location, TreeNode> roots = new HashMap <> ( ) ;
	private BabyClassLoader cl ;

	public OverridingJavaFileManager ( JavaFileManager fileManager, BabyClassLoader cl ) {
		super ( fileManager ) ;
		for ( Location loc : List.of ( SOURCE_PATH ) ) {
			roots.put ( loc, new TreeNode ( loc ) ) ;
		}
		this.cl = cl ;
	}

	@Override
    public JavaFileObject getJavaFileForOutput(Location location, String className, Kind kind, FileObject sibling) throws IOException {
		return new JavaTargetFile ( className, cl, ( JavaSourceFile ) sibling ) ;
	}

	@Override
	public FileObject getFileForInput ( Location location, String packageName, String relativeName ) throws IOException {
		throw new UnsupportedOperationException ( ) ;
	}

	public void addSource ( JavaSourceFile jf ) {
		String name = jf.getPackageName ( ) ;
		Location dest = locationOf ( name, SOURCE_PATH ) ;
		dest = new Sublocation ( jf.getSimpleName ( ), dest ) ;
		put ( name, dest, jf ) ;
	}

	private void put ( String name, Location dest, JavaSourceFile jf ) {
		TreeNode n = findNode ( dest ) ;
		n.setLeaf ( jf ) ;
	}

	private TreeNode findNode ( Location dest ) {
		TreeNode n = roots.get ( dest ) ;
		if ( n == null ) {
			if ( dest instanceof Sublocation sub ) {
				TreeNode p = findNode ( sub.parent ) ;
				if ( p == null ) {
					return null ;
				}
				n = p.getChild ( dest ) ;
				if ( n == null ) {
					n = new TreeNode ( dest ) ;
					p.addChild ( dest, n ) ;
				}
				return n ;
			} else {
				return null ;
			}
		} else {
			return n ;
		}
	}

	private Map <String, Location> internalized = new HashMap <> ( ) ;
	private Location locationOf ( String s, Location parent ) {
		String key = parent.getName ( ) + ":" + s ;
		if ( ! internalized.containsKey ( key ) ) {
			Location loc = new Sublocation ( s, parent ) ;
			internalized.put ( key, loc ) ;
		}
		return internalized.get ( key ) ;
	}

	private static record Sublocation ( String name, Location parent ) implements Location {
		@Override
		public String getName ( ) {
			return name ;
		}

		@Override
		public boolean isOutputLocation ( ) {
			return false ;
		}
	}

	private static final class TreeNode {
		private Location loc ;
		private String name ;
		private Map <Location, TreeNode> children = new LinkedHashMap <> ( ) ;
		private JavaFileObject leaf ;

		public TreeNode ( Location loc ) {
			this ( loc, null ) ;
		}

		@SuppressWarnings ( "unused" )
		public boolean hasChildren ( ) {
			return ! children.isEmpty ( ) ;
		}

		public TreeNode getChild ( Location dest ) {
			return children.get ( dest ) ;
		}

		public TreeNode ( Location loc, String name ) {
			this.loc = loc ;
			this.name = name ;
		}

		@SuppressWarnings ( "unused" )
		public Location getLocation ( ) {
			return loc ;
		}

		@SuppressWarnings ( "unused" )
		public String getName ( ) {
			return name ;
		}

		public void addChild ( Location path, TreeNode n ) {
			children.put ( path, n ) ;
		}

		@SuppressWarnings ( "unused" )
		public List <TreeNode> listChildren ( ) {
			return new ArrayList <> ( children.values ( ) ) ;
		}

		@SuppressWarnings ( "unused" )
		public JavaFileObject getLeaf ( ) {
			return leaf;
		}

		public void setLeaf ( JavaFileObject leaf ) {
			this.leaf = leaf;
		}
	}
}
