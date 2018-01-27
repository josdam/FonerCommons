package com.foner.commons.cache;

import java.io.Serializable;
import net.sf.ehcache.Ehcache;

/**
 * Cache Service Manager Interface Definition.
 * 
 * @author <a href="mailto:josepdcs@gmail.com">Josep Carbonell</a>
 */
public interface CacheServiceManager extends Serializable {

	/**
	 * Get Cache by Cache's Name.
	 * 
	 * @param cacheName
	 *            cache's name.
	 * @return cache instance.
	 */
	Ehcache getCache(String cacheName);

}
