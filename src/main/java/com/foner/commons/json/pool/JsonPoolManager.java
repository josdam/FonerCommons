package com.foner.commons.json.pool;

import com.foner.commons.json.Json;
import com.foner.commons.pool.Pool;
import com.foner.commons.pool.PoolManager;
import com.foner.commons.pool.SimplePooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * The class JsonPoolManager.
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
 * @author <a href="mailto:josepdcs@gmail.com">Josep Carbonell</a>
 */
public class JsonPoolManager implements PoolManager<Json> {

	/** The instance. */
	private static final JsonPoolManager	instance	= new JsonPoolManager();

	/** The config. */
	private GenericObjectPoolConfig			config;

	/** The json parser pool. */
	private Pool<Json>						pool;

	/**
	 * Hides default constructor.
	 */
	private JsonPoolManager() {
		readConfiguration();
	}

	/**
	 * Gets the instance following singleton pattern.
	 * 
	 * @return the json parser pool manager instance.
	 */
	public static JsonPoolManager getInstance() {
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.foner.commons.pool.PoolManager#getPool()
	 */
	@Override
	public Pool<Json> getPool() {
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
		pool = new Pool<>(new SimplePooledObjectFactory<>(Json.class), config);
	}

}
