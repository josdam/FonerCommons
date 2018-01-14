package com.foner.commons.time;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

/**
 * The class DOWPattern.
 * 
 * <p>
 * A Day Of Week pattern. Example DOW(0000011).
 */
public class DOWPattern implements Serializable {

	/** The logger. */
	private static final Logger		logger				= Logger.getLogger(DOWPattern.class);

	/** The ALWAYS constant. */
	public final static DOWPattern	ALWAYS				= new DOWPattern(127);

	/** The Constant serialVersionUID. */
	private static final long		serialVersionUID	= -3969340559986082469L;

	/** The pattern. */
	private int						pattern;

	/**
	 * Week start day. 1 = Monday 2 = Tuesday 3 = Wednesday 4 = Thursday
	 * 5 = Friday 6 = Saturday 7 = Sunday
	 * <p/>
	 * Default week start day is Monday.
	 */
	private int						weekStart			= Calendar.MONDAY;

	/**
	 * Indicates whether the week should be interpreted as inverted. If the
	 * inverted flag is set to false the top left number of the
	 * pattern is the starting day of the week. If the inverted flag is set to
	 * true the top right number of the pattern is the starting day of the week.
	 */
	private boolean					invertedWeek		= false;

	/**
	 * Instantiates a new DOW pattern.
	 */
	public DOWPattern() {}

	/**
	 * Instantiates a new DOW pattern.
	 *
	 * @param pattern
	 *            the pattern
	 */
	public DOWPattern(int pattern) {
		this.pattern = pattern;
	}

	/**
	 * Instantiates a new DOW pattern.
	 *
	 * @param day
	 *            the day
	 */
	public DOWPattern(Calendar day) {
		setAppliesOn(day.get(Calendar.DAY_OF_WEEK), true);
	}

	/**
	 * Instantiates a new DOW pattern.
	 *
	 * @param pattern
	 *            the pattern
	 */
	public DOWPattern(String pattern) {
		this(pattern, 2);
	}

	/**
	 * Instantiates a new DOW pattern.
	 *
	 * @param pattern
	 *            the pattern
	 * @param radix
	 *            the radix
	 */
	public DOWPattern(String pattern, int radix) {
		this.pattern = Integer.parseInt(pattern, radix);
	}

	/**
	 * Sets the applies on.
	 *
	 * @param dayOfWeek
	 *            the day of week
	 * @param applies
	 *            the applies
	 */
	private void setAppliesOn(int dayOfWeek, boolean applies) {
		int mask = getDayMask(dayOfWeek);
		if (applies) {
			// Set bit
			pattern |= mask;
		} else {
			// remove bit
			pattern &= 0xFFFF ^ mask;
		}

	}

	/**
	 * Applies on.
	 *
	 * @param dayOfWeek
	 *            the day of week
	 * @return true, if successful
	 */
	public boolean appliesOn(Calendar dayOfWeek) {
		return appliesOn(dayOfWeek.get(Calendar.DAY_OF_WEEK));
	}

	/**
	 * Applies on.
	 *
	 * @param dayOfWeek
	 *            the day of week
	 * @return true, if successful
	 */
	public boolean appliesOn(int dayOfWeek) {
		return (0 != (pattern & getDayMask(dayOfWeek)));
	}

	/**
	 * Gets the day mask.
	 *
	 * @param dayOfWeek
	 *            the day of week
	 * @return the day mask
	 */
	private int getDayMask(int dayOfWeek) {
		int convertedDay = dayOfWeek - this.weekStart;
		if (convertedDay < 0) {
			convertedDay = convertedDay + 8;
		} else {
			convertedDay++;
		}
		if (invertedWeek) {
			convertedDay = 8 - convertedDay;
		}
		int bit = 7 - convertedDay;
		return (1 << bit);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Integer.toString(pattern, 2);
	}

	/**
	 * Sets the pattern.
	 *
	 * @param pattern
	 *            the new pattern
	 */
	public void setPattern(int pattern) {
		this.pattern = pattern;
	}

	/**
	 * Gets the pattern.
	 *
	 * @return the pattern
	 */
	public int getPattern() {
		return pattern;
	}

