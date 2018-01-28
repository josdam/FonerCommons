package com.jumbotours.commons;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * The class AbstractTest.
 *
 * @author Josep Carbonell
 */
public abstract class AbstractTest {

	/**
	 * The logger.
	 */
	protected static final Logger logger = Logger.getLogger(AbstractTest.class);

	@BeforeClass
	public static void setUpClass() {}

	@AfterClass
	public static void tearDownClass() {}
}
