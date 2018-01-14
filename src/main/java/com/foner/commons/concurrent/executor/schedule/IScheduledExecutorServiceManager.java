package com.foner.commons.concurrent.executor.schedule;

import com.foner.commons.concurrent.executor.IExecutorServiceManager;

/**
 * The Interface IScheduledExecutorServiceManager.
 * 
 * @author Josep Carbonell
 */
public interface IScheduledExecutorServiceManager extends IExecutorServiceManager {

	/**
	 * Schedule.
	 */
	void schedule();

}