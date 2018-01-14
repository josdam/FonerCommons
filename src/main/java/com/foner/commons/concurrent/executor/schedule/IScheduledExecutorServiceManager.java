package com.foner.commons.concurrent.executor.schedule;

import com.foner.commons.concurrent.executor.IExecutorServiceManager;

/**
 * The Interface IScheduledExecutorServiceManager.
 * 
 * @author <a href="mailto:josepdcs@gmail.com">Josep Carbonell</a>
 */
public interface IScheduledExecutorServiceManager extends IExecutorServiceManager {

	/**
	 * Schedule.
	 */
	void schedule();

}
