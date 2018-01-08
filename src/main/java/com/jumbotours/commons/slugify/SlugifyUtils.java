package com.jumbotours.commons.slugify;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import org.apache.commons.lang3.StringUtils;

/**
 * The class SlugifyUitl. Class to slugify strings for SEO-friendly urls
 * 
 * @author Josep Carbonell
 */
public final class SlugifyUtils {

	/**
	 * Hide default constructor.
	 */
	private SlugifyUtils() {}

	/**
	 * Make slugify.
	 * 
	 * @param input
	 *            the input
	 * @return the string
	 */
	public static String slugify(String input) {
		if (StringUtils.isEmpty(input)) {
			return "";
		}
		String toReturn = normalize(input);
		toReturn = StringUtils.replace(toReturn, " ", " ");
		toReturn = StringUtils.replace(toReturn, "&", " ");
		toReturn = StringUtils.replace(toReturn, ";", " ");
		toReturn = StringUtils.replace(toReturn, ":", " ");
		toReturn = StringUtils.replace(toReturn, ".", " ");
		toReturn = StringUtils.replace(toReturn, ",", " ");
		toReturn = StringUtils.replace(toReturn, "*", " ");
		toReturn = StringUtils.replace(toReturn, "%", " ");
		toReturn = StringUtils.replace(toReturn, "\"", " ");
		toReturn = StringUtils.replace(toReturn, "/", " ");
		toReturn = StringUtils.replace(toReturn, "\\", " ");
		toReturn = StringUtils.replace(toReturn, "(", " ");
		toReturn = StringUtils.replace(toReturn, ")", " ");
		toReturn = StringUtils.replace(toReturn, "'", " ");
		toReturn = StringUtils.replace(toReturn, "!", " ");
		toReturn = StringUtils.replace(toReturn, "¡", " ");
		toReturn = StringUtils.replace(toReturn, "?", " ");
		toReturn = StringUtils.replace(toReturn, "¿", " ");
		toReturn = StringUtils.replace(toReturn, "<", " ");
		toReturn = StringUtils.replace(toReturn, ">", " ");
		toReturn = StringUtils.replace(toReturn, "+", " ");
		toReturn = StringUtils.replace(toReturn, "_", " ");
		toReturn = StringUtils.remove(toReturn, "-");
		toReturn = StringUtils.join(StringUtils.split(toReturn, " "), "-");
		toReturn = StringUtils.lowerCase(toReturn);
		try {
			toReturn = URLEncoder.encode(toReturn, "UTF-8");
		} catch (UnsupportedEncodingException ex) {}
		return toReturn;
	}

	/**
	 * Normalize.
	 * 
	 * @param input
	 *            the input
	 * @return the string
	 */
	public static String normalize(String input) {
		if (StringUtils.isEmpty(input)) {
			return "";
		}
		return Normalizer.normalize(input, Form.NFD).replaceAll("[^\\p{ASCII}]", "");
	}

	/**
	 * Normalize and replace.
	 * 
	 * @param input
	 *            the input
	 * @return the string
	 */
	public static String normalizeAndReplace(String input) {
		if (StringUtils.isEmpty(input)) {
			return "";
		}
		String toReturn = Normalizer.normalize(input, Form.NFD).replaceAll("[^\\p{ASCII}]", "");

		toReturn = StringUtils.remove(toReturn, "&");
		toReturn = StringUtils.remove(toReturn, ";");
		toReturn = StringUtils.remove(toReturn, ":");
		toReturn = StringUtils.remove(toReturn, ".");
		toReturn = StringUtils.remove(toReturn, ",");
		toReturn = StringUtils.remove(toReturn, "*");
		toReturn = StringUtils.remove(toReturn, "%");
		toReturn = StringUtils.remove(toReturn, "\"");
		toReturn = StringUtils.remove(toReturn, "/");
		toReturn = StringUtils.remove(toReturn, "\\");
		toReturn = StringUtils.remove(toReturn, "(");
		toReturn = StringUtils.remove(toReturn, ")");
		toReturn = StringUtils.remove(toReturn, "'");
		toReturn = StringUtils.remove(toReturn, "!");
		toReturn = StringUtils.remove(toReturn, "¡");
		toReturn = StringUtils.remove(toReturn, "?");
		toReturn = StringUtils.remove(toReturn, "¿");
		toReturn = StringUtils.remove(toReturn, "<");
		toReturn = StringUtils.remove(toReturn, ">");
		toReturn = StringUtils.remove(toReturn, "+");
		toReturn = StringUtils.remove(toReturn, "-");

		return StringUtils.trim(toReturn);
	}
}
