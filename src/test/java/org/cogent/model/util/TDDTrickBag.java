package org.cogent.model.util;

import java.lang.reflect.InvocationTargetException ;
import java.lang.reflect.Method ;
import java.lang.reflect.Modifier ;
import java.lang.reflect.Parameter ;
import java.util.HashMap ;
import java.util.Map ;
import java.util.Objects ;
import java.util.function.Function ;
import java.util.random.RandomGenerator ;

import com.thedeanda.lorem.Lorem ;
import com.thedeanda.lorem.LoremIpsum ;

public class TDDTrickBag {

	private static Lorem randomWords = new LoremIpsum ( ) ;
	private static RandomGenerator randomNums = RandomGenerator.getDefault ( ) ;

	public static int rollDice ( int min, int max ) {
		return randomNums.nextInt ( min, max + 1 ) ;
	}

	@SafeVarargs
	public static <T> T roll ( T ... ts ) {
		return ts [ rollDice ( 0, ts.length ) ] ;
	}

	public static String words ( int count ) {
		return randomWords.getWords ( count ) ;
	}

	public static <T extends Enum <T>> T roll ( Class <T> x ) {
		return roll ( x.getEnumConstants ( ) ) ;
	}

	public static <T> T setAllTheThings ( T t ) {
		return setAllTheThings ( t, new DefaultSetterContext ( ), defaultSetters ( ) ) ;
	}

	public static <T> T setAllTheThings ( T t, SetterContext ctx ) {
		return setAllTheThings ( t, ctx, defaultSetters ( ) ) ;
	}

	public static <T> T setAllTheThings ( T t, Map <SetterDesc, Function <SetterContext, Object>> generators ) {
		return setAllTheThings ( t, new DefaultSetterContext ( ), generators ) ;
	}

	public static <T> T setAllTheThings ( T t, SetterContext ctx, Map <SetterDesc, Function <SetterContext, Object>> generators ) {
		if ( t == null ) {
			return t ;
		}
		@SuppressWarnings ( "unchecked" )
		Class <T> czz = ( Class <T> ) t.getClass ( ) ;
		setAllTheThings ( t, czz, ctx, generators ) ;
		return t ;
	}

	public static Map <SetterDesc, Function <SetterContext, Object>> defaultSetters ( ) {
		Map <SetterDesc, Function <SetterContext, Object>> retVal = new HashMap <> ( ) ;
		retVal.put ( new SetterDesc ( String.class, null, null ), ctx -> randomWords.getWords ( 1 ) ) ;
		retVal.put ( new SetterDesc ( Boolean.class, null, null ), ctx -> roll ( false, true ) ) ;
		retVal.put ( new SetterDesc ( Boolean.TYPE, null, null ), ctx -> roll ( false, true ) ) ;
		retVal.put ( new SetterDesc ( Integer.class, null, null ), ctx -> roll ( 0, 41625 ) ) ;		
		retVal.put ( new SetterDesc ( Integer.TYPE, null, null ), ctx -> roll ( 0, 41625 ) ) ;		
		retVal.put ( new SetterDesc ( Object.class, null, null ), ctx -> null ) ;		
		return retVal ;
	}

	private static <T> void setAllTheThings ( T t, Class <? super T> czz, SetterContext ctx, Map <SetterDesc, Function <SetterContext, Object>> generators ) {
		if ( czz == null || Objects.equals ( Object.class, czz ) ) {
			return ;
		}
		Method [ ] ms = czz.getDeclaredMethods ( ) ;
		for ( Method m : ms ) {
			if ( isSetter ( m ) ) {
				m.setAccessible ( true ) ;
				Parameter paramToSet = m.getParameters ( ) [ 0 ] ;
				Function <SetterContext, Object> generator = getSetterValueBuilder ( paramToSet, generators ) ;
				Object val = generator.apply ( ctx ) ;
				try {
					m.invoke ( t, val ) ;
				} catch ( IllegalAccessException iae ) {
					throw new RuntimeException ( iae ) ;
				} catch ( RuntimeException re ) {
					throw re ;
				} catch ( InvocationTargetException ite ) {
					throw new RuntimeException ( ite.getTargetException ( ) ) ;
				}
			}
		}
		setAllTheThings ( t, czz.getSuperclass ( ), ctx, generators ) ;
	}

