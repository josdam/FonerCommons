package com.foner.commons.xml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.foner.commons.pool.PooleableObject;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 * The class Xml.
 *
 * @author <a href="mailto:josepdcs@gmail.com">Josep Carbonell</a>
 */
public class Xml implements PooleableObject {

	/** The logger. */
	private static final Logger	logger		= Logger.getLogger(Xml.class);

	/** The instance. */
	private static final Xml	instance	= new Xml();

	/** The object mapper. */
	private XmlMapper			mapper;

	/** The object writer. */
	private ObjectWriter		writer;

	/** The object pretty writer. */
	private ObjectWriter		prettyWriter;

	/** Indicates if this object is pooled. */
	private boolean				pooled;

	/**
	 * Hides default constructor.
	 */
	private Xml() {
		init();
	}

	/**
	 * The init.
	 */
	private void init() {
		mapper = new XmlMapper();
		mapper.findAndRegisterModules();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		// Ignore null values when writing json.
		mapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
		// Don't throw an exception when json has extra fields you are
		// not serializing on. This is useful when you want to use a pojo
		// for deserialization and only care about a portion of the json
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		this.writer = mapper.writer();
		this.prettyWriter = mapper.writerWithDefaultPrettyPrinter();
	}

	/**
	 * Get the instance following singleton pattern.
	 * 
	 * @return the Xml instance.
	 */
	public static Xml getInstance() {
		return instance;
	}

	/**
	 * The new instance.
	 * 
	 * @return the Xml instance.
	 */
	public static Xml newInstance() {
		Xml xml = new Xml();
		logger.debug("Instancing new Xml: " + xml);
		return xml;
	}

	/**
	 * Deserializes XML content from given XML content String into given java type.
	 *
	 * @param <T>
	 *            the generic type
	 * @param xmlString
	 *            the xml string
	 * @param valueType
	 *            the value type
	 * @return the type refer
	 */
	public <T> T deserialize(String xmlString, Class<T> valueType) {
		long time = System.currentTimeMillis();
		try {
			logger.debug("Deserializer Xml Instance: " + this);
			return mapper.readValue(xmlString, valueType);
		} catch (IOException ex) {
			logger.error("Error deserializing the given xml: ", ex);
		} finally {
			logger.info("Deserialize in " + (System.currentTimeMillis() - time) + " ms.");
		}
		return null;
	}

	/**
	 * Serializes any given object to XML format.
	 *
	 * @param object
	 *            the object
	 * @return the json string
	 */
	public String serialize(Object object) {
		return serialize(object, false);
	}

	/**
	 * Serializes any given object to XML pretty format.
	 *
	 * @param object
	 *            the object
	 * @param prettyFormat
	 *            the pretty flag
	 * @return the XML string in pretty format
	 */
	public String serialize(Object object, boolean prettyFormat) {
		long time = System.currentTimeMillis();
		try {
			logger.debug("Serialize Parser Instance: " + this);
			if (prettyFormat) {
				return prettyWriter.writeValueAsString(object);
			}
			return writer.writeValueAsString(object);
		} catch (JsonProcessingException ex) {
			logger.error("Error serializing the given object: ", ex);
		} finally {
			logger.info("Serialize in " + (System.currentTimeMillis() - time) + " ms.");
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.foner.commons.pool.PooleableObject#isPooled()
	 */
	@Override
	public boolean isPooled() {
		return pooled;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.foner.commons.pool.PooleableObject#setPooled(boolean)
	 */
	@Override
	public void setPooled(boolean pooled) {
		this.pooled = pooled;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.foner.commons.pool.PooleableObject#releaseResources()
	 */
	@Override
	public void releaseResources() {
		logger.debug(String.format("Releasing resources for this object: %s", this));
		prettyWriter = null;
		writer = null;
		mapper = null;
	}

}
