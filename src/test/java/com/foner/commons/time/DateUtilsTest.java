package com.foner.commons.time;

import com.foner.commons.time.DateUtils;
import com.foner.commons.AbstractTest;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.datatype.XMLGregorianCalendar;
import org.joda.time.DateTime;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * The DateUtilsTest class.
 * 
 * @author Josep Carbonell
 */
public class DateUtilsTest extends AbstractTest {

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
	 * Test of dateToString method, of class DateUtils.
	 * 
	 * @throws java.lang.Exception
	 */
	@Test
	public void testDateToString() throws Exception {
		Date date = new Date();
		String pattern = DateUtils.PATTERN_DD_MM_YYYY;
		String result = DateUtils.dateToString(date, pattern);
		assertNotNull(result);
	}

	/**
	 * Test of calendarToString method, of class DateUtils.
	 * 
	 * @throws java.lang.Exception
	 */
	@Test
	public void testCalendarToString() throws Exception {
		Calendar calendar = new GregorianCalendar();
		String result = DateUtils.calendarToString(calendar, DateUtils.PATTERN_DD_MM_YYYY);
		assertNotNull(result);
	}

	/**
	 * Test of dateTimeToString method, of class DateUtils.
	 * 
	 * @throws java.lang.Exception
	 */
	@Test
	public void testDateTimeToString() throws Exception {
		DateTime dateTime = new DateTime();
		String result = DateUtils.dateTimeToString(dateTime, DateUtils.PATTERN_DD_MM_YYYY);
		assertNotNull(result);
	}

	/**
	 * Test of stringToDate method, of class DateUtils.
	 * 
	 * @throws java.lang.Exception
	 */
	@Test
	public void testStringToDate() throws Exception {
		String in = "10/10/2010";
		Date result = DateUtils.stringToDate(in, DateUtils.PATTERN_DD_MM_YYYY);
		assertNotNull(result);
	}

	/**
	 * Test of stringToDateTime method, of class DateUtils.
	 * 
	 * @throws java.lang.Exception
	 */
	@Test
	public void testStringToDateTime() throws Exception {
		String in = "10/10/2010";
		DateTime result = DateUtils.stringToDateTime(in, DateUtils.PATTERN_DD_MM_YYYY);
		assertNotNull(result);
	}

	/**
	 * Test of stringToCalendar method, of class DateUtils.
	 * 
	 * @throws java.lang.Exception
	 */
	@Test
	public void testStringToCalendar() throws Exception {
		String in = "10/10/2010";
		Calendar result = DateUtils.stringToCalendar(in, DateUtils.PATTERN_DD_MM_YYYY);
		assertNotNull(result);
	}

	/**
	 * Test of xmlGregorianCalendarToString method, of class DateUtils.
	 * 
	 * @throws java.lang.Exception
	 */
	@Test
	public void testXmlGregorianCalendarToString() throws Exception {
		XMLGregorianCalendar xmlgc = new XMLGregorianCalendarImpl(new GregorianCalendar());
		String result = DateUtils.xmlGregorianCalendarToString(xmlgc, DateUtils.PATTERN_DD_MM_YYYY);
		assertNotNull(result);
	}

	/**
	 * Test of xmlGregorianCalendarToDate method, of class DateUtils.
	 * 
	 * @throws java.lang.Exception
	 */
	@Test
	public void testXmlGregorianCalendarToDate() throws Exception {
		XMLGregorianCalendar xmlgc = new XMLGregorianCalendarImpl(new GregorianCalendar());
		Date result = DateUtils.xmlGregorianCalendarToDate(xmlgc);
		assertNotNull(result);
	}

}
