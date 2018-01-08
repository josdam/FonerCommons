package com.jumbotours.commons.concurrent.executor.impl;

import com.jumbotours.commons.concurrent.executor.IExecutorServiceManager;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The class GenericCachedPoolExecutorServiceManager.
 * 
 * @author Josep Carbonell
 */
public class GenericCachedPoolExecutorServiceManager implements IExecutorServiceManager {

	/** The Constant serialVersionUID. */
	private static final long						serialVersionUID	= 2506633113561973138L;

	/** private static instance. */
	private static final IExecutorServiceManager	instance			= new GenericCachedPoolExecutorServiceManager();

	/** The executor service. */
	private ExecutorService							executorService;

	/**
	 * Instantiates a new executor manager.
	 */
	private GenericCachedPoolExecutorServiceManager() {}

	/**
	 * Gets the single instance of ExecutorManager.
	 * 
	 * @return single instance of ExecutorManager
	 */
	public static IExecutorServiceManager getInstance() {
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jumbotours.commons.concurrent.executor.IExecutorManager#getExecutorService()
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
