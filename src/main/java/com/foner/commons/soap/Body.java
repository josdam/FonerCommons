package com.foner.commons.soap;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * The class Body.
 *
 * @author <a href="mailto:josepdcs@gmail.com">Josep Carbonell</a>
 */
public class Body {

	/** The fault. */
	@JacksonXmlProperty(namespace = "http://schemas.xmlsoap.org/soap/envelope/", localName = "Fault")
	private Fault	fault;

	/** The entity to be implemented. */
	private Object	entity;

	/**
	 * Gets the fault.
	 *
	 * @return the fault
	 */
	public Fault getFault() {
		return fault;
	}

	/**
	 * Sets the fault.
	 *
	 * @param fault
	 *            the new fault
	 */
	public void setFault(Fault fault) {
		this.fault = fault;
	}

	/**
	 * Gets the entity.
	 *
	 * @return the entity
	 */
	public Object getEntity() {
		return entity;
	}

	/**
	 * Sets the entity.
	 *
	 * @param entity
	 *            the new entity
	 */
	public void setEntity(Object entity) {
		this.entity = entity;
	}

}
