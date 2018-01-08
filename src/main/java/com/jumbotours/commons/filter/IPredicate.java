package com.jumbotours.commons.filter;

import java.io.Serializable;

/**
 * The Interface IPredicate.
 *
 * @author Josep Carbonell
 * @param <S>
 *            the unspecified class
 */
public interface IPredicate<S> extends Serializable {

	/**
	 * Do test.
	 * 
	 * @param s
	 *            the s
	 * @return <code>true</code> if test is passed
	 */
	boolean test(S s);
}
