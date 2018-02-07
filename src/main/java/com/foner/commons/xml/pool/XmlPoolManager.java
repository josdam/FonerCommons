package com.foner.commons.xml.pool;

import com.foner.commons.pool.Pool;
import com.foner.commons.pool.PoolManager;
import com.foner.commons.pool.SimplePooledObjectFactory;
import com.foner.commons.xml.Xml;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.log4j.Logger;

/**
 * The class XmlPoolManager.
 * 
 * <p>
 * Using as follows:
 * </p>
 * 
 * <pre>
 * // borrowing object from pool
 * Xml xml = XmlPoolManager.getInstance().borrowObject();
 * ...
 * // returning object to pool
 * XmlPoolManager.getInstance()..returnObject(json);
 * </pre>
 *
 * @author <a href="mailto:josepdcs@gmail.com">Josep Carbonell</a>
 */
public class XmlPoolManager implements PoolManager<Xml> {

	/** The logger. */
	private static final Logger			logger		= Logger.getLogger(XmlPoolManager.class);

	/** The instance. */
	private static final XmlPoolManager	instance	= new XmlPoolManager();

	/** The config. */
	private GenericObjectPoolConfig		config;

	/** The Xml pool. */
	private Pool<Xml>					pool;

	/**
	 * Hides default constructor.
	 */
	private XmlPoolManager() {
		readConfiguration();
	}

	/**
	 * Gets the instance following singleton pattern.
	 * 
	 * @return the json parser pool manager instance.
	 */
	public static XmlPoolManager getInstance() {
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.foner.commons.pool.PoolManager#getPool()
	 */
	@Override
	public Pool<Xml> getPool() {
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
	public Xml borrowObject() {
		Xml xml = null;
		try {
			xml = pool.borrowObject();
			// client.setPooled(true);
			logger.debug("Borrowed Xml from pool: " + xml);
		} catch (Exception e) {
			logger.warn("Error getting Xml from pool", e);
		}
		return xml;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.foner.pool.PoolManager#returnObject(java.lang.Object)
	 */
	@Override
	public void returnObject(Xml xml) {
		if (xml.isPooled()) {
			pool.returnObject(xml);
			logger.debug("Returned Xml to pool: " + xml);
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
		pool = new Pool<>(new SimplePooledObjectFactory<>(Xml.class), config);
	}

}
