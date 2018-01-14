package com.foner.commons.http;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

/**
 * The class HttpBasicAuthenticator.
 *
 * @author Josep Carbonell <josepdcs@gmail.com>
 */
public class HttpBasicAuthenticator extends Authenticator {

	/**
	 * The username.
	 */
	private final String	username;

	/**
	 * The password.
	 */
	private final String	password;

	/**
	 * Instantiates a new simple authenticator.
	 *
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 */
	public HttpBasicAuthenticator(String username, String password) {
		this.username = username;
		this.password = password;
	}

	/**
	 * Gets the password authentication.
	 *
	 * @return the password authentication
	 */
	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(username, password.toCharArray());
	}

}
