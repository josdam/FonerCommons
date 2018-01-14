package com.foner.commons.http;

/**
 * The enum class HttpMethodType.
 *
 * @author Josep Carbonell
 */
public enum HttpMethodType {

	/** The GET. */
	GET("GET"),

	/** The POST. */
	POST("POST"),

	/** The POST. */
	PUT("PUT"),

	/** The DELETE. */
	DELETE("DELETE"),

	/** The HEAD. */
	HEAD("HEAD"),

	/** The OPTIONS. */
	OPTIONS("OPTIONS"),

	/** The TRACE. */
	TRACE("TRACE"),

	/** The CONNECT. */
	CONNECT("CONNECT");

	/** The http method. */
	private final String httpMethod;

	/**
	 * Instantiates a new <code>HttpMethodType</code>.
	 * 
	 * @param httpMethod
	 *            the http method
	 */
	private HttpMethodType(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return httpMethod;
	}

}
