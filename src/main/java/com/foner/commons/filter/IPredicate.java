package com.foner.commons.filter;

import java.io.Serializable;

/**
 * The Interface IPredicate.
 *
 * @author <a href="mailto:josepdcs@gmail.com">Josep Carbonell</a>
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
