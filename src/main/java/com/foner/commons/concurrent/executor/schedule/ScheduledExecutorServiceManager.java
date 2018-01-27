package com.foner.commons.concurrent.executor.schedule;

import com.foner.commons.concurrent.executor.ExecutorServiceManager;

/**
 * The Interface ScheduledExecutorServiceManager.
 * 
 * @author <a href="mailto:josepdcs@gmail.com">Josep Carbonell</a>
 */
public interface ScheduledExecutorServiceManager extends ExecutorServiceManager {

	/**
	 * Schedule.
	 */
	void schedule();

}
