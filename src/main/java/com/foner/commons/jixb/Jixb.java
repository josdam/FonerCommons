package com.foner.commons.jixb;

import com.foner.commons.exception.FonerCommonException;
import com.foner.commons.pool.PooleableObject;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;

/**
 * The class Jixb.
 *
 * @author <a href="mailto:josepdcs@gmail.com">Josep Carbonell</a>
 */
public final class Jixb implements PooleableObject {

	/** The constant UTF-8 */
	private static final String							UTF8				= "UTF-8";

	/** The logger. */
	private static final Logger							logger				= Logger.getLogger(Jixb.class);

	/** The instance. */
	private static final Jixb							instance			= new Jixb();

	/** The binding factory store. */
	private static final Map<Class<?>, IBindingFactory>	bindingFactoryStore	= new ConcurrentHashMap<>();

	/** Indicates if this object is pooled. */
	private boolean										pooled;

	/**
	 * Hides default constructor.
	 */
	private Jixb() {}

	/**
	 * Get the instance following singleton pattern.
	 * 
	 * @return the jaxb instance.
	 */
	public static Jixb getInstance() {
		return instance;
	}

	/**
	 * The new instance.
	 * 
	 * @return the jaxb instance.
	 */
	public static Jixb newInstance() {
		Jixb jixb = new Jixb();
		logger.debug("Instancing new Jixb: " + jixb);
		return jixb;
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
	 *             the fonner common exception
	 */
	@SuppressWarnings("unchecked")
	public <T> T unmarshal(String xml, Class<T> valueType) throws FonerCommonException {
		StringReader reader = null;
		try {
			IBindingFactory bindingFactory = getBindingFactory(valueType);
			IUnmarshallingContext unmarshallingContext = bindingFactory.createUnmarshallingContext();
			reader = new StringReader(xml);
			return (T) unmarshallingContext.unmarshalDocument(reader);
		} catch (JiBXException e) {
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
	 *             the fonner common exception
	 */
	@SuppressWarnings("unchecked")
	public <T> T unmarshal(File file, Class<T> valueType) throws FonerCommonException {
		try {
			return unmarshal(FileUtils.readFileToString(file, UTF8), valueType);
		} catch (IOException ex) {
			throw new FonerCommonException("Unexpected error unmarshalling: ", ex);
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
	 *             the fonner common exception
	 */
	public <T> String marshal(T entity, Class<T> valueType) throws FonerCommonException {
		StringWriter writer = null;

		try {
			IBindingFactory bindingFactory = getBindingFactory(valueType);
			IMarshallingContext marshallingContext = bindingFactory.createMarshallingContext();
			writer = new StringWriter();
			marshallingContext.marshalDocument(entity, UTF8, null, writer);
			return writer.toString();
		} catch (JiBXException e) {
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
	 * @throws JiBXException
	 *             the JiXB exception
	 */
	private synchronized <T> IBindingFactory getBindingFactory(Class<T> valueType) throws JiBXException {
		IBindingFactory bindingFactory = bindingFactoryStore.get(valueType);
		if (bindingFactory == null) {
			bindingFactory = BindingDirectory.getFactory(valueType);
			bindingFactoryStore.put(valueType, bindingFactory);
		}
		return bindingFactory;
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
		bindingFactoryStore.clear();
	}

}
