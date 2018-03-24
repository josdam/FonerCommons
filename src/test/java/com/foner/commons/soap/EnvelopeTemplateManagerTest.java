package com.foner.commons.soap;

import com.foner.commons.AbstractTest;
import com.foner.commons.xml.Xml;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * The class XmlTest.
 *
 * @author <a href="mailto:josepdcs@gmail.com">Josep Carbonell</a>
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Xml.class})
public class EnvelopeTemplateManagerTest extends AbstractTest {

	@BeforeClass
	public static void setUpClass() {}

	@AfterClass
	public static void tearDownClass() {}

	@Before
	public void setUp() {}

	@After
	public void tearDown() {}

	/**
	 * Test of getEnvelope method, of class EnvelopeTemplateManager.
	 */
	@Test
	public void testGetEnvelope() {
		EnvelopeTemplateManager instance = new EnvelopeTemplateManager();
		logger.info(instance.getEnvelope());
	}

}
