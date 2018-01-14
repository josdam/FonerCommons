package com.foner.commons.converter;

import java.io.Serializable;

/**
 * The Interface IConverter.
 *
 * @author Josep Carbonell <josepdcs@gmail.com>
 * @param <T>
 *            the generic type to be converted
 * @param <S>
 *            the generic type converted
 */
public interface IConverter<T, S> extends Serializable {

	/**
	 * Convert.
	 *
	 * @param t
	 *            the t
	 * @return the s
	 */
	S convert(T t);
}
