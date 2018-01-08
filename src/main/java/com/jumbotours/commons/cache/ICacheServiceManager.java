package com.jumbotours.commons.cache;

import java.io.Serializable;
import net.sf.ehcache.Ehcache;

/**
 * Cache Service Manager Interface Definition.
 * 
 * @author Josep Carbonell
 */
public interface ICacheServiceManager extends Serializable {

	/**
	 * Get Cache by Cache's Name.
	 * 
	 * @param cacheName
	 *            cache's name.
	 * @return cache instance.
	 */
	Ehcache getCache(String cacheName);

}
