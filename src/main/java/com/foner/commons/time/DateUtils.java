package com.foner.commons.time;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * The DateUtils class.
 * 
 * @author Josep Carbonell
 */
public final class DateUtils {

	/** The Constant PATTERN_DD_MM_YY. */
	public static final String	PATTERN_DD_MM_YY					= "dd/MM/yy";

	/** The Constant PATTERN_DD_MM_YYYY. */
	public static final String	PATTERN_DD_MM_YYYY					= "dd/MM/yyyy";

	/** The Constant PATTERN_YYYY_MM_DD. */
	public static final String	PATTERN_YYYY_MM_DD					= "yyyy-MM-dd";

	/** The Constant PATTERN_DD_MM_YYYY_H_m. */
	public static final String	PATTERN_DD_MM_YYYY_H_m				= "dd/MM/yyyy H:m";

	/** The Constant PATTERN_DD_MM_YYYY_HH_mm. */
	public static final String	PATTERN_DD_MM_YYYY_HH_mm			= "dd/MM/yyyy HH:mm";

	/** The Constant PATTERN_DD_MM_YYYY_HH_mm_ss. */
	public static final String	PATTERN_DD_MM_YYYY_HH_mm_ss			= "dd/MM/yyyy HH:mm:ss";

	/** The Constant PATTERN_DDMMYYYY_HHmm. */
	public static final String	PATTERN_DDMMYYYY_HHmm				= "ddMMyyyy_HHmm";

	/** The Constant PATTERN_YYYYMMDD. */
	public static final String	PATTERN_YYYYMMDD					= "yyyyMMdd";

	/** The Constant PATTERN_YYYYMMDDHH. */
	public static final String	PATTERN_YYYYMMDDHH					= "yyyyMMddHH";

	/** The Constant PATTERN_YYYYMMDDHHMM. */
	public static final String	PATTERN_YYYYMMDDHHMM				= "yyyyMMddHHmm";

	/** The Constant PATTERN_YYYYMMDDHHMMSS. */
	public static final String	PATTERN_YYYYMMDDHHMMSS				= "yyyyMMddHHmmss";

	/** The Constant PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSSZ. */
	public static final String	PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSSZ	= "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

	/** The Constant ZULU_ZONE. */
	public static final int		ZULU_ZONE							= 0;

	/**
	 * Hides default constructor.
	 */
	private DateUtils() {}

	/**
	 * Date to string with given pattern.
	 * 
	 * @param date
	 *            the date
	 * @param pattern
	 *            the pattern
	 * @return the date as string following given pattern
	 * @throws Exception
	 *             the exception
	 */
	public static String dateToString(Date date, String pattern) throws Exception {
		if (date == null) {
			return null;
		}
		return new DateTime(date.getTime()).toString(DateTimeFormat.forPattern(pattern));

	}

	/**
	 * Calendar to string with given pattern.
	 * 
	 * @param calendar
	 *            the calendar
	 * @param pattern
	 *            the pattern
	 * @return the calendar as string following given pattern
	 * @throws Exception
	 *             the exception
	 */
	public static String calendarToString(Calendar calendar, String pattern) throws Exception {
		if (calendar == null) {
			return null;
		}
		return new DateTime(calendar.getTimeInMillis()).toString(DateTimeFormat.forPattern(pattern));
	}

	/**
	 * Datetime to string with given pattern.
	 * 
	 * @param dateTime
	 *            the date time
	 * @param pattern
	 *            the pattern
	 * @return the datetime as string following given pattern
	 * @throws Exception
	 *             jwlp util exception
	 */
	public static String dateTimeToString(DateTime dateTime, String pattern) throws Exception {
		if (dateTime == null) {
			return null;
		}
		return dateTime.toString(DateTimeFormat.forPattern(pattern));
	}

	/**
	 * String to date.
	 * 
	 * @param in
	 *            the in
	 * @param pattern
	 *            the pattern
	 * @return the date
	 * @throws Exception
	 *             the exception
	 */
	public static Date stringToDate(String in, String pattern) throws Exception {
		if (StringUtils.isBlank(in)) {
			return null;
		}
		DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(pattern);
		return dateTimeFormatter.parseDateTime(in).toDate();
	}

	/**
	 * String to date time.
	 * 
	 * @param in
	 *            the in
	 * @param pattern
	 *            the pattern
	 * @return the date time
	 * @throws Exception
	 *             the exception
	 */
	public static DateTime stringToDateTime(String in, String pattern) throws Exception {
		if (StringUtils.isEmpty(in)) {
			return null;
		}
		DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(pattern);
		return dateTimeFormatter.parseDateTime(in);
	}

