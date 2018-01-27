package com.foner.commons.concurrent.executor.worker;

import com.foner.commons.concurrent.executor.ExecutorServiceManager;
import com.foner.commons.concurrent.worker.generic.GenericWorker;

/**
 * The Interface GenericWorkerExecutorServiceManager.
 * 
 * @param <T>
 *            the generic type
 * @author <a href="mailto:josepdcs@gmail.com">Josep Carbonell</a>
 */
public interface GenericWorkerExecutorServiceManager<T> extends ExecutorServiceManager {

	/**
	 * Gets the generic worker.
	 * 
	 * @return generic worker
	 */
	GenericWorker<T> getGenericWorker();

	/**
	 * Sets the generic worker.
	 * 
	 * @param genericWorker
	 *            the new generic worker
	 */
	void setGenericWorker(GenericWorker<T> genericWorker);

	/**
	 * Sets the generic worker and submit it.
	 * 
	 * @param genericWorker
	 *            the new generic worker
	 */
	void setGenericWorkerAndSubmit(GenericWorker<T> genericWorker);
}
