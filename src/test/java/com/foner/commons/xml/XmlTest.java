package com.foner.commons.xml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.foner.commons.AbstractTest;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

/**
 * The class XmlTest.
 *
 * @author <a href="mailto:josepdcs@gmail.com">Josep Carbonell</a>
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Xml.class, Logger.class})
public class XmlTest extends AbstractTest {

	/** The mock logger. */
	private static Logger	mockLogger;

	/** The mock object mapper. */
	private XmlMapper		mockObjectMapper;

	/** The mock object writer. */
	private ObjectWriter	mockObjectWriter;

	/**
	 * Sets the up class.
	 */
	@BeforeClass
	public static void setUpClass() {
		mockLogger = PowerMockito.mock(Logger.class);
		// mockLogger.setLevel(Level.DEBUG);
		PowerMockito.mockStatic(Logger.class);
		PowerMockito.when(Logger.getLogger(ArgumentMatchers.any(Class.class))).thenReturn(mockLogger);
	}

	/**
	 * Sets the up.
	 */
	@Before
	public void setUp() {
		mockObjectMapper = PowerMockito.mock(XmlMapper.class);
		mockObjectWriter = PowerMockito.mock(ObjectWriter.class);
	}

	/**
	 * Tear down.
	 */
	@After
	public void tearDown() {}

	/**
	 * Test of getInstance method, of class Xml.
	 */
	@Test
	public void testGetInstance() {
		Xml xml = Xml.getInstance();
		MatcherAssert.assertThat(xml, Matchers.notNullValue());
		MatcherAssert.assertThat(xml, IsInstanceOf.instanceOf(Xml.class));
	}

	/**
	 * Test of newInstance method, of class Xml.
	 */
	@Test
	public void testNewInstance() {
		Xml xml = Xml.newInstance();
		MatcherAssert.assertThat(xml, Matchers.notNullValue());
		MatcherAssert.assertThat(xml, IsInstanceOf.instanceOf(Xml.class));
		// Mockito.verify(mockLogger, Mockito.times(1)).debug(ArgumentMatchers.anyString());
	}

	/**
	 * Test of deserialize method, of class Xml.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void testDeserialize() throws IOException {
		Xml xml = Xml.getInstance();
		Whitebox.setInternalState(xml, "mapper", mockObjectMapper);

		Object o = PowerMockito.mock(Object.class);
		PowerMockito.when(mockObjectMapper.readValue(ArgumentMatchers.eq("{OK}"), ArgumentMatchers.any(Class.class))).thenReturn(o);

		Object deserialize = xml.deserialize("{OK}", Object.class);
		MatcherAssert.assertThat(deserialize, Matchers.equalTo(o));
		// Mockito.verify(mockLogger, Mockito.times(1)).debug(ArgumentMatchers.anyString());
	}

	/**
	 * Test of serialize method, of class Xml. Test deserialize Pojo to XML.
	 *
	 * @throws JsonProcessingException
	 *             the json processing exception
	 */
	@Test
	public void testDeserialize_TestPojo() throws JsonProcessingException, IOException {
		Xml xml = Xml.getInstance();
		File file = new File(getClass().getClassLoader().getResource("com/foner/commons/xml/Test.xml").getFile());
		String xmlString = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
		MatcherAssert.assertThat(xmlString, Matchers.notNullValue());
		logger.info(String.format("Xml String: %s", xmlString));
		TestPojo testPojo = xml.deserialize(xmlString, TestPojo.class);
		MatcherAssert.assertThat(testPojo.getTitle(), Matchers.equalTo("TestPojo"));
		MatcherAssert.assertThat(testPojo.getX(), Matchers.equalTo(1));
		MatcherAssert.assertThat(testPojo.getY(), Matchers.equalTo(2));

	}

	/**
	 * Test of serialize method, of class Xml.
	 *
	 * @throws JsonProcessingException
	 *             the son processing exception
	 */
	@Test
	public void testSerialize_Object() throws JsonProcessingException {
		Xml xml = Xml.getInstance();
		Whitebox.setInternalState(xml, "writer", mockObjectWriter);
		PowerMockito.when(mockObjectWriter.writeValueAsString(ArgumentMatchers.any())).thenReturn("{OK}");

		String returned = xml.serialize(ArgumentMatchers.any(Class.class));
		MatcherAssert.assertThat(returned, Matchers.equalTo("{OK}"));

		// Mockito.verify(mockLogger, Mockito.times(1)).debug(ArgumentMatchers.anyString());
		// Mockito.verify(mockLogger, Mockito.times(1)).info(ArgumentMatchers.anyString());

	}

	/**
	 * Test of serialize method, of class Xml. Test serialize Pojo to XML.
	 *
	 * @throws JsonProcessingException
	 *             the json processing exception
	 */
	@Test
	public void testSerialize_TestPojo() throws JsonProcessingException, IOException {
		Xml xml = Xml.getInstance();
		TestPojo testPojo = new TestPojo();
		testPojo.setTitle("TestPojo");
		testPojo.setX(1);
		testPojo.setY(2);

		String xmlString = xml.serialize(testPojo);
		MatcherAssert.assertThat(xmlString, Matchers.notNullValue());
		MatcherAssert.assertThat(StringUtils.contains(xmlString, "TestPojo"), Matchers.equalTo(true));
		logger.info(String.format("Xml String: %s", xmlString));

	}

	/**
	 * Test of serialize method, of class Xml.
	 */
	@Test
	public void testSerialize_Object_boolean() {

	}

	/**
	 * Test of releaseResources method, of class Xml.
	 */
	@Test
	public void testDestroy() {
		Xml xml = Xml.getInstance();
		xml.releaseResources();
		// Mockito.verify(mockLogger, Mockito.times(1)).debug(ArgumentMatchers.anyString());
	}

}
