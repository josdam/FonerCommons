package com.jumbotours.commons.concurrent.executor.worker;

import com.jumbotours.commons.concurrent.executor.IExecutorServiceManager;
import com.jumbotours.commons.concurrent.worker.generic.IGenericWorker;

/**
 * The Interface IGenericWorkerExecutorServiceManager.
 * 
 * @param <T>
 *            the generic type
 * @author Josep Carbonell
 */
public interface IGenericWorkerExecutorServiceManager<T> extends IExecutorServiceManager {

	/**
	 * Gets the generic worker.
	 * 
	 * @return generic worker
	 */
	IGenericWorker<T> getGenericWorker();

	/**
	 * Sets the generic worker.
	 * 
	 * @param genericWorker
	 *            the new generic worker
	 */
	void setGenericWorker(IGenericWorker<T> genericWorker);

	/**
	 * Sets the generic worker and submit it.
	 * 
	 * @param genericWorker
	 *            the new generic worker
	 */
	void setGenericWorkerAndSubmit(IGenericWorker<T> genericWorker);
}
