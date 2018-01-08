package com.jumbotours.commons.json.parser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.log4j.Logger;

/**
 * The class JsonParser.
 *
 * @author Josep Carbonell
 */
public class JsonParser {

	/** The logger. */
	private static final Logger		logger		= Logger.getLogger(JsonParser.class);

	/** The instance. */
	private static final JsonParser	instance	= new JsonParser();

	/** The object mapper. */
	private ObjectMapper			mapper;

	/** The object writer. */
	private ObjectWriter			writer;

	/** The object pretty writer. */
	private ObjectWriter			prettyWriter;

	/**
	 * Hides default constructor.
	 */
	private JsonParser() {
		init();
	}

	/**
	 * The init.
	 */
	private void init() {
		mapper = new ObjectMapper();
		mapper.findAndRegisterModules();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		// Ignore null values when writing json.
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
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
	 * @return the json parser instance.
	 */
	public static JsonParser getInstance() {
		return instance;
	}

	/**
	 * The new instance.
	 * 
	 * @return the json parser instance.
	 */
	public static JsonParser newInstance() {
		JsonParser jsonParser = new JsonParser();
		logger.debug("Instancing new JsonParser: " + jsonParser);
		return jsonParser;
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
			logger.debug("Deserialize Parser Instance: " + this);
			return mapper.readValue(jsonString, valueType);
		} catch (Exception ex) {
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
			logger.debug("Serialize Parser Instance: " + this);
			if (prettyFormat) {
				return prettyWriter.writeValueAsString(object);
			}
			return writer.writeValueAsString(object);
		} catch (Exception ex) {
			logger.error("Error serializing the given object: ", ex);
		} finally {
			logger.info("Serialize in " + (System.currentTimeMillis() - time) + " ms.");
		}
		return null;
	}
}
