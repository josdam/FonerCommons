package com.jumbotours.commons.concurrent.executor.worker.impl;

import com.jumbotours.commons.concurrent.executor.worker.IGenericWorkerExecutorServiceManager;
import com.jumbotours.commons.concurrent.worker.generic.IGenericWorker;

/**
 * The class AbstractGenericWorkerExecutorServiceManager.
 * 
 * @param <T>
 *            the generic type
 * @author Josep Carbonell
 */
public abstract class AbstractGenericWorkerExecutorServiceManager<T> implements IGenericWorkerExecutorServiceManager<T> {

	/** The Constant serialVersionUID. */
	private static final long	serialVersionUID	= -2704255266522990349L;

	/** The related generic worker. */
	private IGenericWorker<T>	genericWorker;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jumbotours.commons.concurrent.executor.worker.IGenericWorkerExecutorServiceManager#getGenericWorker()
	 */
	@Override
	public IGenericWorker<T> getGenericWorker() {
		return genericWorker;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jumbotours.commons.concurrent.executor.worker.IGenericWorkerExecutorServiceManager#setGenericWorker(com.jumbotours.b2c.core.concurrent.worker.
	 * generic.
	 * IGenericWorker)
	 */
	@Override
	public void setGenericWorker(IGenericWorker<T> genericWorker) {
		this.genericWorker = genericWorker;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jumbotours.commons.concurrent.executor.worker.IGenericWorkerExecutorServiceManager#setGenericWorkerAndSubmit(com.jumbotours.b2c.core.concurrent.
	 * worker.
	 * generic.IGenericWorker)
	 */
	@Override
	public void setGenericWorkerAndSubmit(IGenericWorker<T> genericWorker) {
		setGenericWorker(genericWorker);
		getExecutorService().submit(genericWorker);
	}

}
