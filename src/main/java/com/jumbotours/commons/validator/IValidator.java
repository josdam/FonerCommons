package com.jumbotours.commons.validator;

import java.io.Serializable;

/**
 * The Interface IValidator.
 *
 * @author Josep Carbonell
 * @param <T>
 *            the generic type
 */
public interface IValidator<T> extends Serializable {

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