	private static Function <SetterContext, Object> getSetterValueBuilder ( Parameter p, Map <SetterDesc, Function <SetterContext, Object>> generators ) {

		String property = extractPropertyName ( p.getDeclaringExecutable ( ).getName ( ) ) ;
		SetterDesc desc = new SetterDesc ( p.getType ( ), property, p.getName ( ) ) ;
		Function <SetterContext, Object> retVal = generators.get ( desc ) ;
		if ( retVal != null ) {
			return retVal ;
		}
		desc = new SetterDesc ( p.getType ( ), property, null ) ;
		retVal = generators.get ( desc ) ;
		if ( retVal != null ) {
			return retVal ;
		}
		for ( Class <?> type = p.getType ( ) ; type != null ; type = type.getSuperclass ( ) ) {
			desc = new SetterDesc ( type, null, null ) ;
			retVal = generators.get ( desc ) ;
			if ( retVal != null ) {
				return retVal ;
			}
		}
		throw new IllegalArgumentException ( "No setter found for " + p ) ;
	}

	private static String extractPropertyName ( String name ) {
		if ( name.startsWith ( "is" ) ) {
			return camelCase ( name.substring ( "is".length ( ), name.length ( ) ) ) ;
		}
		if ( name.startsWith ( "get" ) ) {
			return camelCase ( name.substring ( "get".length ( ), name.length ( ) ) ) ;
		}
		if ( name.startsWith ( "set" ) ) {
			return camelCase ( name.substring ( "set".length ( ), name.length ( ) ) ) ;
		}		return null ;
	}

	private static String camelCase ( String s ) {
		if ( s == null || s.length ( ) == 0 ) {
			return s ;
		} else if ( s.length ( ) == 1 ) {
			return s.toLowerCase ( ) ;
		} else {
			return Character.toLowerCase ( s.charAt ( 0 ) ) + s.substring ( 1 ) ;
		}
	}

	private static boolean isSetter ( Method m ) {
		if ( ! Modifier.isPublic ( m.getModifiers ( ) ) ) {
			return false ;
		}
		if ( Modifier.isStatic ( m.getModifiers ( ) ) ) {
			return false ;
		}
		if ( ! m.getName ( ).startsWith ( "set" ) ) {
			return false ;
		}
		if ( m.getParameterCount ( ) != 1 ) {
			return false ;
		}
		return true ;
	}

	public static record SetterDesc ( Class <?> czz, String propertyName, String paramName ) { ; }

	public static interface SetterContext {
		public <T> T get ( String name ) ;
		public void set ( String name, Object val ) ;
		public void clear ( ) ;
		public SetterContext child ( ) ;
		public SetterContext parent ( ) ;
	}

	private static class DefaultSetterContext implements SetterContext {

		private SetterContext parent ;
		private Map <String, Object> scope = new HashMap <> ( ) ;

		@SuppressWarnings ( "unchecked" )
		@Override
		public <T> T get ( String name ) {
			T retVal = ( T ) scope.get ( name ) ;
			if ( retVal == null && parent != null ) {
				return parent.get ( name ) ;
			}
			return retVal ;
		}

		@Override
		public void set ( String name, Object val ) {
			scope.put ( name, val ) ;
		}

		@Override
		public void clear ( ) {
			scope.clear ( ) ;
		}

		@Override
		public SetterContext child ( ) {
			DefaultSetterContext ctx = new DefaultSetterContext ( ) ;
			ctx.parent = this ;
			return ctx ;
		}

		@Override
		public SetterContext parent ( ) {
			return parent ;
		}
	}
}
