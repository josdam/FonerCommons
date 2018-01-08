package com.jumbotours.commons.http;

import com.google.common.io.ByteStreams;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Map;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * The class HttpClientUtil.
 *
 * @author Josep Carbonell
 */
public final class HttpClientUtil {

	/**
	 * The logger.
	 */
	private static final Logger	logger			= Logger.getLogger(HttpClientUtil.class);

	/**
	 * The Constant GET_METHOD.
	 */
	public static String		GET_METHOD		= "GET";

	/**
	 * The Constant POST_METHOD.
	 */
	public static String		POST_METHOD		= "POST";

	/**
	 * The Constant PUT_METHOD.
	 */
	public static String		PUT_METHOD		= "PUT";

	/**
	 * The Constant DELETE_METHOD.
	 */
	public static String		DELETE_METHOD	= "DELETE";

	/**
	 * The Constant DEFAULT_TIMEOUT.
	 */
	private static final int	DEFAULT_TIMEOUT	= 5000;

	/**
	 * Hide default constructor.
	 */
	private HttpClientUtil() {}

	/**
	 * Do http request.
	 *
	 * @param endPoint
	 *            the end point
	 * @return the array of bytes
	 * @throws Exception
	 *             the jumbo white label platform exception
	 */
	public static byte[] doRequest(String endPoint) throws Exception {
		return doRequest(null, GET_METHOD, endPoint, null, DEFAULT_TIMEOUT, DEFAULT_TIMEOUT, false, null, null);
	}

	/**
	 * Do http request.
	 *
	 * @param request
	 *            the message request
	 * @param endPoint
	 *            the end point
	 * @return the array of bytes
	 * @throws Exception
	 *             the jumbo white label platform exception
	 */
	public static byte[] doRequest(String request, String endPoint) throws Exception {
		return doRequest(request, POST_METHOD, endPoint, null, DEFAULT_TIMEOUT, DEFAULT_TIMEOUT, false, null, null);
	}

