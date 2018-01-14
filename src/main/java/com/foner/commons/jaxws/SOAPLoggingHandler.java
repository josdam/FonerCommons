package com.foner.commons.jaxws;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import org.apache.log4j.Logger;

/**
 * The class SOAPLoggingHandler.
 * 
 * @author Josep Carbonell <josepdcs@gmail.com>
 */
public class SOAPLoggingHandler implements SOAPHandler<SOAPMessageContext> {

	/** The logger. */
	private static final Logger		logger					= Logger.getLogger(SOAPLoggingHandler.class);

	/** The request message. */
	private String					requestMessage;

	/** The response message. */
	private String					responseMessage;

	/** The Constant DEFAULT_CHARACTER_SET. */
	private static final Charset	DEFAULT_CHARACTER_SET	= StandardCharsets.UTF_8;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.xml.ws.handler.soap.SOAPHandler#getHeaders()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Set<QName> getHeaders() {
		return Collections.EMPTY_SET;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.xml.ws.handler.Handler#handleMessage(javax.xml.ws.handler.MessageContext)
	 */
	@Override
	public boolean handleMessage(SOAPMessageContext smc) {
		hadlerMessage(smc);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.xml.ws.handler.Handler#handleFault(javax.xml.ws.handler.MessageContext)
	 */
	@Override
	public boolean handleFault(SOAPMessageContext smc) {
		hadlerMessage(smc);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.xml.ws.handler.Handler#close(javax.xml.ws.handler.MessageContext)
	 */
	@Override
	public void close(MessageContext messageContext) {}

	/**
	 * Gets the request message.
	 * 
	 * @return the message as string
	 */
	public String getRequestMessage() {
		String retVal = requestMessage;
		requestMessage = null;
		return retVal;
	}

	/**
	 * Gets the response message.
	 * 
	 * @return the message as string
	 */
	public String getResponseMessage() {
		String retVal = responseMessage;
		responseMessage = null;
		return retVal;
	}

	/**
	 * Handler message.
	 * 
	 * @param smc
	 *            the soap message context
	 */
	private void hadlerMessage(SOAPMessageContext smc) {
		SOAPMessage message = smc.getMessage();
		Boolean outboundProperty = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

		ByteArrayOutputStream byteArrayOutputStream = null;
		try {
			byteArrayOutputStream = new ByteArrayOutputStream();
			message.writeTo(byteArrayOutputStream);
			if (outboundProperty) {
				requestMessage = byteArrayOutputStream.toString(DEFAULT_CHARACTER_SET.name());
			} else {
				responseMessage = byteArrayOutputStream.toString(DEFAULT_CHARACTER_SET.name());
			}
		} catch (SOAPException | IOException ex) {
			logger.error("SOAP Message could not be written", ex);
		} finally {
			if (byteArrayOutputStream != null) {
				try {
					byteArrayOutputStream.close();
				} catch (IOException ex) {
					logger.error("SOAP Message could not be written", ex);
				}
			}
		}
	}
}
