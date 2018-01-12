package com.jumbotours.commons.http;

import com.jumbotours.commons.exception.JumboCommonException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
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
	private static final AtomicBoolean			initialised								= new AtomicBoolean(false);

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
		if (!initialised.getAndSet(true)) {
			buildOkHttpClient();
		}
		return instance;
	}

	/**
	 * Builds the {@link OkHttpClient}}.
	 *
	 * @throws JumboCommonException
	 *             the jumbo common exception
	 */
	private static void buildOkHttpClient() {
		OkHttpClient.Builder builder;
		if (httpClient == null) {
			builder = new OkHttpClient.Builder();
		} else {
			builder = httpClient.newBuilder();
		}

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
				logger.error("Error setting configuration for ingnoring ssl certificates", ex);
			}
		}

		// setting connection pool configuration
		builder.connectionPool(new ConnectionPool(maxIdleConnectionsPool, keepAliveDurationInMinutes, TimeUnit.MINUTES));

		httpClient = builder.writeTimeout(requestTimeout, TimeUnit.MILLISECONDS).connectTimeout(connectionTimeout, TimeUnit.MILLISECONDS).readTimeout(
				readTimeout, TimeUnit.MILLISECONDS).build();
	}

	/**
	 * Gets the OkHttpClient instance.
	 * 
	 * @return the OkHttpClient instance
	 */
	public OkHttpClient getOkHttpClient() {
		return httpClient;
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
	 * @throws JumboCommonException
	 *             the jumbo common exception
	 */
	public synchronized void writePoolConfiguration(int maxIdle, int keepAliveInMinutes) throws JumboCommonException {
		maxIdleConnectionsPool = maxIdle;
		keepAliveDurationInMinutes = keepAliveInMinutes;
		buildOkHttpClient();
	}

}