	/**
	 * Applies always.
	 *
	 * @return true, if successful
	 */
	public boolean appliesAlways() {
		return (pattern == 127);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof DOWPattern)) {
			return false;
		} else {
			DOWPattern aDow = (DOWPattern) o;
			return (aDow.pattern == this.pattern) && (aDow.weekStart == this.weekStart) && (aDow.invertedWeek == this.invertedWeek);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 7;
		hash = 53 * hash + this.pattern;
		hash = 53 * hash + this.weekStart;
		hash = 53 * hash + (this.invertedWeek ? 1 : 0);
		return hash;
	}

	/**
	 * Intersects.
	 *
	 * @param other
	 *            the other
	 * @return true, if successful
	 */
	public boolean intersects(DOWPattern other) {
		int anded = (this.pattern & other.pattern);
		return (anded != 0);
	}

	/**
	 * Gets the week start.
	 *
	 * @return the week start
	 */
	public int getWeekStart() {
		return weekStart;
	}

	/**
	 * Sets the weekStart attribute. <br>
	 * If the value is smaller than 1 it will be replaced by its corresponding
	 * week day between 1 and 7: 0 = Saturday -1 = Friday -2 = Thursday ... <br>
	 * If the value is greater than 7 it will be replaced by its corresponding
	 * week day between 1 and 7: 8 = Sunday 9 = Monday 10 = Tuesday ...
	 *
	 * @param weekStart
	 *            Week start day.
	 */
	public void setWeekStart(int weekStart) {
		this.weekStart = Math.abs(weekStart % 7) == 0 ? 7 : Math.abs(weekStart % 7);
	}

	/**
	 * Checks if is inverted week.
	 *
	 * @return true, if is inverted week
	 */
	public boolean isInvertedWeek() {
		return invertedWeek;
	}

	/**
	 * Sets the inverted week.
	 *
	 * @param invertedWeek
	 *            the new inverted week
	 */
	public void setInvertedWeek(boolean invertedWeek) {
		this.invertedWeek = invertedWeek;
	}

	/**
	 * Gets dates on range according DOW pattern.
	 * 
	 * @param from
	 *            the from
	 * @param to
	 *            the to
	 * @return the list of dates
	 */
	public List<Calendar> getDatesOnRange(Calendar from, Calendar to) {
		List<Calendar> calendars = new ArrayList<>(0);

		Calendar current = from;
		if (to.after(from) || to.equals(from)) {
			while (current.before(to) || current.equals(to)) {
				if (appliesOn(current)) {
					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(current.getTimeInMillis());
					calendars.add(calendar);
				}
				current.add(Calendar.DAY_OF_MONTH, 1);
			}
		}

		return calendars;
	}

	/**
	 * Gets dates on range according DOW pattern.
	 * 
	 * @param from
	 *            the from
	 * @param to
	 *            the to
	 * @return the list of dates
	 */
	public List<DateTime> getDatesOnRange(DateTime from, DateTime to) {
		List<DateTime> dateTimes = new ArrayList<>(0);

		while (from.isBefore(to) || from.isEqual(to)) {
			if (appliesOn(from.toGregorianCalendar())) {
				dateTimes.add(from);
			}
			from = from.plusDays(1);
		}

		return dateTimes;
	}

	/**
	 * Gets dates on range according DOW pattern.
	 * 
	 * @param from
	 *            the from
	 * @param to
	 *            the to
	 * @param datePattern
	 *            the date pattern
	 * @return the list of dates
	 */
	public List<String> getDatesOnRange(String from, String to, String datePattern) {
		List<String> dates = new ArrayList<>(0);

		try {
			DateTime fromDateTime = DateUtils.stringToDateTime(from, datePattern);
			DateTime toDateTime = DateUtils.stringToDateTime(to, datePattern);
			List<DateTime> dateTimes = getDatesOnRange(fromDateTime, toDateTime);
			for (DateTime dateTime : dateTimes) {
				dates.add(DateUtils.dateTimeToString(dateTime, datePattern));
			}
		} catch (Exception e) {
			logger.error(e, e);
		}

		return dates;
	}
}
