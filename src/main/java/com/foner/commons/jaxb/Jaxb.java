package com.foner.commons.jaxb;

import com.foner.commons.exception.FonerCommonException;
import com.foner.commons.pool.PooleableObject;
import com.sun.xml.internal.bind.marshaller.NamespacePrefixMapper;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;
import org.apache.log4j.Logger;

/**
 * The class Jaxb.
 *
 * @author <a href="mailto:josepdcs@gmail.com">Josep Carbonell</a>
 */
public final class Jaxb implements PooleableObject {

	/** The constant NAMESPACE_PREFIX_MAPPER_PROPERTY. */
	private static final String						NAMESPACE_PREFIX_MAPPER_PROPERTY	= "com.sun.xml.internal.bind.namespacePrefixMapper";

	/** The constant UTF-8 */
	private static final String						UTF8								= "UTF-8";

	/** The logger. */
	private static final Logger						logger								= Logger.getLogger(Jaxb.class);

	/** The instance. */
	private static final Jaxb						instance							= new Jaxb();

	/** The Constant contextStore. */
	private static final Map<Class<?>, JAXBContext>	contextStore						= new ConcurrentHashMap<>();

	/** The namespace prefix mapper. */
	private NamespacePrefixMapper					namespacePrefixMapper;

	/** Indicates if this object is pooled. */
	private boolean									pooled;

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
		long time = System.currentTimeMillis();
		StringReader reader = null;
		try {
			JAXBContext jaxbContext = getJAXBContext(valueType);
			// creating new unmarshaller because it's not thread safe while JAXBContext it is
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			reader = new StringReader(xml);
			if (isXmlRootElement(valueType)) {
				Object object = jaxbUnmarshaller.unmarshal(reader);
				return (T) object;
			} else {
				JAXBElement<T> jaxbElement = (JAXBElement) jaxbUnmarshaller.unmarshal(new StreamSource(reader), valueType);
				return jaxbElement.getValue();
			}
		} catch (JAXBException e) {
			throw new FonerCommonException("Unexpected error unmarshalling: ", e);
		} finally {
			if (reader != null) {
				reader.close();
			}
			logger.debug("Unmarshal in " + (System.currentTimeMillis() - time) + " ms.");
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
		long time = System.currentTimeMillis();
		try {
			JAXBContext jaxbContext = getJAXBContext(valueType);
			// creating new unmarshaller because it's not thread safe while JAXBContext it is
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			if (isXmlRootElement(valueType)) {
				Object object = jaxbUnmarshaller.unmarshal(file);
				return (T) object;
			} else {
				JAXBElement<T> jaxbElement = (JAXBElement) jaxbUnmarshaller.unmarshal(new StreamSource(file), valueType);
				return jaxbElement.getValue();
			}
		} catch (JAXBException e) {
			throw new FonerCommonException("Unexpected error unmarshalling: ", e);
		} finally {
			logger.debug("Unmarshal in " + (System.currentTimeMillis() - time) + " ms.");
		}
	}

	/**
	 * Checks if is xml root element.
	 *
	 * @param clazz
	 *            the clazz
	 * @return true, if is xml root element
	 */
	private static boolean isXmlRootElement(Class<?> clazz) {
		Annotation[] annotations = clazz.getAnnotations();
		for (Annotation annotation : annotations) {
			if (annotation instanceof XmlRootElement) {
				return true;
			}
		}
		return false;
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
		return marshal(entity, valueType, null);
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
	 * @param rootNamespaceURI
	 *            the root namespace URI
	 * @return the string
	 * @throws FonerCommonException
	 *             the jumbo common exception
	 */
	public <T> String marshal(T entity, Class<T> valueType, String rootNamespaceURI) throws FonerCommonException {
		long time = System.currentTimeMillis();
		StringWriter writer = null;

		try {
			JAXBContext jaxbContext = getJAXBContext(valueType);
			// creating new unmarshaller because it's not thread safe while JAXBContext it is
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, UTF8);
			if (this.namespacePrefixMapper != null) {
				jaxbMarshaller.setProperty(NAMESPACE_PREFIX_MAPPER_PROPERTY, namespacePrefixMapper);
			}
			writer = new StringWriter();
			if (!isXmlRootElement(entity.getClass())) {
				JAXBElement<T> rootElement = new JAXBElement<>(new QName(rootNamespaceURI, valueType.getSimpleName()), valueType, entity);
				jaxbMarshaller.marshal(rootElement, writer);
			} else {
				jaxbMarshaller.marshal(entity, writer);
			}
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
			logger.debug("Marshal in " + (System.currentTimeMillis() - time) + " ms.");
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

	/**
	 * Sets value type.
	 * 
	 * @param valueType
	 *            the value type
	 * @throws JAXBException
	 *             the JAXB exception
	 */
	public void setValueType(Class<?> valueType) throws JAXBException {
		getJAXBContext(valueType);
	}

	/**
	 * Gets the namespace prefix mapper.
	 *
	 * @return the namespace prefix mapper
	 */
	public NamespacePrefixMapper getNamespacePrefixMapper() {
		return namespacePrefixMapper;
	}

	/**
	 * Sets the namespace prefix mapper.
	 *
	 * @param namespacePrefixMapper
	 *            the new namespace prefix mapper
	 */
	public void setNamespacePrefixMapper(NamespacePrefixMapper namespacePrefixMapper) {
		this.namespacePrefixMapper = namespacePrefixMapper;
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
		contextStore.clear();
	}

}
