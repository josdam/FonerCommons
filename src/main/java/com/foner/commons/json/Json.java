package com.foner.commons.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.foner.commons.pool.PooleableObject;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 * The class Json.
 *
 * @author <a href="mailto:josepdcs@gmail.com">Josep Carbonell</a>
 */
public class Json implements PooleableObject {

	/** The logger. */
	private static final Logger	logger		= Logger.getLogger(Json.class);

	/** The instance. */
	private static final Json	instance	= new Json();

	/** The object mapper. */
	private ObjectMapper		mapper;

	/** The object writer. */
	private ObjectWriter		writer;

	/** The object pretty writer. */
	private ObjectWriter		prettyWriter;

	/** Indicates if this object is pooled. */
	private boolean				pooled;

	/**
	 * Hides default constructor.
	 */
	private Json() {
		init();
	}

	/**
	 * The init.
	 */
	private void init() {
		mapper = new ObjectMapper();
		mapper.findAndRegisterModules();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

		this.writer = mapper.writer();
		this.prettyWriter = mapper.writerWithDefaultPrettyPrinter();
	}

	/**
	 * Get the instance following singleton pattern.
	 * 
	 * @return the Json instance.
	 */
	public static Json getInstance() {
		return instance;
	}

	/**
	 * The new instance.
	 * 
	 * @return the Json instance.
	 */
	public static Json newInstance() {
		Json json = new Json();
		logger.debug("Instancing new Json: " + json);
		return json;
	}

	/**
	 * Deserializes JSON content from given JSON content String into given java type.
	 *
	 * @param <T>
	 *            the generic type
	 * @param jsonString
	 *            the json string
	 * @param valueType
	 *            the value type
	 * @return the type refer
	 */
	public <T> T deserialize(String jsonString, Class<T> valueType) {
		long time = System.currentTimeMillis();
		try {
			logger.debug("Deserializer Json Instance: " + this);
			return mapper.readValue(jsonString, valueType);
		} catch (IOException ex) {
			logger.error("Error deserializing the given json: ", ex);
		} finally {
			logger.info("Deserialize in " + (System.currentTimeMillis() - time) + " ms.");
		}
		return null;
	}

	/**
	 * Serializes any given object to JSON format.
	 *
	 * @param object
	 *            the object
	 * @return the json string
	 */
	public String serialize(Object object) {
		return serialize(object, false);
	}

	/**
	 * Serializes any given object to JSON pretty format.
	 *
	 * @param object
	 *            the object
	 * @param prettyFormat
	 *            the pretty flag
	 * @return the json string in pretty format
	 */
	public String serialize(Object object, boolean prettyFormat) {
		long time = System.currentTimeMillis();
		try {
			logger.debug("Serializer Json Instance: " + this);
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
