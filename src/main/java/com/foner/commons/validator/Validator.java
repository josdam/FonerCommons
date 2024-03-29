package com.foner.commons.validator;

import java.io.Serializable;

/**
 * The Interface Validator.
 *
 * @author <a href="mailto:josepdcs@gmail.com">Josep Carbonell</a>
 * @param <T>
 *            the generic type
 */
public interface Validator<T> extends Serializable {

	/**
	 * Validate.
	 *
	 * @param t
	 *            the t
	 * @throws Exception
	 *             the exception
	 */
	void validate(T t) throws Exception;
}
