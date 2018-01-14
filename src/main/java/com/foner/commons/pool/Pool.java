package com.foner.commons.pool;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * The class JsonParserPool.
 *
 * @author Josep Carbonell
 * @param <T>
 *            Type of element pooled in this pool.
 */
public class Pool<T> extends GenericObjectPool<T> {

	/**
	 * Instantiates a new json parser pool.
	 *
	 * @param factory
	 *            the factory
	 */
	public Pool(PooledObjectFactory<T> factory) {
		super(factory);
	}

	/**
	 * Instantiates a new json parser pool.
	 *
	 * @param factory
	 *            the factory
	 * @param config
	 *            the config
	 */
	public Pool(PooledObjectFactory<T> factory, GenericObjectPoolConfig config) {
		super(factory, config);
	}

}
