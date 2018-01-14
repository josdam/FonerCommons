package com.foner.commons.jaxws;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * The class JaxWsUtil.
 * 
 * @author <a href="mailto:josepdcs@gmail.com">Josep Carbonell</a>
 */
public final class JaxwsUtils {

	/** The logger. */
	private static final Logger	logger				= Logger.getLogger(JaxwsUtils.class);

	/** The CONNECT_TIMEOUT constant. */
	public static final String	CONNECT_TIMEOUT_OLD	= "com.sun.xml.internal.ws.connect.timeout";

	/** The REQUEST_TIMEOUT constant. */
	public static final String	REQUEST_TIMEOUT_OLD	= "com.sun.xml.internal.ws.request.timeout";

	/** The CONNECT_TIMEOUT constant. */
	public static final String	CONNECT_TIMEOUT		= "com.sun.xml.ws.connect.timeout";

	/** The REQUEST_TIMEOUT constant. */
	public static final String	REQUEST_TIMEOUT		= "com.sun.xml.ws.request.timeout";

	/**
	 * Hides default constructor.
	 */
	private JaxwsUtils() {}

	/**
	 * Overrides http headers
	 * 
	 * @param httpHeadersParam
	 *            the http headers parameter (form application settings)
	 * @param bindingProvider
	 *            the binding provider
	 */
	public static void overrideHttpHeaders(String httpHeadersParam, BindingProvider bindingProvider) {
		logger.info("HTTP Headers: " + httpHeadersParam);
		Map<String, List<String>> httpHeaders = new HashMap<>();
		if (StringUtils.isNotBlank(httpHeadersParam)) {
			String[] params = StringUtils.split(httpHeadersParam, ";");
			for (String param : params) {
				String header = StringUtils.substringBefore(param, "=");
				String values = StringUtils.substringAfter(param, "=");
				httpHeaders.put(header, Arrays.asList(values));
			}
		}
		bindingProvider.getRequestContext().put(MessageContext.HTTP_REQUEST_HEADERS, httpHeaders);
		// debug code
		@SuppressWarnings("unchecked")
		Map<String, List<String>> headers = (Map<String, List<String>>) bindingProvider.getRequestContext().get(MessageContext.HTTP_REQUEST_HEADERS);
		for (Map.Entry<String, List<String>> entrySet : headers.entrySet()) {
			String key = entrySet.getKey();
			List<String> values = entrySet.getValue();
			logger.info("HTTP Header: " + key);
			logger.info("HTTP Values: " + Arrays.toString(values.toArray()));
		}
	}

	/**
	 * Overrides end point
	 * 
	 * @param endPoint
	 *            the new end-point
	 * @param bindingProvider
	 *            the binding provider
	 */
	public static void overrideEndPoint(String endPoint, BindingProvider bindingProvider) {
		logger.info("End-point: " + endPoint);
		bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endPoint);
	}

	/**
	 * Overrides connection timeout
	 * 
	 * @param seconds
	 *            the new seconds
	 * @param bindingProvider
	 *            the binding provider
	 */
	public static void overrideConnectionTimeout(int seconds, BindingProvider bindingProvider) {
		logger.info("Connection timeout: " + seconds + " seconds");
		bindingProvider.getRequestContext().put(CONNECT_TIMEOUT_OLD, seconds * 1000);
		bindingProvider.getRequestContext().put(CONNECT_TIMEOUT, seconds * 1000);
	}

	/**
	 * Overrides read timeout
	 * 
	 * @param seconds
	 *            the new seconds
	 * @param bindingProvider
	 *            the binding provider
	 */
	public static void overrideReadTimeout(int seconds, BindingProvider bindingProvider) {
		logger.info("Read timeout: " + seconds + " seconds");
		bindingProvider.getRequestContext().put(REQUEST_TIMEOUT_OLD, seconds * 1000);
		bindingProvider.getRequestContext().put(REQUEST_TIMEOUT, seconds * 1000);
	}

	/**
	 * Adds the soap logging handler.
	 * 
	 * @param bindingProvider
	 *            the binding provider
	 * @param soapHandler
	 *            the soap handler
	 */
	public static void addSoapHandler(BindingProvider bindingProvider, SOAPHandler<SOAPMessageContext> soapHandler) {
		Binding binding = bindingProvider.getBinding();
		List<javax.xml.ws.handler.Handler> handlerChain = binding.getHandlerChain();
		handlerChain.add(soapHandler);
		binding.setHandlerChain(handlerChain);
	}

	/**
	 * Gets the request message.
	 * 
	 * @param bindingProvider
	 *            the binding provider
	 * @return the request message
	 */
	public static String getRequestMessage(BindingProvider bindingProvider) {
		String requestMessage = null;
		Binding binding = bindingProvider.getBinding();
		List<javax.xml.ws.handler.Handler> handlers = binding.getHandlerChain();
		for (Handler handler : handlers) {
			if (handler instanceof SOAPLoggingHandler) {
				requestMessage = ((SOAPLoggingHandler) handler).getRequestMessage();
			}
		}
		return requestMessage;
	}

	/**
	 * Gets the response message.
	 * 
	 * @param bindingProvider
	 *            the binding provider
	 * @return the response message
	 */
	public static String getResponseMessage(BindingProvider bindingProvider) {
		String responseMessage = null;
		Binding binding = bindingProvider.getBinding();
		List<javax.xml.ws.handler.Handler> handlers = binding.getHandlerChain();
		for (Handler handler : handlers) {
			if (handler instanceof SOAPLoggingHandler) {
				responseMessage = ((SOAPLoggingHandler) handler).getResponseMessage();
			}
		}
		return responseMessage;
	}

}
