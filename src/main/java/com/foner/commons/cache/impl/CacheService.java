package com.foner.commons.cache.impl;

import com.foner.commons.cache.ICacheService;
import com.foner.commons.cache.ICacheServiceManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * Cache Service implementation class.
 * 
 * @author Josep Carbonell
 */
public class CacheService implements ICacheService {

	/** The Constant serialVersionUID. */
	private static final long			serialVersionUID	= -4762787449513451881L;

	/** The logger. */
	private static final Logger			logger				= Logger.getLogger(CacheService.class);

	/** private instance. */
	private static final ICacheService	instance			= new CacheService();

	/** Cache Service Manager instance. */
	private final ICacheServiceManager	cacheManager		= CacheServiceManager.getInstance();

	/**
	 * private constructor.
	 */
	private CacheService() {}

	/**
	 * singleton pattern implementation.
	 * 
	 * @return singleton instance.
	 */
	public static ICacheService getInstance() {
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jumbotours.b2c.core.cache.ICacheService#existsCachedElements(java.lang.String)
	 */
	@Override
	public boolean existsCachedElements(String cacheName) {
		Ehcache cache = cacheManager.getCache(cacheName);
		return cache != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jumbotours.b2c.core.cache.ICacheService#cache(java.lang.String, java.lang.String, com.jumbotours.b2c.core.bean.Object,
	 * java.lang.String[])
	 */
	@Override
	public Object cache(String cacheName, String action, Object value, String... keys) {
		// Building The Composed Key.
		String key = buildComposedKey(keys);
		// Executing Cache Action.
		return cacheFacade(cacheName, action, key, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jumbotours.b2c.core.cache.ICacheService#cache(java.lang.String, java.lang.String, com.jumbotours.b2c.core.bean.Object,
	 * java.lang.Object)
	 */
	@Override
	public Object cache(String cacheName, String action, Object value, Object key) {
		// Executing Cache Action.
		return cacheFacade(cacheName, action, key, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jumbotours.b2c.core.cache.ICacheService#getCacheManager()
	 */
	@Override
	public ICacheServiceManager getCacheManager() {
		return cacheManager;
	}

	/**
	 * Cache Action Facade Pattern.
	 * 
	 * @param cacheName
	 *            cache's name.
	 * @param action
	 *            cache action.
	 * @param key
	 *            cached object key.
	 * @param value
	 *            object.
	 * @return object cached or null.
	 */
	private Object cacheFacade(String cacheName, String action, Object key, Object value) {
		// Cached Object
		Object objectCached = null;
		logger.debug("Cache name: " + cacheName);

		// Register Action.
		if (action.equalsIgnoreCase(CACHE_REGISTER_ELEMENT_ACTION)) {
			registerCachedElement(cacheName, key, value);
			logger.debug("Registered key: " + key);
			// Update Action.
		} else if (action.equalsIgnoreCase(CACHE_UPDATE_ELEMENT_ACTION)) {
			updateCachedElement(cacheName, key, value);
			logger.debug("Updated key: " + key);
			// Remove Action.
		} else if (action.equalsIgnoreCase(CACHE_REMOVE_ELEMENT_ACTION)) {
			removeCachedElement(cacheName, key);
			logger.debug("Removed key: " + key);
			// Retrieve Action.
		} else if (action.equalsIgnoreCase(CACHE_RETRIEVE_ELEMENT_ACTION)) {
			long time = System.currentTimeMillis();
			objectCached = getCachedElement(cacheName, key);
			long elapsed = System.currentTimeMillis() - time;
			logger.debug("Retrieved key: " + key + " in " + elapsed + " ms.");
		}

		return objectCached;
	}

	/**
	 * Build a Composed Key.
	 * By default, composed keys are formed as: key1@key2@key3.
	 * 
	 * @param keys
	 *            the keys
	 * @return composed key.
	 */
	private String buildComposedKey(String... keys) {
		StringBuilder builder = null;

		if (keys.length != 0) {
			builder = new StringBuilder();
			for (String key : keys) {
				if (StringUtils.isNotEmpty(key)) {
					builder.append(key);
					builder.append(CACHE_COMPOSED_KEY_SEPARATOR);
				}
			}
		}

		return (builder != null && (builder.length() > 0)) ? builder.substring(0, builder.length() - 1) : null;
	}

	/**
	 * Insert an Element to be Cached.
	 * 
	 * @param cacheName
	 *            cache's name.
	 * @param key
	 *            object's key to be cached.
	 * @param value
	 *            object to be cached.
	 */
	private void registerCachedElement(String cacheName, Object key, Object value) {
		if (cacheManager.getCache(cacheName) == null) {
			logger.warn("Cache manager doesn't exist: " + cacheName);
			return;
		}

		// Checking if the object already exist in Cache.
		Object isObjectCached = getCachedElement(cacheName, key);

		// If not cached: Caching
		if (isObjectCached == null) {
			cacheManager.getCache(cacheName).put(new Element(key, value));
		}

	}

	/**
	 * Update Cached Element.
	 * 
	 * @param cacheName
	 *            cache's name.
	 * @param key
	 *            object's key.
	 * @param value
	 *            object to be cached.
	 */
	private void updateCachedElement(String cacheName, Object key, Object value) {
		if (cacheManager.getCache(cacheName) == null) {
			return;
		}

		// Removing Element.
		removeCachedElement(cacheName, key);

		// Registering as new Element.
		registerCachedElement(cacheName, key, value);
	}

	/**
	 * Remove a Cached Element by Key.
	 * 
	 * @param cacheName
	 *            cache's name.
	 * @param key
	 *            object's key to be deleted.
	 */
	private void removeCachedElement(String cacheName, Object key) {
		if (cacheManager.getCache(cacheName) == null) {
			return;
		}

		// Checking if the object already exist in Cache.
		Object isObjectCached = getCachedElement(cacheName, key);

		if (isObjectCached != null) {
			cacheManager.getCache(cacheName).remove(key);
		}

	}

	/**
	 * Gets a Cached Element.
	 * 
	 * @param cacheName
	 *            cache's name.
	 * @param key
	 *            object's key to be retrieved.
	 * @return cached object.
	 */
	private Object getCachedElement(String cacheName, Object key) {
		if (key == null) {
			logger.warn("Key is null, returning");
			return null;
		}
		if (cacheManager.getCache(cacheName) == null) {
			return null;
		}

		// Retrieving Element from Cache.
		Element cacheElement = cacheManager.getCache(cacheName).get(key);

		// Checking if we retrieved some object.
		if ((cacheElement != null)) {
			// logger.info("Size of cache element: " + cacheElement.getSerializedSize() + " bytes");
			Object value = cacheElement.getObjectValue();
			if (value != null) {
				return value;
			}
		}

		return null;
	}

}
