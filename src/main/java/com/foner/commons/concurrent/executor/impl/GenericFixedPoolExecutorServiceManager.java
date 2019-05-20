package com.foner.commons.concurrent.executor.impl;

import com.foner.commons.concurrent.executor.ExecutorServiceManager;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.commons.lang3.StringUtils;

/**
 * The class GenericFixedPoolExecutorServiceManager.
 *
 * @author <a href="mailto:josep.carbonell@jumbotours.com">Josep Carbonell</a>
 */
public class GenericFixedPoolExecutorServiceManager implements ExecutorServiceManager {

	private static final long					serialVersionUID		= -2103560856589662893L;

	/** The Constant GENERIC_FIXED_POOL_SIXE. */
	public final static String					GENERIC_FIXED_POOL_SIXE	= "generic.fixed.pool.size";

	/** private static instance. */
	private static final ExecutorServiceManager	instance				= new GenericFixedPoolExecutorServiceManager();

	/** The executor service. */
	private ExecutorService						executorService;

	/** The pool size. */
	private int									poolSize				= 10;

	/**
	 * Instantiates a new executor manager.
	 */
	private GenericFixedPoolExecutorServiceManager() {}

	/**
	 * Gets the single instance of ExecutorManager.
	 * 
	 * @return single instance of ExecutorManager
	 */
	public static ExecutorServiceManager getInstance() {
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.foner.commons.concurrent.executor.ExecutorServiceManager#getExecutorService()
	 */
	@Override
	public synchronized ExecutorService getExecutorService() {
		int nThreads = poolSize;
		String poolSizeProperty = System.getProperty(GENERIC_FIXED_POOL_SIXE, String.valueOf(nThreads));
		if (StringUtils.isNotEmpty(poolSizeProperty)) {
			nThreads = Integer.valueOf(poolSizeProperty);
		}
		if (executorService == null || executorService.isShutdown()) {
			executorService = Executors.newFixedThreadPool(nThreads);
		} else if (poolSize != nThreads) { // pool size has chanched, try to shutdown and rebuild
			executorService.shutdownNow();
			executorService = Executors.newFixedThreadPool(nThreads);
		}
		poolSize = nThreads;

		return executorService;
	}

	/**
	 * Gets the pool size.
	 * 
	 * @return the pool size
	 */
	public int getPoolSize() {
		return poolSize;
	}

}
