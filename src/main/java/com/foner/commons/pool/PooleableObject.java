package com.foner.commons.pool;

/**
 * The interface PooleableObject.
 *
 * @author <a href="mailto:josepdcs@gmail.com">Josep Carbonell</a>
 */
public interface PooleableObject {

	/**
	 * Releasing resources for the current pooleable object.
	 */
	void releaseResources();
}
