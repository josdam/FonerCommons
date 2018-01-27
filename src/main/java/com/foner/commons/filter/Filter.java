package com.foner.commons.filter;

import java.io.Serializable;
import java.util.List;

/**
 * The Interface Filter.
 *
 * @author <a href="mailto:josepdcs@gmail.com">Josep Carbonell</a>
 * @param <T>
 *            the unspecified class
 * @param <S>
 *            the unspecified class
 */
public interface Filter<T, S> extends Serializable {

	/**
	 * Do filter.
	 * 
	 * @param elements
	 *            the elements
	 * @param predicates
	 *            the predicates
	 * @return the filtered list
	 */
	List<T> doFilter(List<T> elements, List<Predicate<S>> predicates);
}
