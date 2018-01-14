package com.foner.commons.cache;

import java.io.Serializable;

/**
 * Cache Service Implementation.
 * 
 * @author <a href="mailto:josepdcs@gmail.com">Josep Carbonell</a>
 */
public interface ICacheService extends Serializable {

	/** ********************* CACHE'S KEY SEPARATOR *************************. */
	/** Cache Composed Key Separator */
	public static final String	CACHE_COMPOSED_KEY_SEPARATOR	= "@";

	/** ********************* CACHE'S OPERATIONS ****************************. */
	/** Register Element Action. */
	public static final String	CACHE_REGISTER_ELEMENT_ACTION	= "register";

	/** Update Element Action. */
	public static final String	CACHE_UPDATE_ELEMENT_ACTION		= "update";

	/** Remove Element Action. */
	public static final String	CACHE_REMOVE_ELEMENT_ACTION		= "remove";

	/** Retrieve Element Action. */
	public static final String	CACHE_RETRIEVE_ELEMENT_ACTION	= "retrieve";

	/**
	 * Gets current cache manager.
	 * 
	 * @return the cache manager
	 */
	ICacheServiceManager getCacheManager();

	/**
	 * Checks if a Cache has cached Elements.
	 * 
	 * @param cacheName
	 *            cache's name.
	 * @return cache size.
	 */
	boolean existsCachedElements(String cacheName);

	/**
	 * Cache Business Delegate Pattern.
	 * 
	 * @param cacheName
	 *            the cache name
	 * @param action
	 *            action to be execute.
	 * @param value
	 *            object to be cached.
	 * @param keys
	 *            individuals keys.
	 * @return object cached or null.
	 */
	Object cache(String cacheName, String action, Object value, String... keys);

	/**
	 * Cache Business Delegate Pattern.
	 * 
	 * @param cacheName
	 *            the cache name
	 * @param action
	 *            action to be execute.
	 * @param value
	 *            object to be cached.
	 * @param key
	 *            key as an object.
	 * @return object cached or null.
	 */
	Object cache(String cacheName, String action, Object value, Object key);
}
