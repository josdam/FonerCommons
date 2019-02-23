package com.foner.commons.csv;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.foner.commons.AbstractTest;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * The class CSVTest.
 *
 * @author <a href="mailto:josepdcs@gmail.com">Josep Carbonell</a>
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Csv.class, Logger.class, TestPojo.class})
public class CsvTest extends AbstractTest {

	/**
	 * Sets the up class.
	 */
	@BeforeClass
	public static void setUpClass() {}

	/**
	 * Sets the up.
	 */
	@Before
	public void setUp() {

	}

	/**
	 * Tear down.
	 */
	@After
	public void tearDown() {}

	/**
	 * Test of getInstance method, of class CSV.
	 */
	@Test
	public void testGetInstance() {
		Csv csv = Csv.getInstance();
		MatcherAssert.assertThat(csv, Matchers.notNullValue());
		MatcherAssert.assertThat(csv, IsInstanceOf.instanceOf(Csv.class));
	}

	/**
	 * Test of newInstance method, of class CSV.
	 */
	@Test
	public void testNewInstance() {
		Csv csv = Csv.newInstance();
		MatcherAssert.assertThat(csv, Matchers.notNullValue());
		MatcherAssert.assertThat(csv, IsInstanceOf.instanceOf(Csv.class));
	}

	/**
	 * Test of serialize method, of class CSV. Test deserialize Pojo to CSV.
	 *
	 * @throws JsonProcessingException
	 *             the json processing exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testDeserialize_File_TestPojo() throws JsonProcessingException, IOException {
		Csv csv = Csv.newInstance();
		File file = new File(getClass().getClassLoader().getResource("com/foner/commons/csv/Test.csv").getFile());
		List<TestPojo> testPojos = csv.deserialize(file, TestPojo.class, ',');
		MatcherAssert.assertThat(testPojos.get(0).getColumnA(), Matchers.equalTo("1"));
		MatcherAssert.assertThat(testPojos.get(0).getColumnB(), Matchers.equalTo("A"));
		MatcherAssert.assertThat(testPojos.get(1).getColumnA(), Matchers.equalTo("2"));
		MatcherAssert.assertThat(testPojos.get(1).getColumnB(), Matchers.equalTo("B"));
		MatcherAssert.assertThat(testPojos.get(2).getColumnA(), Matchers.equalTo("3"));
		MatcherAssert.assertThat(testPojos.get(2).getColumnB(), Matchers.equalTo("C"));

	}

	/**
	 * Test of serialize method, of class CSV. Test deserialize Pojo to CSV.
	 *
	 * @throws JsonProcessingException
	 *             the json processing exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testDeserialize_String_TestPojo() throws JsonProcessingException, IOException {
		Csv csv = Csv.newInstance();
		File file = new File(getClass().getClassLoader().getResource("com/foner/commons/csv/Test.csv").getFile());
		String csvString = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
		MatcherAssert.assertThat(csvString, Matchers.notNullValue());
		logger.info(String.format("CSV String: %s", csvString));
		List<TestPojo> testPojos = csv.deserialize(csvString, TestPojo.class, ',');
		MatcherAssert.assertThat(testPojos.get(0).getColumnA(), Matchers.equalTo("1"));
		MatcherAssert.assertThat(testPojos.get(0).getColumnB(), Matchers.equalTo("A"));
		MatcherAssert.assertThat(testPojos.get(1).getColumnA(), Matchers.equalTo("2"));
		MatcherAssert.assertThat(testPojos.get(1).getColumnB(), Matchers.equalTo("B"));
		MatcherAssert.assertThat(testPojos.get(2).getColumnA(), Matchers.equalTo("3"));
		MatcherAssert.assertThat(testPojos.get(2).getColumnB(), Matchers.equalTo("C"));

	}

	/**
	 * Test of serialize method, of class CSV. Test deserialize file matrix.
	 *
	 * @throws JsonProcessingException
	 *             the json processing exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testDeserialize_File_Matrix() throws JsonProcessingException, IOException {
		Csv csv = Csv.newInstance();
		File file = new File(getClass().getClassLoader().getResource("com/foner/commons/csv/Test.csv").getFile());
		List<List<String>> elements = csv.deserialize(file, ',');
		MatcherAssert.assertThat(elements.get(0).get(0), Matchers.equalTo("1"));
		MatcherAssert.assertThat(elements.get(0).get(1), Matchers.equalTo("A"));
		MatcherAssert.assertThat(elements.get(1).get(0), Matchers.equalTo("2"));
		MatcherAssert.assertThat(elements.get(1).get(1), Matchers.equalTo("B"));
		MatcherAssert.assertThat(elements.get(2).get(0), Matchers.equalTo("3"));
		MatcherAssert.assertThat(elements.get(2).get(1), Matchers.equalTo("C"));

	}

	/**
	 * Test of serialize method, of class Xml. Test serialize Pojo to XML.
	 *
	 * @throws JsonProcessingException
	 *             the json processing exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testSerialize_TestPojo() throws JsonProcessingException, IOException {
		Csv csv = Csv.newInstance();
		List<TestPojo> testPojos = new ArrayList<>();
		TestPojo testPojo = new TestPojo();
		testPojo.setColumnA("1");
		testPojo.setColumnB("A");
		testPojos.add(testPojo);
		testPojo = new TestPojo();
		testPojo.setColumnA("2");
		testPojo.setColumnB("B");
		testPojos.add(testPojo);
		testPojo = new TestPojo();
		testPojo.setColumnA("3");
		testPojo.setColumnB("C");
		testPojos.add(testPojo);

		String xmlString = csv.serialize(testPojos, TestPojo.class);
		MatcherAssert.assertThat(xmlString, Matchers.notNullValue());
		logger.info(String.format("Csv String: %s", xmlString));

	}
}
