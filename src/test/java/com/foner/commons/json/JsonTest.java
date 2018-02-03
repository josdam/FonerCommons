package com.foner.commons.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.foner.commons.AbstractTest;
import java.io.IOException;
import org.apache.log4j.Level;
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
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

/**
 * The class JsonTest.
 *
 * @author <a href="mailto:josepdcs@gmail.com">Josep Carbonell</a>
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Json.class, Logger.class})
public class JsonTest extends AbstractTest {

	/** The mock logger. */
	private static Logger	mockLogger;

	/** The mock object mapper. */
	private ObjectMapper	mockObjectMapper;

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
		mockObjectMapper = PowerMockito.mock(ObjectMapper.class);
		mockObjectWriter = PowerMockito.mock(ObjectWriter.class);
	}

	/**
	 * Tear down.
	 */
	@After
	public void tearDown() {}

	/**
	 * Test of getInstance method, of class Json.
	 */
	@Test
	public void testGetInstance() {
		Json json = Json.getInstance();
		MatcherAssert.assertThat(json, Matchers.notNullValue());
		MatcherAssert.assertThat(json, IsInstanceOf.instanceOf(Json.class));
	}

	/**
	 * Test of newInstance method, of class Json.
	 */
	@Test
	public void testNewInstance() {
		Json json = Json.newInstance();
		MatcherAssert.assertThat(json, Matchers.notNullValue());
		MatcherAssert.assertThat(json, IsInstanceOf.instanceOf(Json.class));
		// Mockito.verify(mockLogger, Mockito.times(1)).debug(ArgumentMatchers.anyString());
	}

	/**
	 * Test of deserialize method, of class Json.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void testDeserialize() throws IOException {
		Json json = Json.getInstance();
		Whitebox.setInternalState(json, "mapper", mockObjectMapper);

		Object o = PowerMockito.mock(Object.class);
		PowerMockito.when(mockObjectMapper.readValue(ArgumentMatchers.eq("{OK}"), ArgumentMatchers.any(Class.class))).thenReturn(o);

		Object deserialize = json.deserialize("{OK}", Object.class);
		MatcherAssert.assertThat(deserialize, Matchers.equalTo(o));
		// Mockito.verify(mockLogger, Mockito.times(1)).debug(ArgumentMatchers.anyString());
	}

	/**
	 * Test of serialize method, of class Json.
	 *
	 * @throws JsonProcessingException
	 *             the json processing exception
	 */
	@Test
	public void testSerialize_Object() throws JsonProcessingException {
		Json json = Json.getInstance();
		Whitebox.setInternalState(json, "writer", mockObjectWriter);
		PowerMockito.when(mockObjectWriter.writeValueAsString(ArgumentMatchers.any())).thenReturn("{OK}");

		String returned = json.serialize(ArgumentMatchers.any(Class.class));
		MatcherAssert.assertThat(returned, Matchers.equalTo("{OK}"));

		// Mockito.verify(mockLogger, Mockito.times(1)).debug(ArgumentMatchers.anyString());
		// Mockito.verify(mockLogger, Mockito.times(1)).info(ArgumentMatchers.anyString());

	}

	/**
	 * Test of serialize method, of class Json.
	 */
	@Test
	public void testSerialize_Object_boolean() {

	}

	/**
	 * Test of releaseResources method, of class Json.
	 */
	@Test
	public void testDestroy() {
		Json json = Json.getInstance();
		json.releaseResources();
		// Mockito.verify(mockLogger, Mockito.times(1)).debug(ArgumentMatchers.anyString());
	}

}
