package com.jumbotours.commons.cache.impl;

import com.jumbotours.commons.cache.ICacheServiceManager;
import java.net.URL;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * Cache Service Manager implementation class.
 * 
 * @author Josep Carbonell
 */
public class CacheServiceManager implements ICacheServiceManager {

	/** The Constant serialVersionUID. */
	private static final long					serialVersionUID	= -7638743888524100750L;

	/** Cache Settings. */
	public final static String					CACHE_SETTINGS		= "ehcache.xml";

	/** Cache Manager. */
	private static CacheManager					cacheManager		= null;

	/** private static instance. */
	private static final ICacheServiceManager	instance			= new CacheServiceManager();

	/** The logger. */
	private static final Logger					logger				= Logger.getLogger(CacheServiceManager.class);

	/**
	 * private constructor.
	 */
	private CacheServiceManager() {}

	/**
	 * Singleton pattern implementation.
	 * 
	 * @return singleton instance.
	 */
	public static ICacheServiceManager getInstance() {
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
				} catch (Exception ex) {
					logger.warn("An error was taken creating Cache Manager: " + ex);
				}
				// Loading from resource.
			} else {
				try {
					cacheManager = CacheManager.create(fileName);
				} catch (Exception ex) {
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
	 * @see com.jumbotours.b2c.core.cache.ICacheServiceManager#getCache(java.lang.String)
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
	 * ShutDown CacheServiceManager.
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
