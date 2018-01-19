package com.foner.commons.jaxb;

import com.foner.commons.exception.FonerCommonException;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.apache.log4j.Logger;

/**
 * The class Jaxb.
 *
 * @author <a href="mailto:josepdcs@gmail.com">Josep Carbonell</a>
 */
public final class Jaxb {

	/** The logger. */
	private static final Logger						logger			= Logger.getLogger(Jaxb.class);

	/** The instance. */
	private static final Jaxb						instance		= new Jaxb();

	/** The Constant contextStore. */
	private static final Map<Class<?>, JAXBContext>	contextStore	= new ConcurrentHashMap<>();

	/**
	 * Hides default constructor.
	 */
	private Jaxb() {}

	/**
	 * Get the instance following singleton pattern.
	 * 
	 * @return the jaxb instance.
	 */
	public static Jaxb getInstance() {
		return instance;
	}

	/**
	 * The new instance.
	 * 
	 * @return the jaxb instance.
	 */
	public static Jaxb newInstance() {
		Jaxb jaxb = new Jaxb();
		logger.debug("Instancing new Jaxb: " + jaxb);
		return jaxb;
	}

	/**
	 * Unmarshal.
	 *
	 * @param <T>
	 *            the generic type
	 * @param xml
	 *            the xml
	 * @param valueType
	 *            the value type
	 * @return the t
	 * @throws FonerCommonException
	 *             the jumbo common exception
	 */
	@SuppressWarnings("unchecked")
	public <T> T unmarshal(String xml, Class<T> valueType) throws FonerCommonException {
		StringReader reader = null;
		try {
			JAXBContext jaxbContext = getJAXBContext(valueType);
			// creating new unmarshaller because it's not thread safe while JAXBContext it is
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			reader = new StringReader(xml);
			return (T) jaxbUnmarshaller.unmarshal(reader);
		} catch (JAXBException e) {
			throw new FonerCommonException("Unexpected error unmarshalling: ", e);
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}

	/**
	 * Unmarshal.
	 *
	 * @param <T>
	 *            the generic type
	 * @param file
	 *            the file
	 * @param valueType
	 *            the value type
	 * @return the t
	 * @throws FonerCommonException
	 *             the jumbo common exception
	 */
	@SuppressWarnings("unchecked")
	public <T> T unmarshal(File file, Class<T> valueType) throws FonerCommonException {
		try {
			JAXBContext jaxbContext = getJAXBContext(valueType);
			// creating new unmarshaller because it's not thread safe while JAXBContext it is
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			return (T) jaxbUnmarshaller.unmarshal(file);
		} catch (JAXBException e) {
			throw new FonerCommonException("Unexpected error unmarshalling: ", e);
		}
	}

	/**
	 * Marshal.
	 *
	 * @param <T>
	 *            the generic type
	 * @param entity
	 *            the entity
	 * @param valueType
	 *            the value type
	 * @return the string
	 * @throws FonerCommonException
	 *             the jumbo common exception
	 */
	public <T> String marshal(T entity, Class<T> valueType) throws FonerCommonException {
		StringWriter writer = null;

		try {
			JAXBContext jaxbContext = getJAXBContext(valueType);
			// creating new unmarshaller because it's not thread safe while JAXBContext it is
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, StandardCharsets.UTF_8);
			writer = new StringWriter();
			jaxbMarshaller.marshal(entity, writer);
			return writer.toString();
		} catch (JAXBException e) {
			throw new FonerCommonException("Unexpected error marshalling: ", e);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					throw new FonerCommonException("Unexpected error closing StringWriter: ", e);
				}
			}
		}
	}

	/**
	 * Gets the JAXB context.
	 *
	 * @param <T>
	 *            the generic type
	 * @param valueType
	 *            the value type
	 * @return the JAXB context
	 * @throws JAXBException
	 *             the JAXB exception
	 */
	private synchronized <T> JAXBContext getJAXBContext(Class<T> valueType) throws JAXBException {
		JAXBContext jaxbContext = contextStore.get(valueType);
		if (jaxbContext == null) {
			jaxbContext = JAXBContext.newInstance(valueType);
			contextStore.put(valueType, jaxbContext);
		}
		return jaxbContext;
	}

}
