package com.jumbotours.commons.lang;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 * The class ComplexStringUtils extends {@link StringUtils} capabilities.
 *
 * @author Josep Carbonell
 */
public final class ComplexStringUtils extends StringUtils {

	/**
	 * Hides default constructor.
	 */
	private ComplexStringUtils() {}

	/**
	 * <p>
	 * Splits the provided text into an Map, separators specified. This is an alternative to using StringTokenizer and convert to Map. Provided text has to
	 * contain "=" character (mandatory) in order to build the key=value map pair
	 * </p>
	 *
	 * <p>
	 * The separator is not included in the returned String map. Adjacent separators are treated as one separator. For more control over the split use the
	 * StrTokenizer class.
	 * </p>
	 *
	 * <p>
	 * A <code>null</code> input String returns <code>null</code>. A <code>null</code> separatorChars splits on whitespace.
	 * </p>
	 *
	 * <pre>
	 * ComplexStringUtils.splitToMap(null, *)               = null
	 * ComplexStringUtils.splitToMap("", *)                 = []	 
	 * ComplexStringUtils.splitToMap("k=abc b=def", null)   = ["k"="abc", "b"="def"]
	 * ComplexStringUtils.splitToMap("k=abc b=def", "")     = ["k"="abc", "b"="def"]
	 * ComplexStringUtils.splitToMap("k=abc bdef", " ")     = ["k"="abc", "b"="def"]
	 * ComplexStringUtils.splitToMap("abc      def", " ")   = ["k"="abc", "b"="def"]
	 * ComplexStringUtils.splitToMap("k=ab:b=cd:c=ef", ":") = ["k"="ab", "b"="cd", "c"="ef"]
	 * </pre>
	 *
	 * @param str
	 *            the String to parse, may be null
	 * @param separatorChars
	 *            the characters used as the delimiters, <code>null</code> splits on whitespace
	 * @return an map of parsed Strings, <code>null</code> if null String input
	 */
	public static Map<String, String> splitToMap(String str, String separatorChars) {
		Map<String, String> map = new HashMap<>(0);
		if (isNotEmpty(str)) {
			String[] list = split(str, separatorChars);
			for (String elem : list) {
				if (contains(elem, "=")) {
					String[] values = split(elem, "=");
					if ((values.length > 1) && isNotEmpty(values[0]) && isNotEmpty(values[1])) {
						map.put(trim(values[0]), trim(values[1]));
					}
				}
			}
		}

		return map;
	}

	/**
	 * Converts the given string into InputString. Keep in mind that Stream remains open, it has to be closed by the programmer
	 * 
	 * @param str
	 *            the string to convert to InputStream, may be null
	 * @return an InputStream
	 */
	public static InputStream stringToInputStream(String str) {
		InputStream is = null;
		if (isNotEmpty(str)) {
			is = new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));
		}
		return is;
	}

}
