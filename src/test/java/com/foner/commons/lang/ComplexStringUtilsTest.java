package com.foner.commons.lang;

import com.foner.commons.AbstractTest;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;
import org.apache.log4j.Logger;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * The class ComplexStringUtilsTest.
 *
 * @author <a href="mailto:josepdcs@gmail.com">Josep Carbonell</a>
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ComplexStringUtils.class, Logger.class})
public class ComplexStringUtilsTest extends AbstractTest {

	/**
	 * Instantiates a new complex string utils test.
	 */
	public ComplexStringUtilsTest() {}

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

	/**
	 * Test of splitToMap method, of class ComplexStringUtils.
	 */
	@Test
	public void testSplitToMap() {
		String str = "Key1=Value1;Key2=Value2";
		String separatorChars = ";";
		Map<String, String> result = ComplexStringUtils.splitToMap(str, separatorChars);
		Assert.assertEquals(2, result.size());
		Assert.assertEquals("Value1", result.get("Key1"));
		Assert.assertEquals("Value2", result.get("Key2"));
	}

	/**
	 * Test of splitToMap method, of class ComplexStringUtils.
	 */
	@Test
	public void testSplitToMap_emptySeparator() {
		String str = "Key1=Value1 Key2=Value2";
		Map<String, String> result = ComplexStringUtils.splitToMap(str, null);
		Assert.assertEquals(2, result.size());
		Assert.assertEquals("Value1", result.get("Key1"));
		Assert.assertEquals("Value2", result.get("Key2"));
	}

	/**
	 * Test of stringToInputStream method, of class ComplexStringUtils.
	 */
	@Test
	public void testStringToInputStream() {
		String str = "TEST";
		InputStream result = ComplexStringUtils.stringToInputStream(str);
		MatcherAssert.assertThat(result, IsInstanceOf.instanceOf(ByteArrayInputStream.class));
	}

	/**
	 * Test of stringToInputStream method, of class ComplexStringUtils.
	 */
	@Test
	public void testStringToInputStream_Empty() {
		String str = "";
		InputStream result = ComplexStringUtils.stringToInputStream(str);
		MatcherAssert.assertThat(result, Matchers.nullValue());
	}

	/**
	 * Test of stringToInputStream method, of class ComplexStringUtils.
	 */
	@Test
	public void testStringToInputStream_Null() {
		String str = null;
		InputStream result = ComplexStringUtils.stringToInputStream(str);
		MatcherAssert.assertThat(result, Matchers.nullValue());
	}

}
