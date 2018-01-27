package com.foner.commons.concurrent.executor.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.foner.commons.concurrent.executor.ExecutorServiceManager;

/**
 * The class GenericCachedPoolExecutorServiceManager.
 * 
 * @author <a href="mailto:josepdcs@gmail.com">Josep Carbonell</a>
 */
public class GenericCachedPoolExecutorServiceManager implements ExecutorServiceManager {

	/** The Constant serialVersionUID. */
	private static final long					serialVersionUID	= 2506633113561973138L;

	/** private static instance. */
	private static final ExecutorServiceManager	instance			= new GenericCachedPoolExecutorServiceManager();

	/** The executor service. */
	private ExecutorService						executorService;

	/**
	 * Instantiates a new executor manager.
	 */
	private GenericCachedPoolExecutorServiceManager() {}

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
	 * @see com.foner.commons.concurrent.executor.IExecutorManager#getExecutorService()
	 */
	@Override
	public synchronized ExecutorService getExecutorService() {
		if (executorService == null) {
			executorService = Executors.newCachedThreadPool();
		}
		if (executorService.isShutdown()) {
			executorService = Executors.newCachedThreadPool();
		}
		return executorService;
	}
}
