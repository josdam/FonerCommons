package com.foner.commons;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * The class AbstractTest.
 *
 * @author <a href="mailto:josepdcs@gmail.com">Josep Carbonell</a>
 */
public abstract class AbstractTest {

	/**
	 * The logger.
	 */
	protected static final Logger logger = Logger.getLogger(AbstractTest.class);

	/**
	 * Sets the up class.
	 */
	@BeforeClass
	public static void setUpClass() {}

	/**
	 * Tear down class.
	 */
	@AfterClass
	public static void tearDownClass() {}
}
