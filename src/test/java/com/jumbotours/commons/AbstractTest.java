package com.jumbotours.commons;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;

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

	/**
	 * Sets the up.
	 */
	@Before
	public void setUp() {}

	/**
	 * Tear down.
	 */
	@After
	public void tearDown() {}
}
