package com.foner.commons.converter;

import java.io.Serializable;

/**
 * The Interface Converter.
 *
 * @author <a href="mailto:josepdcs@gmail.com">Josep Carbonell</a>
 * @param <T>
 *            the generic type to be converted
 * @param <S>
 *            the generic type converted
 */
public interface Converter<T, S> extends Serializable {

	/**
	 * Convert.
	 *
	 * @param t
	 *            the t
	 * @return the s
	 */
	S convert(T t);
}
