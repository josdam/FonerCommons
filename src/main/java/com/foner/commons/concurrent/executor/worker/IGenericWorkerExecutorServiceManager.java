package com.foner.commons.concurrent.executor.worker;

import com.foner.commons.concurrent.executor.IExecutorServiceManager;
import com.foner.commons.concurrent.worker.generic.IGenericWorker;

/**
 * The Interface IGenericWorkerExecutorServiceManager.
 * 
 * @param <T>
 *            the generic type
 * @author <a href="mailto:josepdcs@gmail.com">Josep Carbonell</a>
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
