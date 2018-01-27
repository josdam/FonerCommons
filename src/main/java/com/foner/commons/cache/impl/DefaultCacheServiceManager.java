package com.foner.commons.cache.impl;

import com.foner.commons.cache.CacheServiceManager;
import java.net.URL;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * Cache Service Manager implementation class.
 * 
 * @author <a href="mailto:josepdcs@gmail.com">Josep Carbonell</a>
 */
public class DefaultCacheServiceManager implements CacheServiceManager {

	/** The Constant serialVersionUID. */
	private static final long					serialVersionUID	= -7638743888524100750L;

	/** Cache Settings. */
	public final static String					CACHE_SETTINGS		= "ehcache.xml";

	/** Cache Manager. */
	private static CacheManager					cacheManager		= null;

	/** private static instance. */
	private static final CacheServiceManager	instance			= new DefaultCacheServiceManager();

	/** The logger. */
	private static final Logger					logger				= Logger.getLogger(CacheServiceManager.class);

	/**
	 * Hides default constructor.
	 */
	private DefaultCacheServiceManager() {}

	/**
	 * Singleton pattern implementation.
	 * 
	 * @return singleton instance.
	 */
	public static CacheServiceManager getInstance() {
		return instance;
	}

	/**
	 * Load Cache Configuration Settings.
	 * 
	 * @param fileName
	 *            the file name
	 */
	public static void loadCacheSettings(URL fileName) {
		String configFile;

		try {
			logger.info("Loading Cache Settings ...");

			// Loading From System Properties.
			configFile = System.getProperty("eh-cache.conf");
			// Checking if exists.
			if (StringUtils.isNotEmpty(configFile)) {
				try {
					cacheManager = CacheManager.create(configFile);
				} catch (CacheException ex) {
					logger.warn("An error was taken creating Cache Manager: " + ex);
				}
				// Loading from resource.
			} else {
				try {
					cacheManager = CacheManager.create(fileName);
				} catch (CacheException ex) {
					logger.warn("An error was taken creating Cache Manager: " + ex);
				}
			}
			logger.info("Cache Settings Loaded ...");
		} catch (Exception ex) {
			logger.error("Cache Settings could not be read: " + ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.foner.commons.cache.CacheServiceManager#getCache(java.lang.String)
	 */
	@Override
	public Ehcache getCache(String cacheName) {
		Ehcache cache = null;

		if (cacheManager.cacheExists(cacheName)) {
			cache = cacheManager.getEhcache(cacheName);
		}

		return cache;

	}

	/**
	 * ShutDown DefaultCacheServiceManager.
	 */
	public static void shutDownCacheManager() {
		cacheManager.shutdown();
		logger.info("Ehcache shutdown.");
	}

	/**
	 * Remove cache elements for given cache name.
	 * 
	 * @param cacheName
	 *            the cache name
	 */
	public static void removeCacheManager(String cacheName) {
		cacheManager.removeCache(cacheName);
	}

	/**
	 * Remove all cache elements.
	 */
	public static void removeAllCacheManager() {
		cacheManager.removeAllCaches();
	}

}
