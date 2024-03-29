package com.foner.commons.pool;

/**
 * The interface PoolManager.
 *
 * @author <a href="mailto:josepdcs@gmail.com">Josep Carbonell</a>
 * @param <T>
 *            Type of element pooled in this pool manager.
 */
public interface PoolManager<T> {

	/** The Constant DEFAULT_MIN_IDLE. */
	static final int	DEFAULT_MIN_IDLE							= 5;

	/** The Constant DEFAULT_MAX_IDLE. */
	static final int	DEFAULT_MAX_IDLE							= 10;

	/** The Constant DEFAULT_MAX_TOTAL. */
	static final int	DEFAULT_MAX_TOTAL							= 10;

	/** The Constant DEFAULT_MAX_WAIT_MILLIS. */
	static final long	DEFAULT_MAX_WAIT_MILLIS						= 5000;

	/** The Constant DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS. */
	static final long	DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS	= 60000;

	/** The Constant DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS. */
	static final long	DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS		= 120000;

	/**
	 * Gets the pool.
	 * 
	 * @return the pool
	 */
	Pool<T> getPool();

	/**
	 * Reloads pool.
	 */
	void reloadPool();

	/**
	 * Borrow object.
	 *
	 * @return the t
	 */
	T borrowObject();

	/**
	 * Return object.
	 *
	 * @param t
	 *            the t
	 */
	void returnObject(T t);
}
