package com.jumbotours.commons.concurrent.executor.schedule;

import com.jumbotours.commons.concurrent.executor.IExecutorServiceManager;

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
