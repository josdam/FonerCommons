package com.foner.commons.jaxb.pool;

import com.foner.commons.jaxb.Jaxb;
import com.foner.commons.pool.Pool;
import com.foner.commons.pool.PoolManager;
import com.foner.commons.pool.SimplePooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.log4j.Logger;

/**
 * The class JaxbPoolManager.
 * 
 * <p>
 * Using as follows:
 * </p>
 * 
 * <pre>
 * // borrowing object from pool
 * Jaxb jaxb = JaxbPoolManager.getInstance().borrowObject();
 * ...
 * // returning object to pool
 * JaxbPoolManager.getInstance().returnObject(jaxb);
 * </pre>
 * 
 * @author <a href="mailto:josepdcs@gmail.com">Josep Carbonell</a>
 */
public class JaxbPoolManager implements PoolManager<Jaxb> {

	/** The logger. */
	private static final Logger				logger		= Logger.getLogger(JaxbPoolManager.class);

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
	 * @see com.foner.commons.pool.PoolManager#getPool()
	 */
	@Override
	public Pool<Jaxb> getPool() {
		return pool;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.foner.commons.pool.PoolManager#reloadPool()
	 */
	@Override
	public void reloadPool() {
		if (pool != null) {
			// try to close
			pool.close();
		}
		readConfiguration();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.foner.pool.PoolManager#borrowObject()
	 */
	@Override
	public Jaxb borrowObject() {
		Jaxb jaxb = null;
		try {
			jaxb = pool.borrowObject();
			// client.setPooled(true);
			logger.debug("Borrowed Jaxb from pool: " + jaxb);
		} catch (Exception e) {
			logger.warn("Error getting Jaxb from pool", e);
		}
		return jaxb;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.foner.pool.PoolManager#returnObject(java.lang.Object)
	 */
	@Override
	public void returnObject(Jaxb jaxb) {
		if (jaxb.isPooled()) {
			pool.returnObject(jaxb);
			logger.debug("Returned Jaxb to pool: " + jaxb);
		}
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
