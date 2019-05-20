package com.foner.commons.concurrent.executor.impl;

import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;

/**
 * The class SimpleLogicClass.
 *
 * @author <a href="mailto:josep.carbonell@jumbotours.com">Josep Carbonell</a>
 */
public class SimpleLogicClass {

	/**
	 * The logger.
	 */
	protected static final Logger logger = Logger.getLogger(SimpleLogicClass.class);

	/**
	 * Gets the result.
	 * 
	 * @return the result
	 */
	public String getResult() {
		try {
			TimeUnit.SECONDS.sleep(1);
			logger.info("Running this method...");
		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}
		return "Result of the asynchronous computation";
	}
}