	/**
	 * String to Calendar from given pattern.
	 * 
	 * @param in
	 *            string date in.
	 * @param pattern
	 *            string pattern
	 * @return date
	 * @throws Exception
	 *             b2c core exception
	 */
	public static Calendar stringToCalendar(String in, String pattern) throws Exception {
		if (StringUtils.isEmpty(in)) {
			return null;
		}
		DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(pattern);
		return dateTimeFormatter.parseDateTime(in).toGregorianCalendar();

	}

	/**
	 * XMLGregorianCalendar to String transform.
	 * 
	 * @param xmlgc
	 *            the date
	 * @param pattern
	 *            the pattern
	 * @return string
	 * @throws Exception
	 *             the exception
	 */
	public static String xmlGregorianCalendarToString(XMLGregorianCalendar xmlgc, String pattern) throws Exception {
		if (xmlgc == null) {
			return null;
		}
		return new DateTime(xmlgc.toGregorianCalendar().getTime()).toString(DateTimeFormat.forPattern(pattern));
	}

	/**
	 * XMLGregorianCalendar to Date transform.
	 * 
	 * @param xmlgc
	 *            the date
	 * @return string
	 * @throws Exception
	 *             the exception
	 */
	public static Date xmlGregorianCalendarToDate(XMLGregorianCalendar xmlgc) throws Exception {
		if (xmlgc == null) {
			return null;
		}
		return new DateTime(xmlgc.toGregorianCalendar().getTime()).toDate();
	}

	/**
	 * GregorianCalendar to XMLGregorianCalendar.
	 * 
	 * @param gregorianCalendar
	 *            the gregorian calendar
	 * @return XMLGregorianCalendar
	 * @throws Exception
	 *             the exception
	 */
	public static XMLGregorianCalendar gregorianCalendarToXMLGregorianCalendar(GregorianCalendar gregorianCalendar) throws Exception {
		if (gregorianCalendar == null) {
			return null;
		}
		return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
	}

	/**
	 * Date to XMLGregorianCalendar.
	 * 
	 * @param date
	 *            the date
	 * @return XMLGregorianCalendar
	 * @throws Exception
	 *             the exception
	 */
	public static XMLGregorianCalendar dateToXMLGregorianCalendar(Date date) throws Exception {
		if (date == null) {
			return null;
		}
		GregorianCalendar c = new GregorianCalendar();
		c.setTime(date);
		return DatatypeFactory.newInstance().newXMLGregorianCalendar(c);

	}

	/**
	 * java.util.Date to XMLGregorianCalendar.
	 * 
	 * @param date
	 *            the date
	 * @param pattern
	 *            the pattern
	 * @return XMLGregorianCalendar
	 * @throws Exception
	 *             the exception
	 */
	public static XMLGregorianCalendar dateToXMLGregorianCalendar(Date date, String pattern) throws Exception {
		if (date == null) {
			return null;
		}
		try {
			DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(pattern);
			DateTime dateTime = new DateTime(date.getTime());
			GregorianCalendar c = dateTimeFormatter.parseDateTime(dateTime.toString()).toGregorianCalendar();
			return DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
		} catch (DatatypeConfigurationException ex) {
			return null;
		}
	}

	/**
	 * String to XMLGregorianCalendar.
	 * 
	 * @param in
	 *            the in
	 * @param pattern
	 *            the pattern
	 * @return date
	 * @throws Exception
	 *             the jwlp util exception
	 */
	public static XMLGregorianCalendar stringToXMLGregorianCalendar(String in, String pattern) throws Exception {
		if (StringUtils.isEmpty(in)) {
			return null;
		}
		DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(pattern);
		GregorianCalendar gcal = dateTimeFormatter.parseDateTime(in).toGregorianCalendar();
		return DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
	}

	/**
	 * Number of Days Between two Dates.
	 * 
	 * @param in1
	 *            date 1;
	 * @param in2
	 *            date 2;
	 * @param pattern
	 *            the pattern
	 * @return number of days.
	 * @throws Exception
	 *             the exception
	 */
	public static int daysBetween(String in1, String in2, String pattern) throws Exception {
		Date first = DateUtils.stringToDate(in1, pattern);
		Date last = DateUtils.stringToDate(in2, pattern);
		return Days.daysBetween(new DateTime(first), new DateTime(last)).getDays();
	}

	/**
	 * Number of Days Between two Dates.
	 * 
	 * @param in1
	 *            date 1;
	 * @param in2
	 *            date 2;
	 * @return number of days.
	 * @throws Exception
	 *             the exception
	 */
	public static int daysBetween(Date in1, Date in2) throws Exception {
		return Days.daysBetween(new DateTime(in1), new DateTime(in2)).getDays();
	}

	/**
	 * add days.
	 * 
	 * @param date
	 *            date;
	 * @param days
	 *            days;
	 * @return date with add days.
	 * @throws Exception
	 *             the exception
	 */
	public static Date addDays(Date date, int days) throws Exception {
		if (date == null) {
			return null;
		}
		DateTime dateTime = new DateTime(date.getTime());
		DateTime added = dateTime.plusDays(days);
		return added.toDate();
	}

