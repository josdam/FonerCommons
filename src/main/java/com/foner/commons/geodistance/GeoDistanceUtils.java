package com.foner.commons.geodistance;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * The class DistanceUtil. This code is based on DistanceCalculator's routine from GeoDataSource:
 * 
 * <a href="https://www.geodatasource.com/developers/java">https://www.geodatasource.com/developers/java</a>
 * 
 * @author <a href="mailto:josepdcs@gmail.com">Josep Carbonell</a>
 */
public final class GeoDistanceUtils {

	/** The logger. */
	private static final Logger	logger				= Logger.getLogger(GeoDistanceUtils.class);

	/** The constant KILOMETER_UNIT. */
	public static final String	KILOMETERS_UNIT		= "K";

	/** The constant MILES_UNIT. */
	public static final String	MILES_UNIT			= "M";

	/** The constant NAUTICAL_MILES_UNIT. */
	public static final String	NAUTICAL_MILES_UNIT	= "N";

	/**
	 * Hide default constructor.
	 */
	private GeoDistanceUtils() {}

	/**
	 * This calculates the distance between two points (given the latitude/longitude of those points).
	 * 
	 * <pre>
	 * latitude1, longitude1 = Latitude and Longitude of point 1 (in decimal degrees)
	 * latitude2, longitude2 = Latitude and Longitude of point 2 (in decimal degrees)
	 * unit = the unit you desire for results 
	 *        where: 'M' is miles (default) 
	 *               'K' is kilometers
	 *               'N' is nautical miles
	 * </pre>
	 * 
	 * @param latitude1
	 *            the latitude of point 1
	 * @param longitude1
	 *            the longitude of point 1
	 * @param latitude2
	 *            the latitude of point 2
	 * @param longitude2
	 *            the longitude of point 2
	 * @param unit
	 *            the unit of returned results
	 * @return
	 * 		the distance between two points
	 */
	public static double distance(double latitude1, double longitude1, double latitude2, double longitude2, String unit) {
		double theta = longitude1 - longitude2;
		double dist = Math.sin(deg2rad(latitude1)) * Math.sin(deg2rad(latitude2))
				+ Math.cos(deg2rad(latitude1)) * Math.cos(deg2rad(latitude2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		if (StringUtils.equalsIgnoreCase(unit, KILOMETERS_UNIT)) {
			dist *= 1.609344;
		} else if (StringUtils.equalsIgnoreCase(unit, NAUTICAL_MILES_UNIT)) {
			dist *= 0.8684;
		}

		return dist;
	}

	/**
	 * Converts decimal degrees to radians.
	 */
	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	/**
	 * Converts radians to decimal degrees.
	 */
	private static double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}

}
