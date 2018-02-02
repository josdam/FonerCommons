package com.foner.commons.pool;

import java.lang.reflect.Constructor;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.log4j.Logger;

/**
 * The class SimplePooledObjectFactory.
 *
 * @author <a href="mailto:josepdcs@gmail.com">Josep Carbonell</a>
 * @param <T>
 *            Type of element pooled in this pool.
 */
public class SimplePooledObjectFactory<T> extends BasePooledObjectFactory<T> {

	/** The logger. */
	private static final Logger	logger	= Logger.getLogger(SimplePooledObjectFactory.class);

	/** The object. */
	private final Class<T>		object;

	/** The parameters. */
	private Object[]			parameters;

	/**
	 * Instance new SimplePooledObjectFactory with given class to be pooled.
	 * 
	 * @param object
	 *            the object to be pooled
	 */
	public SimplePooledObjectFactory(Class<T> object) {
		this.object = object;
	}

	/**
	 * Instance new SimplePooledObjectFactory with given class to be pooled and parameters needed by the constructor.
	 * 
	 * @param object
	 *            the object to be pooled
	 * @param parameters
	 *            the parameters
	 */
	public SimplePooledObjectFactory(Class<T> object, Object... parameters) {
		this.object = object;
		this.parameters = parameters;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.commons.pool2.BasePooledObjectFactory#create()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public T create() throws Exception {
		T instance = null;
		// checking for sigleton pattern
		for (Constructor constructor : object.getDeclaredConstructors()) {
			if (constructor.getParameterTypes().length == 0) {
				boolean accessible = constructor.isAccessible();
				if (!accessible) {
					constructor.setAccessible(true);
				}
				instance = (T) constructor.newInstance();
				constructor.setAccessible(accessible);
			} else {
				if (ArrayUtils.isNotEmpty(parameters)) {
					instance = (T) constructor.newInstance(parameters);
				}
			}
		}
		logger.debug("Object created: " + instance);
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.commons.pool2.BasePooledObjectFactory#wrap(java.lang.Object)
	 */
	@Override
	public PooledObject<T> wrap(T t) {
		return new DefaultPooledObject<>(t);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.commons.pool2.BasePooledObjectFactory#passivateObject(org.apache.commons.pool2.PooledObject)
	 */
	@Override
	public void passivateObject(PooledObject<T> p) throws Exception {
		// return object to the pool
		super.passivateObject(p);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.commons.pool2.BasePooledObjectFactory#validateObject(org.apache.commons.pool2.PooledObject)
	 */
	@Override
	public boolean validateObject(PooledObject<T> p) {
		return super.validateObject(p);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.commons.pool2.BasePooledObjectFactory#destroyObject(org.apache.commons.pool2.PooledObject)
	 */
	@Override
	public void destroyObject(PooledObject<T> p) throws Exception {
		T t = p.getObject();
		if (t instanceof PooleableObject) {
			logger.debug("PooleableObject will release resources and will be destroyed: " + p);
			((PooleableObject) t).releaseResources();
		}
		super.destroyObject(p);
	}

}