	/**
	 * add days.
	 * 
	 * @param date
	 *            the date
	 * @param pattern
	 *            the pattern
	 * @param days
	 *            the days
	 * @return date with add days.
	 * @throws Exception
	 *             the exception
	 */
	public static String addDays(String date, String pattern, int days) throws Exception {
		if (date == null) {
			return null;
		}
		DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(pattern);
		DateTime dateTime = dateTimeFormatter.parseDateTime(date);
		DateTime added = dateTime.plusDays(days);
		return dateToString(added.toDate(), pattern);
	}

	/**
	 * Minus days.
	 * 
	 * @param date
	 *            the date
	 * @param days
	 *            the days
	 * @return date with minus days.
	 * @throws Exception
	 *             the exception
	 */
	public static Date minusDays(Date date, int days) throws Exception {
		if (date == null) {
			return null;
		}
		DateTime dateTime = new DateTime(date.getTime());
		DateTime added = dateTime.minusDays(days);
		return added.toDate();
	}

	/**
	 * add days.
	 * 
	 * @param date
	 *            the date
	 * @param pattern
	 *            the pattern
	 * @param days
	 *            the days
	 * @return date with minus days.
	 * @throws Exception
	 *             the exception
	 */
	public static String minusDays(String date, String pattern, int days) throws Exception {
		DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(pattern);
		DateTime dateTime = dateTimeFormatter.parseDateTime(date);
		DateTime added = dateTime.minusDays(days);
		return dateToString(added.toDate(), pattern);
	}

	/**
	 * Gets the hour of day from date.
	 * 
	 * @param date
	 *            the date;
	 * @return the hour.
	 * @throws Exception
	 *             the exception
	 */
	public static String getHourOfDay(Date date) throws Exception {
		DateTime dateTime = new DateTime(date.getTime());
		return String.valueOf(dateTime.getHourOfDay());
	}

	/**
	 * Check's if day is within date range.
	 * 
	 * @param date
	 *            the date
	 * @param dateFrom
	 *            the date from
	 * @param dateTo
	 *            the date to
	 * @return true, if date is in range.
	 */
	public static boolean dayWithinDateRange(Date date, Date dateFrom, Date dateTo) {
		if ((date == null) || (dateFrom == null) || (dateTo == null)) {
			return false;
		}
		DateTime date1 = new DateTime(date.getTime());
		DateTime rangeFrom = new DateTime(dateFrom.getTime());
		DateTime rangeTo = new DateTime(dateTo.getTime());

		return (Days.daysBetween(rangeFrom, date1).getDays() >= 0) && (Days.daysBetween(rangeTo, date1).getDays() <= 0);
	}

	/**
	 * Check's if day is within date range.
	 * 
	 * @param date
	 *            the date
	 * @param dateFrom
	 *            the date from
	 * @param dateTo
	 *            the date to
	 * @return true, if date is in range.
	 */
	public static boolean dayWithinDateRange(DateTime date, DateTime dateFrom, DateTime dateTo) {
		if ((date == null) || (dateFrom == null) || (dateTo == null)) {
			return false;
		}
		return (Days.daysBetween(dateFrom, date).getDays() >= 0) && (Days.daysBetween(dateTo, date).getDays() <= 0);
	}

	/**
	 * Check's if day is within date range.
	 * 
	 * @param date
	 *            the date
	 * @param dateFrom
	 *            the date from
	 * @param dateTo
	 *            the date to
	 * @return true, if date is in range.
	 */
	public static boolean dayWithinDateRange(XMLGregorianCalendar date, XMLGregorianCalendar dateFrom, XMLGregorianCalendar dateTo) {
		DateTime date1 = new DateTime(date.toGregorianCalendar().getTime());
		DateTime rangeFrom = new DateTime(dateFrom.toGregorianCalendar().getTime());
		DateTime rangeTo = new DateTime(dateTo.toGregorianCalendar().getTime());

		return (Days.daysBetween(rangeFrom, date1).getDays() >= 0) && (Days.daysBetween(rangeTo, date1).getDays() <= 0);
	}

	/**
	 * Formats the date.
	 * 
	 * @param date
	 *            the date
	 * @param givenFormat
	 *            the given format
	 * @param resultFormat
	 *            the result format
	 * @return the formated date.
	 * @throws Exception
	 *             the exception
	 */
	public static String reformatDate(String date, String givenFormat, String resultFormat) throws Exception {
		if (StringUtils.isBlank(date)) {
			return null;
		}
		Date currentDate = DateUtils.stringToDate(date, givenFormat);
		return DateUtils.dateToString(currentDate, resultFormat);
	}
}
