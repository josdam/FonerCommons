package com.foner.commons.soap;

import com.foner.commons.xml.Xml;

/**
 * The class EnvelopeTemplateManager.
 *
 * @author <a href="mailto:josepdcs@gmail.com">Josep Carbonell</a>
 */
public class EnvelopeTemplateManager {

	/**
	 * Gets the envelope.
	 *
	 * @return the envelope
	 */
	public String getEnvelope() {
		Envelope envelope = new Envelope();
		Header header = new Header();
		envelope.setHeader(header);
		Body body = new Body();
		envelope.setBody(body);
		return Xml.getInstance().serialize(envelope);
	}
}
