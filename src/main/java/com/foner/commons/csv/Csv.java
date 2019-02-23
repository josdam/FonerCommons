package com.foner.commons.csv;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.foner.commons.pool.PooleableObject;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * The class Csv.
 *
 * @author <a href="mailto:josepdcs@gmail.com">Josep Carbonell</a>
 */
public class Csv implements PooleableObject {

	/** The logger. */
	private static final Logger	logger		= Logger.getLogger(Csv.class);

	/** The instance. */
	private static final Csv	instance	= new Csv();

	/** The object mapper. */
	private CsvMapper			mapper;

	/** The object writer. */
	private ObjectWriter		writer;

	/** The object pretty writer. */
	private ObjectWriter		prettyWriter;

	/** Indicates if this object is pooled. */
	private boolean				pooled;

	/**
	 * Hides default constructor.
	 */
	private Csv() {
		init();
	}

	/**
	 * The init.
	 */
	private void init() {
		mapper = new CsvMapper();
		mapper.findAndRegisterModules();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		mapper.configure(JsonParser.Feature.ALLOW_YAML_COMMENTS, true);
		mapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);

		this.writer = mapper.writer();
		this.prettyWriter = mapper.writerWithDefaultPrettyPrinter();
	}

	/**
	 * Get the instance following singleton pattern.
	 * 
	 * @return the CSV instance.
	 */
	public static Csv getInstance() {
		return instance;
	}

	/**
	 * The new instance.
	 * 
	 * @return the CSV instance.
	 */
	public static Csv newInstance() {
		Csv csv = new Csv();
		logger.debug("Instancing new CSV: " + csv);
		return csv;
	}

	/**
	 * Deserializes CSV content from given CSV content file into given java type.
	 *
	 * @param <T>
	 *            the generic type
	 * @param file
	 *            the file
	 * @param valueType
	 *            the value type
	 * @param columnSeparator
	 *            the column separator
	 * @return the type refer
	 */
	public <T> List<T> deserialize(File file, Class<T> valueType, char columnSeparator) {
		List<T> elements = new ArrayList<>();
		long time = System.currentTimeMillis();
		try {
			CsvSchema schema = mapper.schemaFor(valueType).withColumnSeparator(columnSeparator);
			MappingIterator<T> it = mapper.readerFor(valueType).with(schema).readValues(file);
			elements.addAll(it.readAll());
		} catch (IOException ex) {
			logger.error("Error deserializing the given csv: ", ex);
		} finally {
			logger.info("Deserialize in " + (System.currentTimeMillis() - time) + " ms.");
		}
		return elements;
	}

	/**
	 * Deserializes CSV content from given CSV content String into given java type.
	 *
	 * @param <T>
	 *            the generic type
	 * @param csvString
	 *            the csv string
	 * @param valueType
	 *            the value type
	 * @param columnSeparator
	 *            the column separator
	 * @return the type refer
	 */
	public <T> List<T> deserialize(String csvString, Class<T> valueType, char columnSeparator) {
		List<T> elements = new ArrayList<>();
		long time = System.currentTimeMillis();
		try {
			CsvSchema schema = mapper.schemaFor(valueType);
			MappingIterator<T> it = mapper.readerFor(valueType).with(schema).readValues(csvString);
			elements.addAll(it.readAll());
		} catch (IOException ex) {
			logger.error("Error deserializing the given csv: ", ex);
		} finally {
			logger.info("Deserialize in " + (System.currentTimeMillis() - time) + " ms.");
		}
		return elements;
	}

	/**
	 * Deserializes CSV content from given CSV content file into matrix.
	 *
	 * @param file
	 *            the file
	 * @param columnSeparator
	 *            the column separator
	 * @return the matrix
	 */
	public List<List<String>> deserialize(File file, char columnSeparator) {
		List<List<String>> elements = new ArrayList<>();
		long time = System.currentTimeMillis();
		try {
			CsvSchema schema = CsvSchema.emptySchema();
			mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY);
			MappingIterator<List<String>> it = mapper.readerFor(List.class).with(schema).readValues(file);
			elements.addAll(it.readAll());
		} catch (IOException ex) {
			logger.error("Error deserializing the given csv: ", ex);
		} finally {
			logger.info("Deserialize in " + (System.currentTimeMillis() - time) + " ms.");
		}
		return elements;
	}

	/**
	 * Deserializes CSV content from given CSV content file into matrix.
	 *
	 * @param csvString
	 *            the CVS string
	 * @param columnSeparator
	 *            the column separator
	 * @return the matrix
	 */
	public List<List<String>> deserialize(String csvString, char columnSeparator) {
		List<List<String>> elements = new ArrayList<>();
		long time = System.currentTimeMillis();
		try {
			CsvSchema schema = CsvSchema.emptySchema();
			mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY);
			MappingIterator<List<String>> it = mapper.readerFor(List.class).with(schema).readValues(csvString);
			elements.addAll(it.readAll());
		} catch (IOException ex) {
			logger.error("Error deserializing the given csv: ", ex);
		} finally {
			logger.info("Deserialize in " + (System.currentTimeMillis() - time) + " ms.");
		}
		return elements;
	}

	/**
	 * Serializes any given object to CSV format.
	 *
	 * @param <T>
	 *            the generic type
	 * @param elementsToSerialize
	 *            the elements to serialize
	 * @return the json string
	 */
	public <T> String serialize(List<T> elementsToSerialize, Class<T> valueType) {
		return serialize(elementsToSerialize, valueType, false);
	}

	/**
	 * Serializes any given object to CSV pretty format.
	 *
	 * @param <T>
	 *            the generic type
	 * @param elementsToSerialize
	 *            the elements to serialize
	 * @param prettyFormat
	 *            the pretty flag
	 * @return the CSV string in pretty format
	 */
	public <T> String serialize(List<T> elementsToSerialize, Class<T> valueType, boolean prettyFormat) {
		long time = System.currentTimeMillis();
		try {
			logger.debug("Serialize Parser Instance: " + this);
			CsvSchema schema = mapper.schemaFor(valueType);
			if (prettyFormat) {
				return prettyWriter.with(schema).writeValueAsString(elementsToSerialize);
			}
			return writer.with(schema).writeValueAsString(elementsToSerialize);
		} catch (JsonProcessingException ex) {
			logger.error("Error serializing the given objects: ", ex);
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
