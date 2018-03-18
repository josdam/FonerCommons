package com.foner.commons.soap;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * The class Body.
 *
 * @author <a href="mailto:josepdcs@gmail.com">Josep Carbonell</a>
 */
public class Body {

	/** The fault. */
	@JacksonXmlProperty(localName = "soap:Fault")
	private Fault fault;

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

}
