package com.foner.commons.soap;

import com.foner.commons.xml.Xml;
import es.juniper.webservice._2007.CityList;
import es.juniper.webservice._2007.JPCityListRQ;
import es.juniper.webservice._2007.JPLogin;

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
		CityList cityList = new CityList();
		JPCityListRQ cityListRQ = new JPCityListRQ();
		cityListRQ.setVersion("1.1");
		cityListRQ.setLanguage("ES");
		JPLogin login = new JPLogin();
		login.setEmail("XML_JumboTest");
		login.setPassword("9kDgrh6Pr");
		cityListRQ.setLogin(login);

		cityList.setCityListRQ(cityListRQ);
		Body body = new Body();
		body.setEntity(cityList);
		envelope.setBody(body);
		return Xml.getInstance().serialize(envelope);
	}
}
