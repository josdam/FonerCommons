package com.foner.commons.filter;

import java.io.Serializable;
import java.util.List;

/**
 * The Interface IFilter.
 *
 * @author Josep Carbonell <josepdcs@gmail.com>
 * @param <T>
 *            the unspecified class
 * @param <S>
 *            the unspecified class
 */
public interface IFilter<T, S> extends Serializable {

	/**
	 * Do filter.
	 * 
	 * @param elements
	 *            the elements
	 * @param predicates
	 *            the predicates
	 * @return the filtered list
	 */
	List<T> doFilter(List<T> elements, List<IPredicate<S>> predicates);
}
