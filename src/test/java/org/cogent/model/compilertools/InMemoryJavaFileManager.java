package org.cogent.model.compilertools;

import static javax.tools.StandardLocation.* ;

import java.io.IOException ;
import java.util.ArrayList ;
import java.util.HashMap ;
import java.util.Iterator ;
import java.util.LinkedHashMap ;
import java.util.List ;
import java.util.Map ;
import java.util.Set ;

import javax.tools.FileObject ;
import javax.tools.JavaFileManager ;
import javax.tools.JavaFileObject ;
import javax.tools.JavaFileObject.Kind ;

import org.cogent.model.JavaSourceFile ;

/*
 * Did not work in the slightest.  Keeping for reference temporarily.
 */
public class InMemoryJavaFileManager implements JavaFileManager {

	private Map <Location, TreeNode> roots = new HashMap <> ( ) ;

	public InMemoryJavaFileManager ( ) {
		for ( Location loc : List.of ( SOURCE_PATH, CLASS_OUTPUT, CLASS_PATH ) ) {
			roots.put ( loc, new TreeNode ( loc ) ) ;
		}
	}
/**/
	@Override
	public int isSupportedOption ( String option ) {
		// TODO Auto-generated method stub
		// --multi-release
		return 0 ;
	}

	@Override
	public ClassLoader getClassLoader ( Location location ) {
		// CLASS_PATH
		return getClass ( ).getClassLoader ( ) ;
	}

	@Override
	public Iterable <JavaFileObject> list ( Location location, String packageName, Set <Kind> kinds, boolean recurse ) throws IOException {
		List <JavaFileObject> retVal = new ArrayList <> ( ) ;
		Location specificLoc = locationOf ( packageName, location ) ;
		TreeNode origin = findNode ( specificLoc ) ;
		recursiveList ( kinds, recurse, retVal, origin ) ;
		return retVal ;
	}

	private void recursiveList ( Set <Kind> kinds, boolean recurse, List <JavaFileObject> retVal, TreeNode n ) {
		if ( n == null ) {
			return ;
		}
		List <TreeNode> children = n.listChildren ( ) ;
		for ( TreeNode child : children ) {
			JavaFileObject candidate = child.getLeaf ( ) ;
			if ( candidate != null ) {
				if ( kinds.contains ( candidate.getKind ( ) ) ) {
					retVal.add ( candidate ) ;
				}
			}
			if ( recurse && child.hasChildren ( ) ) {
				recursiveList ( kinds, recurse, retVal, child ) ;
			}
		}
	}

	@Override
	public String inferBinaryName ( Location location, JavaFileObject file ) {
		return file.getName ( ) ;
	}

	@Override
	public boolean isSameFile ( FileObject a, FileObject b ) {
		// TODO Auto-generated method stub
		System.out.println ( "InMemoryJavaFile: isSameFile: " + a + ", " + b ) ;
		return false ;
	}

	@Override
	public boolean handleOption ( String current, Iterator <String> remaining ) {
		// TODO Auto-generated method stub
		System.out.println ( "InMemoryJavaFile: handleOption: " + current + ", " + remaining ) ;
		return false ;
	}

	@Override
	public boolean hasLocation ( Location location ) {
		// MODULE_SOURCE_PATH
		// ANNOTATION_PROCESSOR_MODULE_PATH, ANNOTATION_PROCESSOR_PATH
		// ANNOTATION_PROCESSOR_MODULE_PATH: com.sun.source.util.Plugin
		// PATCH_MODULE_PATH
		// CLASS_OUTPUT
		// TODO Auto-generated method stub
		TreeNode found = findNode ( location ) ;
		return found != null ;
	}

	@Override
	public JavaFileObject getJavaFileForInput ( Location location, String className, Kind kind ) throws IOException {
		// TODO Auto-generated method stub
		System.out.println ( "InMemoryJavaFile: getJavaFileForInput: " + location + ", " + className + ", " + kind ) ;
		return null ;
	}

	@Override
	public JavaFileObject getJavaFileForOutput ( Location location, String className, Kind kind, FileObject sibling ) throws IOException {
		// TODO Auto-generated method stub
		System.out.println ( "InMemoryJavaFile: getJavaFileForOutput: " + location + ", " + className + ", " + kind + ", " + sibling ) ;
		return null ;
	}

	@Override
	public FileObject getFileForInput ( Location location, String packageName, String relativeName ) throws IOException {
		// TODO Auto-generated method stub
		System.out.println ( "InMemoryJavaFile: getFileForInput: " + location + ", " + packageName + ", " + relativeName ) ;
		return null ;
	}

	@Override
	public FileObject getFileForOutput ( Location location, String packageName, String relativeName, FileObject sibling ) throws IOException {
		// TODO Auto-generated method stub
		System.out.println ( "InMemoryJavaFile: getFileForOutput: " + location + ", " + packageName + ", " + relativeName + ", " + sibling ) ;
		return null ;
	}

	@Override
	public void flush ( ) throws IOException {
		// TODO Auto-generated method stub
		System.out.println ( "Placeholder flush" ) ;
	}

	@Override
	public void close ( ) throws IOException {
		// TODO Auto-generated method stub
		System.out.println ( "Placeholder close" ) ;
	}

	@Override
    public Iterable<Set<Location>> listLocationsForModules(Location location) throws IOException {
		System.out.println ( "location: " + location ) ;
//		throw new UnsupportedOperationException ( ) ;
		return List.of ( ) ;
    }

	@Override
	public Location getLocationForModule(Location location, JavaFileObject fo) throws IOException {
		String name = fo.getName ( ) ;
		String [ ] components = name.split ( "\\." ) ;
		Location retVal = location ;
		for ( String c : components ) {
			retVal = locationOf ( c, retVal ) ;
		}
        return retVal ;
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

	@Override
	public String inferModuleName(Location location) throws IOException {
        return null ; //
    }

	private static final class TreeNode {
		private Location loc ;
		private String name ;
		private Map <Location, TreeNode> children = new LinkedHashMap <> ( ) ;
		private JavaFileObject leaf ;

		public TreeNode ( Location loc ) {
			this ( loc, null ) ;
		}

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

		public List <TreeNode> listChildren ( ) {
			return new ArrayList <> ( children.values ( ) ) ;
		}

		public JavaFileObject getLeaf ( ) {
			return leaf;
		}

		public void setLeaf ( JavaFileObject leaf ) {
			this.leaf = leaf;
		}
	}
}
