package com.foner.commons.concurrent.executor.worker.impl;

import com.foner.commons.concurrent.executor.worker.GenericWorkerExecutorServiceManager;
import com.foner.commons.concurrent.worker.generic.GenericWorker;

/**
 * The class AbstractGenericWorkerExecutorServiceManager.
 * 
 * @param <T>
 *            the generic type
 * @author <a href="mailto:josepdcs@gmail.com">Josep Carbonell</a>
 */
public abstract class AbstractGenericWorkerExecutorServiceManager<T> implements GenericWorkerExecutorServiceManager<T> {

	/** The Constant serialVersionUID. */
	private static final long	serialVersionUID	= -2704255266522990349L;

	/** The related generic worker. */
	private GenericWorker<T>	genericWorker;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.foner.commons.concurrent.executor.worker.GenericWorkerExecutorServiceManager#getGenericWorker()
	 */
	@Override
	public GenericWorker<T> getGenericWorker() {
		return genericWorker;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.foner.commons.concurrent.executor.worker.GenericWorkerExecutorServiceManager#setGenericWorker(com.jumbotours.b2c.core.concurrent.worker.
	 * generic.
	 * GenericWorker)
	 */
	@Override
	public void setGenericWorker(GenericWorker<T> genericWorker) {
		this.genericWorker = genericWorker;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.foner.commons.concurrent.executor.worker.GenericWorkerExecutorServiceManager#setGenericWorkerAndSubmit(com.jumbotours.b2c.core.concurrent.
	 * worker.
	 * generic.GenericWorker)
	 */
	@Override
	public void setGenericWorkerAndSubmit(GenericWorker<T> genericWorker) {
		setGenericWorker(genericWorker);
		getExecutorService().submit(genericWorker);
	}

}
