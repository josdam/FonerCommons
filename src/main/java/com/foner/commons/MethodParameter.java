package com.foner.commons;

import java.io.Serializable;

/**
 * The class MethodParameter.
 * 
 * @author <a href="mailto:josepdcs@gmail.com">Josep Carbonell</a>
 */
public class MethodParameter implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long	serialVersionUID	= 1380222922781877717L;

	/** The type. */
	private Class<?>			type;

	/** The value. */
	private Object				value;

	/** The values. */
	private Object[]			values;

	/**
	 * Instantiates a new method parameter.
	 * 
	 * @param type
	 *            the type
	 * @param value
	 *            the value
	 */
	public MethodParameter(Class<?> type, Object value) {
		this.type = type;
		this.value = value;
	}

	/**
	 * Instantiates a new method parameter.
	 * 
	 * @param type
	 *            the type
	 * @param values
	 *            the values
	 */
	public MethodParameter(Class<?> type, Object[] values) {
		this.type = type;
		this.values = values;
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public Class<?> getType() {
		return type;
	}

	/**
	 * Sets the type.
	 * 
	 * @param type
	 *            the new type
	 */
	public void setType(Class<?> type) {
		this.type = type;
	}

	/**
	 * Gets the value.
	 * 
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Sets the value.
	 * 
	 * @param value
	 *            the new value
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * Gets the values.
	 * 
	 * @return the values
	 */
	public Object[] getValues() {
		return values;
	}

	/**
	 * Sets the values.
	 * 
	 * @param values
	 *            the new values
	 */
	public void setValues(Object[] values) {
		this.values = values;
	}

}