	/**
	 * Do request.
	 *
	 * @param request
	 *            the message request
	 * @param requestMethod
	 *            the request method
	 * @param endPoint
	 *            the end point
	 * @param httpHeaders
	 *            the http headers
	 * @param connectionTimeout
	 *            the connection timeout in milliseconds
	 * @param readTimeout
	 *            the read timeout in milliseconds
	 * @param ignoreCertificate
	 *            the ignore certificate
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 * @return the array of bytes
	 * @throws Exception
	 *             the exception
	 */
	public static byte[] doRequest(String request, String requestMethod, String endPoint, Map<String, String> httpHeaders, int connectionTimeout,
			int readTimeout, boolean ignoreCertificate, String username, String password) throws Exception {
		URL url;
		HttpURLConnection httpConnetion;
		HttpsURLConnection httpsConnetion;

		if (StringUtils.isEmpty(endPoint)) {
			throw new Exception("The end-point parameter is mandatory");
		} else {
			logger.info("Trying out: " + endPoint);
		}

		if (StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(password)) {
			setDefaultAuthenticator(username, password);
		}

		// Create connection
		url = new URL(endPoint);
		if (StringUtils.equals(url.getProtocol(), "https")) {
			// Active serveral TLS protocols
			System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
			if (StringUtils.equalsIgnoreCase(System.getProperty("proxy"), Boolean.TRUE.toString())) {
				Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.services.gjt", 3128));
				httpsConnetion = (HttpsURLConnection) url.openConnection(proxy);
			} else {
				httpsConnetion = (HttpsURLConnection) url.openConnection();
			}

			if (ignoreCertificate) {
				// Create a trust manager that does not validate certificate chains
				TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {

					/*
					 * (non-Javadoc)
					 * 
					 * @see javax.net.ssl.X509TrustManager#getAcceptedIssuers()
					 */
					@Override
					public java.security.cert.X509Certificate[] getAcceptedIssuers() {
						return null;
					}

					/*
					 * (non-Javadoc)
					 * 
					 * @see javax.net.ssl.X509TrustManager#checkClientTrusted(java.security.cert.X509Certificate[], java.lang.String)
					 */
					@Override
					public void checkClientTrusted(X509Certificate[] certs, String authType) {}

					/*
					 * (non-Javadoc)
					 * 
					 * @see javax.net.ssl.X509TrustManager#checkServerTrusted(java.security.cert.X509Certificate[], java.lang.String)
					 */
					@Override
					public void checkServerTrusted(X509Certificate[] certs, String authType) {}
				}};

				// Install the all-trusting trust manager
				SSLContext sc = SSLContext.getInstance("SSL");
				sc.init(null, trustAllCerts, new SecureRandom());
				httpsConnetion.setSSLSocketFactory(sc.getSocketFactory());

				// Create all-trusting host name verifier
				HostnameVerifier allHostsValid = new HostnameVerifier() {

					@Override
					public boolean verify(String hostname, SSLSession session) {
						return true;
					}
				};

				// Install the all-trusting host verifier
				HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
			}

			// Get Response
			return getResponseAsArrayBytes(request, requestMethod, httpHeaders, connectionTimeout, readTimeout, httpsConnetion);
		} else {
			if (StringUtils.equalsIgnoreCase(System.getProperty("proxy"), Boolean.TRUE.toString())) {
				Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.services.gjt", 3128));
				httpConnetion = (HttpURLConnection) url.openConnection(proxy);
			} else {
				httpConnetion = (HttpURLConnection) url.openConnection();
			}

			// Get Response
			return getResponseAsArrayBytes(request, requestMethod, httpHeaders, connectionTimeout, readTimeout, httpConnetion);
		}

	}

	/**
	 * Gets the response as array of bytes.
	 *
	 * @param request            the request if is POST
	 * @param requestMethod            the request method
	 * @param httpHeaders            the http headers
	 * @param connectionTimeout the connection timeout
	 * @param readTimeout the read timeout
	 * @param httpURLConnection            the instance of {@link HttpURLConnection}
	 * @return the response as array bytes
	 * @throws Exception             the exception
	 */
	private static byte[] getResponseAsArrayBytes(String request, String requestMethod, Map<String, String> httpHeaders, int connectionTimeout, int readTimeout,
			HttpURLConnection httpURLConnection) throws Exception {
		DataOutputStream dataOutputStream = null;
		InputStream inputStream = null;
		try {
			httpURLConnection.setUseCaches(false);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setConnectTimeout(connectionTimeout);
			httpURLConnection.setReadTimeout(readTimeout);
			httpURLConnection.setRequestMethod(requestMethod);
			if (StringUtils.isEmpty(request) && !StringUtils.equals(requestMethod, DELETE_METHOD)) {
				httpURLConnection.setRequestMethod(GET_METHOD);
			}
			if (httpHeaders != null) {
				for (Map.Entry<String, String> entrySet : httpHeaders.entrySet()) {
					String key = entrySet.getKey();
					String value = entrySet.getValue();
					httpURLConnection.setRequestProperty(key, value);

				}
			}
			if (StringUtils.isNotEmpty(request)) {
				// Send request
				dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
				dataOutputStream.writeBytes(request);
				dataOutputStream.flush();
			}
			// Get Response
			inputStream = dispatch(httpURLConnection);
			return ByteStreams.toByteArray(inputStream);
		} catch (Exception e) {
			throw e;
		} finally {
			if (dataOutputStream != null) {
				try {
					dataOutputStream.close();
				} catch (IOException ex) {
					throw new Exception(ex.getMessage());
				}
			}
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException ex) {
					throw new Exception(ex.getMessage());
				}
			}
			if (httpURLConnection != null) {
				httpURLConnection.disconnect();
			}
		}
	}

	/**
	 * Dispatch from HttpURLConnection.
	 *
	 * @param http the http
	 * @return the input stream
	 * @throws Exception the exception
	 */
	private static InputStream dispatch(HttpURLConnection http) throws Exception {
		try {
			return http.getInputStream();
		} catch (Exception ex) {
			logger.error(ex);
			return http.getErrorStream();
		}
	}

	/**
	 * Just an util method to setup basic http authentication.
	 *
	 * @param username
	 *            the username
	 * @param password
	 *            the passoword
	 */
	public static void setDefaultAuthenticator(String username, String password) {
		Authenticator.setDefault(new HttpBasicAuthenticator(username, password));
	}

	/**
	 * Just an util method in order to convert bytes to String (UTF-8).
	 *
	 * @param bytes
	 *            the bytes to be converted
	 * @return the converted bytes to String
	 * @throws UnsupportedEncodingException
	 *             the unsupported encoding exception
	 */
	public static String toString(byte[] bytes) throws UnsupportedEncodingException {
		return new String(bytes, "UTF-8");
	}
}
