package com.foner.commons.soap;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * The class Envelope.
 *
 * @author <a href="mailto:josepdcs@gmail.com">Josep Carbonell</a>
 */
@JacksonXmlRootElement(localName = "soap:Envelope")
public class Envelope {

	/** The soap. */
	@JacksonXmlProperty(isAttribute = true, localName = "xmlns:soap")
	private String	soap			= "http://www.w3.org/2003/05/soap-envelope/";

	/** The encoding style. */
	@JacksonXmlProperty(isAttribute = true, localName = "soap:encodingStyle")
	private String	encodingStyle	= "http://www.w3.org/2003/05/soap-encoding";

	/** The header. */
	@JacksonXmlProperty(localName = "soap:Header")
	private Header	header;

	/** The body. */
	@JacksonXmlProperty(localName = "soap:Body")
	private Body	body;

	/**
	 * Gets the soap.
	 *
	 * @return the soap
	 */
	public String getSoap() {
		return soap;
	}

	/**
	 * Sets the soap.
	 *
	 * @param soap
	 *            the new soap
	 */
	public void setSoap(String soap) {
		this.soap = soap;
	}

	/**
	 * Gets the encoding style.
	 *
	 * @return the encoding style
	 */
	public String getEncodingStyle() {
		return encodingStyle;
	}

	/**
	 * Sets the encoding style.
	 *
	 * @param encodingStyle
	 *            the new encoding style
	 */
	public void setEncodingStyle(String encodingStyle) {
		this.encodingStyle = encodingStyle;
	}

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
