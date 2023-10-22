package org.cogent.util;

/**
 * VariableKey is used in VariableScope to avoid fatfingered keys.  It
 * is meant to be implemented by an enum specific to a test step set.
 */
public interface StrongKey {

	public String name ( ) ;
}
