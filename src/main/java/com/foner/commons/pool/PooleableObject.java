package com.foner.commons.pool;

/**
 * The interface PooleableObject.
 *
 * @author <a href="mailto:josepdcs@gmail.com">Josep Carbonell</a>
 */
public interface PooleableObject {

	/**
	 * Checks if this objected is pooled.
	 *
	 * @return true, if is pooled
	 */
	boolean isPooled();

	/**
	 * Sets the pooled.
	 *
	 * @param pooled
	 *            the new pooled
	 */
	void setPooled(boolean pooled);

	/**
	 * Releasing resources for the current pooleable object.
	 */
	void releaseResources();
}
