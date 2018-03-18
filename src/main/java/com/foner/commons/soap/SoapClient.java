package com.foner.commons.soap;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * The class SoapClient, based in code from {@link https://gist.github.com/kdelfour/b2a449a1bac23e3baec8}
 *
 * @author <a href="mailto:josepdcs@gmail.com">Josep Carbonell</a>
 */
public class SoapClient {

	/** The Constant logger. */
	private static final Logger	logger				= Logger.getLogger(SoapClient.class);

	/** The uri SOAP server. */
	private String				uriSOAPServer;

	/** The soap connection. */
	private SOAPConnection		soapConnection		= null;

	/** The Constant PREFIX_NAMESPACE. */
	private static final String	PREFIX_NAMESPACE	= "ns";

	/** The Constant NAMESPACE. */
	private static final String	NAMESPACE			= "http://other.namespace.to.add.to.header";

	/**
	 * A constructor who create a SOAP connection.
	 *
	 * @param url
	 *            the SOAP server URI
	 */
	public SoapClient(final String url) {
		this.uriSOAPServer = url;

		try {
			createSOAPConnection();
		} catch (UnsupportedOperationException | SOAPException e) {
			logger.error(e);
		}
	}

	/**
	 * Send a SOAP request for a specific operation.
	 *
	 * @param xmlRequestBody
	 *            the body of the SOAP message
	 * @param operation
	 *            the operation from the SOAP server invoked
	 * @return a response from the server
	 * @throws SOAPException
	 *             the SOAP exception
	 * @throws SAXException
	 *             the SAX exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws ParserConfigurationException
	 *             the parser configuration exception
	 */
	public String sendMessageToSOAPServer(String xmlRequestBody,
			String operation) throws SOAPException, SAXException, IOException, ParserConfigurationException {

		// Send SOAP Message to SOAP Server
		final SOAPElement stringToSOAPElement = stringToSOAPElement(xmlRequestBody);
		final SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(stringToSOAPElement, operation), uriSOAPServer);

		// Print SOAP Response
		logger.info("Response SOAP Message : " + soapResponse.toString());
		return soapResponse.toString();
	}

	/**
	 * Create a SOAP connection.
	 *
	 * @throws UnsupportedOperationException
	 *             the unsupported operation exception
	 * @throws SOAPException
	 *             the SOAP exception
	 */
	private void createSOAPConnection() throws UnsupportedOperationException, SOAPException {
		// Create SOAP Connection
		SOAPConnectionFactory soapConnectionFactory;
		soapConnectionFactory = SOAPConnectionFactory.newInstance();
		soapConnection = soapConnectionFactory.createConnection();
	}

	/**
	 * Create a SOAP request.
	 *
	 * @param body
	 *            the body of the SOAP message
	 * @param operation
	 *            the operation from the SOAP server invoked
	 * @return the SOAP message request completed
	 * @throws SOAPException
	 *             the SOAP exception
	 */
	private SOAPMessage createSOAPRequest(SOAPElement body, String operation) throws SOAPException {

		final MessageFactory messageFactory = MessageFactory.newInstance();
		final SOAPMessage soapMessage = messageFactory.createMessage();
		final SOAPPart soapPart = soapMessage.getSOAPPart();

		// SOAP Envelope
		final SOAPEnvelope envelope = soapPart.getEnvelope();
		envelope.addNamespaceDeclaration(PREFIX_NAMESPACE, NAMESPACE);

		// SOAP Body
		final SOAPBody soapBody = envelope.getBody();
		soapBody.addChildElement(body);

		// Mime Headers
		final MimeHeaders headers = soapMessage.getMimeHeaders();
		logger.info("SOAPAction : " + uriSOAPServer + operation);
		headers.addHeader("SOAPAction", uriSOAPServer + operation);

		soapMessage.saveChanges();

		/* Print the request message */
		logger.info("Request SOAP Message :" + soapMessage.toString());
		return soapMessage;
	}

	/**
	 * Transform a String to a SOAP element.
	 *
	 * @param xmlRequestBody
	 *            the string body representation
	 * @return a SOAP element
	 * @throws SOAPException
	 *             the SOAP exception
	 * @throws SAXException
	 *             the SAX exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws ParserConfigurationException
	 *             the parser configuration exception
	 */
	private SOAPElement stringToSOAPElement(String xmlRequestBody) throws SOAPException, SAXException, IOException, ParserConfigurationException {

		// Load the XML text into a DOM Document
		final DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		builderFactory.setNamespaceAware(true);
		final InputStream stream = new ByteArrayInputStream(xmlRequestBody.getBytes());
		final Document doc = builderFactory.newDocumentBuilder().parse(stream);

		// Use SAAJ to convert Document to SOAPElement
		// Create SoapMessage
		final MessageFactory msgFactory = MessageFactory.newInstance();
		final SOAPMessage message = msgFactory.createMessage();
		final SOAPBody soapBody = message.getSOAPBody();

		// This returns the SOAPBodyElement that contains ONLY the Payload
		return soapBody.addDocument(doc);
	}
}
