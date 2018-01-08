package com.jumbotours.commons.json.parser.pool;

import com.jumbotours.commons.json.parser.JsonParser;
import com.jumbotours.commons.pool.IPoolManager;
import com.jumbotours.commons.pool.Pool;
import com.jumbotours.commons.pool.SimplePooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * The class JsonParserPoolManagerV2.
 *
 * @author Josep Carbonell
 */
public class JsonParserPoolManager implements IPoolManager<JsonParser> {

	/** The instance. */
	private static final JsonParserPoolManager	instance	= new JsonParserPoolManager();

	/** The config. */
	private GenericObjectPoolConfig				config;

	/** The json parser pool. */
	private Pool<JsonParser>					pool;

	/**
	 * Hides default constructor.
	 */
	private JsonParserPoolManager() {
		readConfiguration();
	}

	/**
	 * Gets the instance following singleton pattern.
	 * 
	 * @return the json parser pool manager instance.
	 */
	public static JsonParserPoolManager getInstance() {
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jumbotours.commons.pool.IPoolManager#getPool()
	 */
	@Override
	public Pool<JsonParser> getPool() {
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
		pool = new Pool<>(new SimplePooledObjectFactory<>(JsonParser.class), config);
	}

}
