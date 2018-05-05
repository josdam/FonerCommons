package com.foner.commons.soap;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * The class Envelope.
 *
 * @author <a href="mailto:josepdcs@gmail.com">Josep Carbonell</a>
 */
@JacksonXmlRootElement(namespace = "http://schemas.xmlsoap.org/soap/envelope/", localName = "Envelope")
public class Envelope {

	/** The header. */
	@JacksonXmlProperty(namespace = "http://schemas.xmlsoap.org/soap/envelope/", localName = "Header")
	private Header	header;

	/** The body. */
	@JacksonXmlProperty(namespace = "http://schemas.xmlsoap.org/soap/envelope/", localName = "Body")
	private Body	body;

	/**
	 * Gets the header.
	 *
	 * @return the header
	 */
	public Header getHeader() {
		return header;
	}

	/**
	 * Sets the header.
	 *
	 * @param header
	 *            the new header
	 */
	public void setHeader(Header header) {
		this.header = header;
	}

	/**
	 * Gets the body.
	 *
	 * @return the body
	 */
	public Body getBody() {
		return body;
	}

	/**
	 * Sets the body.
	 *
	 * @param body
	 *            the new body
	 */
	public void setBody(Body body) {
		this.body = body;
	}

}
