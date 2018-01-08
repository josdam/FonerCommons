package com.jumbotours.commons.http;

import com.jumbotours.commons.exception.JumboCommonException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.ConnectionPool;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * The class OkHttpClientManager.
 *
 * @author Josep Carbonell
 */
public class OkHttpClientManager {

	/**
	 * The logger.
	 */
	private static final Logger					logger									= Logger.getLogger(OkHttpClientManager.class);

	/**
	 * The DEFAULT_CONNECTION_TIMEOUT.
	 */
	private static final int					DEFAULT_CONNECTION_TIMEOUT				= 5000;

	/**
	 * The DEFAULT_REQUEST_TIMEOUT.
	 */
	private static final int					DEFAULT_REQUEST_TIMEOUT					= 5000;

	/**
	 * The DEFAULT_READ_TIMEOUT.
	 */
	private static final int					DEFAULT_READ_TIMEOUT					= 5000;

	/**
	 * The DEFAULT_MAX_IDLE_CONNECTIONS_POOL.
	 */
	private static final int					DEFAULT_MAX_IDLE_CONNECTIONS_POOL		= 10;

	/**
	 * The DEFAULT_KEEP_ALIVE_DURATION_IN_MINUTES.
	 */
	private static final int					DEFAULT_KEEP_ALIVE_DURATION_IN_MINUTES	= 2;

	/**
	 * The SSL_IGNORE_CERTIFICATE.
	 */
	private static final String					SSL_IGNORE_CERTIFICATE					= System.getProperty("ssl.ignore.certificate");

	/**
	 * The INSTANCE.
	 */
	private static final OkHttpClientManager	instance								= new OkHttpClientManager();

	/**
	 * The initialised.
	 */
	private static boolean						initialised								= false;

	/**
	 * The http client.
	 */
	private static OkHttpClient					httpClient								= null;

	/** The connection timeout. */
	private static int							connectionTimeout						= DEFAULT_CONNECTION_TIMEOUT;

	/** The request timeout. */
	private static int							requestTimeout							= DEFAULT_REQUEST_TIMEOUT;

	/** The read timeout. */
	private static int							readTimeout								= DEFAULT_READ_TIMEOUT;

	/** The max idle connections pool. */
	private static int							maxIdleConnectionsPool					= DEFAULT_MAX_IDLE_CONNECTIONS_POOL;

	/** The keep alive duration in minutes. */
	private static int							keepAliveDurationInMinutes				= DEFAULT_KEEP_ALIVE_DURATION_IN_MINUTES;

	/**
	 * Hides default constructors.
	 */
	private OkHttpClientManager() {}

	/**
	 * Gets the single instance of OkHttpClientManager.
	 *
	 * @return single instance of OkHttpClientManager
	 */
	public static OkHttpClientManager getInstance() {
		if (!initialised) {
			try {
				buildOkHttpClient();
				initialised = true;
			} catch (JumboCommonException ex) {
				logger.fatal(ex, ex);
			}
		}
		return instance;
	}

	/**
	 * Builds the {@link OkHttpClient}}.
	 *
	 * @throws JumboCommonException
	 *             the jumbo common exception
	 */
	private static void buildOkHttpClient() throws JumboCommonException {
		OkHttpClient.Builder builder = new OkHttpClient.Builder();

		if (StringUtils.isNotBlank(SSL_IGNORE_CERTIFICATE) && Boolean.valueOf(SSL_IGNORE_CERTIFICATE)) {
			try {
				// Create a trust manager that does not validate certificate chains
				final TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {

					@Override
					public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {}

					@Override
					public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {}

					@Override
					public java.security.cert.X509Certificate[] getAcceptedIssuers() {
						return new java.security.cert.X509Certificate[] {};
					}
				}};

				// Install the all-trusting trust manager
				final SSLContext sslContext = SSLContext.getInstance("SSL");
				sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
				// Create an ssl socket factory with our all-trusting manager
				final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
				builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
				builder.hostnameVerifier(new HostnameVerifier() {

					@Override
					public boolean verify(String hostname, SSLSession session) {
						return true;
					}

				});
			} catch (NoSuchAlgorithmException | KeyManagementException ex) {
				throw new JumboCommonException("Error building OkHttpClient", ex);
			}
		}

		// setting connection pool configuration
		builder.connectionPool(new ConnectionPool(maxIdleConnectionsPool, keepAliveDurationInMinutes, TimeUnit.MINUTES));

