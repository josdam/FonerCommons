package com.foner.commons.http;

import com.foner.commons.exception.JumboCommonException;
import java.io.IOException;
import java.util.Map;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * The class OkHttpClientUtil.
 *
 * @author Josep Carbonell
 */
public final class OkHttpClientUtil {

	/** The logger. */
	private static final Logger logger = Logger.getLogger(OkHttpClientUtil.class);

	/**
	 * Hide default constructor.
	 */
	private OkHttpClientUtil() {}

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
	public static byte[] getResponseAsBytes(String endPoint, HttpMethodType httpMethodType, Map<String, String> httpHeaders, String requestMessage,
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
	public static String getResponseAsString(String endPoint, HttpMethodType httpMethodType, Map<String, String> httpHeaders, String requestMessage,
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
	private static Response getResponse(String endPoint, HttpMethodType httpMethodType, Map<String, String> httpHeaders, String requestMessage,
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
		return OkHttpClientManager.getInstance().getOkHttpClient().newCall(request).execute();
	}
}
