package com.jumbotours.commons.currency;

import com.jumbotours.commons.currency.type.CurrencyType;
import java.text.DecimalFormat;
import org.apache.commons.lang3.StringUtils;

/**
 * The class CurrencyUtil.
 * 
 * @author Josep Carbonell
 */
public final class CurrencyUtils {

	/**
	 * Hides default constructor.
	 */
	private CurrencyUtils() {}

	/**
	 * Gets the currency code
	 * 
	 * @param currencyCode
	 *            the currency code.
	 * @return the currency code.
	 */
	public static String getCurrency(String currencyCode) {
		if (StringUtils.isNotEmpty(currencyCode)) {
			return CurrencyType.searchKeyByCode(currencyCode);
		}
		return currencyCode;
	}

	/**
	 * Format the amount.
	 * 
	 * @param amount
	 *            the amount.
	 * @return the amount formated.
	 */
	public static String formatAmount(Double amount) {
		String amountFormated = "";
		if (amount != null) {
			DecimalFormat df = new DecimalFormat("###0.00");
			amountFormated = StringUtils.replace(df.format(amount), ",", ".");
		}
		return amountFormated;
	}

	/**
	 * Format the amount.
	 * 
	 * @param amount
	 *            the amount.
	 * @param decimalNumber
	 *            the decimal number.
	 * @return the amount formated.
	 */
	public static String formatAmount(Double amount, int decimalNumber) {
		String amountFormated = "";
		if (amount != null) {
			String format = "###0";
			if (decimalNumber > 0) {
				format += "." + StringUtils.repeat("0", decimalNumber);
			}
			DecimalFormat df = new DecimalFormat(format);
			amountFormated = StringUtils.replace(df.format(amount), ",", ".");
		}
		return amountFormated;
	}

	/**
	 * Format the amount.
	 * 
	 * @param amount
	 *            the amount.
	 * @param currencyCode
	 *            the currency code.
	 * @return the amount formated.
	 */
	public static String formatAmount(Double amount, String currencyCode) {
		String amountFormated = "";
		if (amount != null) {
			int numberOfDecimals = CurrencyType.getNumberOfDecimals(currencyCode);
			String format = "###0";
			if (numberOfDecimals > 0) {
				format += "." + StringUtils.repeat("0", numberOfDecimals);
			}
			DecimalFormat df = new DecimalFormat(format);
			amountFormated = StringUtils.replace(df.format(amount), ",", ".");
		}
		return amountFormated;
	}

	/**
	 * Format the amount without decimals.
	 * 
	 * @param amount
	 *            the amount.
	 * @return the amount formated.
	 */
	public static String formatAmountWithoutDecimals(Double amount) {
		String amountFormated = "";
		if (amount != null) {
			DecimalFormat df = new DecimalFormat("###0");
			amountFormated = StringUtils.replace(df.format(amount), ",", ".");
		}
		return amountFormated;
	}
}