		httpClient = builder.writeTimeout(requestTimeout, TimeUnit.MILLISECONDS).connectTimeout(connectionTimeout, TimeUnit.MILLISECONDS).readTimeout(
				readTimeout, TimeUnit.MILLISECONDS).build();
	}

	/**
	 * Gets the response as bytes.
	 *
	 * @param endPoint
	 *            the end point
	 * @param httpMethodType
	 *            the http method type
	 * @param httpHeaders
	 *            the http headers
	 * @param requestMessage
	 *            the request message
	 * @param mediaType
	 *            the media type
	 * @return the response as bytes
	 * @throws JumboCommonException
	 *             the jumbo common exception
	 */
	public byte[] getResponseAsBytes(String endPoint, HttpMethodType httpMethodType, Map<String, String> httpHeaders, String requestMessage,
			String mediaType) throws JumboCommonException {
		byte[] result = null;
		try (Response response = getResponse(endPoint, httpMethodType, httpHeaders, requestMessage, mediaType)) {
			if (!response.isSuccessful()) {
				logger.warn(String.format("HTTP ERROR - Code: %d, Message: %s", response.code(), response.message()));
			}
			// checking for body
			ResponseBody body = response.body();
			if (body != null) {
				result = body.bytes();
			}
		} catch (IOException | NullPointerException e) {
			throw new JumboCommonException("Error getting http response body", e);
		}

		return result;
	}

	/**
	 * Gets the response as string.
	 *
	 * @param endPoint
	 *            the end point
	 * @param httpMethodType
	 *            the http method type
	 * @param httpHeaders
	 *            the http headers
	 * @param requestMessage
	 *            the request message
	 * @param mediaType
	 *            the media type
	 * @return the response as string
	 * @throws JumboCommonException
	 *             the jumbo common exception
	 */
	public String getResponseAsString(String endPoint, HttpMethodType httpMethodType, Map<String, String> httpHeaders, String requestMessage,
			String mediaType) throws JumboCommonException {
		String result = null;
		try (Response response = getResponse(endPoint, httpMethodType, httpHeaders, requestMessage, mediaType)) {
			if (!response.isSuccessful()) {
				logger.warn(String.format("HTTP ERROR - Code: %d, Message: %s", response.code(), response.message()));
			}
			// checking for body
			ResponseBody body = response.body();
			if (body != null) {
				result = body.string();
			}
		} catch (IOException | NullPointerException e) {
			throw new JumboCommonException("Error getting http response body", e);
		}

		return result;
	}

	/**
	 * Gets the response.
	 *
	 * @param endPoint
	 *            the end point
	 * @param httpMethodType
	 *            the http method type
	 * @param httpHeaders
	 *            the http headers
	 * @param requestMessage
	 *            the request message
	 * @param mediaType
	 *            the media type
	 * @return the response
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws NullPointerException
	 *             the null pointer exception
	 */
	private Response getResponse(String endPoint, HttpMethodType httpMethodType, Map<String, String> httpHeaders, String requestMessage,
			String mediaType) throws IOException, NullPointerException {
		Request request;
		// switching by http method type and building the request instance
		switch (httpMethodType) {
			case DELETE:
				if (StringUtils.isNotEmpty(requestMessage)) {
					request = new Request.Builder().delete(RequestBody.create(MediaType.parse(mediaType), requestMessage)).url(endPoint).build();
				} else {
					request = new Request.Builder().delete().url(endPoint).build();
				}
				break;
			case GET:
				request = new Request.Builder().get().url(endPoint).build();
				break;
			case POST:
				request = new Request.Builder().post(RequestBody.create(MediaType.parse(mediaType), requestMessage)).url(endPoint).build();
				break;
			case PUT:
				request = new Request.Builder().put(RequestBody.create(MediaType.parse(mediaType), requestMessage)).url(endPoint).build();
				break;
			default: // default is HEAD
				request = new Request.Builder().head().build();
				break;
		}
		// adding http headers if is necessary
		if ((httpHeaders != null) && !httpHeaders.isEmpty()) {
			Request.Builder builder = request.newBuilder();
			for (Map.Entry<String, String> entry : httpHeaders.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				builder.addHeader(key, value);
			}
			request = builder.build();
		}
		// calling to server and getting response
		return httpClient.newCall(request).execute();
	}

	/**
	 * Write connection timeout.
	 *
	 * @param timeout
	 *            the timeout
	 * @throws JumboCommonException
	 *             the jumbo common exception
	 */
	public synchronized void writeConnectionTimeout(int timeout) throws JumboCommonException {
		connectionTimeout = timeout;
		buildOkHttpClient();
	}

	/**
	 * Write read timeout.
	 *
	 * @param timeout
	 *            the timeout
	 * @throws JumboCommonException
	 *             the jumbo common exception
	 */
	public synchronized void writeReadTimeout(int timeout) throws JumboCommonException {
		readTimeout = timeout;
		buildOkHttpClient();
	}

	/**
	 * Write request timeout.
	 *
	 * @param timeout
	 *            the timeout
	 * @throws JumboCommonException
	 *             the jumbo common exception
	 */
	public synchronized void writeRequestTimeout(int timeout) throws JumboCommonException {
		requestTimeout = timeout;
		buildOkHttpClient();
	}

	/**
	 * Write pool configuration.
	 *
	 * @param maxIdle
	 *            the max idle
	 * @param keepAliveInMinutes
	 *            the keep alive in minutes
	 */
	public synchronized void writePoolConfiguration(int maxIdle, int keepAliveInMinutes) {
		maxIdleConnectionsPool = maxIdle;
		keepAliveDurationInMinutes = keepAliveInMinutes;
	}

}
