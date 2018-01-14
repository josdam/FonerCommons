package com.foner.commons.jaxb.pool;

import com.foner.commons.jaxb.Jaxb;
import com.foner.commons.pool.IPoolManager;
import com.foner.commons.pool.Pool;
import com.foner.commons.pool.SimplePooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * The class JaxbPoolManager.
 * 
 * <p>
 * Using as follows:
 * </p>
 * 
 * <pre>
 * // borrowing object from pool
 * Json json = JsonPoolManager.getInstance().getPool().borrowObject();
 * ...
 * // returning object to pool
 * JsonPoolManager.getInstance().getPool().returnObject(json);
 * </pre>
 * 
 * @author Josep Carbonell <josepdcs@gmail.com>
 */
public class JaxbPoolManager implements IPoolManager<Jaxb> {

	/** The instance. */
	private static final JaxbPoolManager	instance	= new JaxbPoolManager();

	/** The config. */
	private GenericObjectPoolConfig			config;

	/** The jaxb parser pool. */
	private Pool<Jaxb>						pool;

	/**
	 * Hides default constructor.
	 */
	private JaxbPoolManager() {
		readConfiguration();
	}

	/**
	 * Gets the instance following singleton pattern.
	 * 
	 * @return the jaxb parser pool manager instance.
	 */
	public static JaxbPoolManager getInstance() {
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jumbotours.commons.pool.IPoolManager#getPool()
	 */
	@Override
	public Pool<Jaxb> getPool() {
		return pool;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jumbotours.commons.pool.IPoolManager#reloadPool()
	 */
	@Override
	public void reloadPool() {
		if (pool != null) {
			// try to close
			pool.close();
		}
		readConfiguration();
	}

	/**
	 * Read configuration.
	 */
	private void readConfiguration() {
		config = new GenericObjectPoolConfig();
		config.setMinIdle(DEFAULT_MIN_IDLE);
		config.setMaxIdle(DEFAULT_MAX_IDLE);
		config.setMaxTotal(DEFAULT_MAX_TOTAL);
		config.setMaxWaitMillis(DEFAULT_MAX_WAIT_MILLIS);
		config.setTimeBetweenEvictionRunsMillis(DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS);
		config.setMinEvictableIdleTimeMillis(DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS);
		pool = new Pool<>(new SimplePooledObjectFactory<>(Jaxb.class), config);
	}

}
