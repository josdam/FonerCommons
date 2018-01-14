package com.foner.commons.currency.type;

import java.io.Serializable;
import org.apache.commons.lang3.StringUtils;

/**
 * The Enum CurrencyType.
 * 
 * @author Josep Carbonell <josepdcs@gmail.com>
 */
public enum CurrencyType implements Serializable {

	/** The Euro. */
	EUR("EUR", "€", "Euro", 2),

	/** The Pound. */
	GBP("GBP", "£", "Point", 2),

	/** The Dolar. */
	USD("USD", "$", "Dolar", 2),

	/** The Dominican Peso. */
	DOP("DOP", "$", "Peso", 2),

	/** The Mexican Peso. */
	MXN("MXN", "$", "Peso", 2),

	/** The Thai Baht. */
	THB("THB", "฿", "Baht", 3),

	/** The Tunisian Dinar. */
	TND("TND", "د.ت", "Dinar", 3),

	/** The Moroccan Dirham. */
	MAD("MAD", "درهم", "Dirham", 2),

	/** The UAE Dirham. */
	AED("AED", "د.إ", "Dirham", 2);

	/** The constant DEFAULT_NUMBER_DECIMALS. */
	private static final int	DEFAULT_NUMBER_DECIMALS	= 2;

	/** The code. */
	public final String			code;

	/** The key. */
	public final String			key;

	/** The name. */
	public final String			name;

	/** The decimals. */
	public final int			decimals;

	/**
	 * Instantiates a new request type.
	 * 
	 * @param code
	 *            the code
	 * @param key
	 *            the key
	 * @param name
	 *            the name
	 */
	private CurrencyType(String code, String key, String name, Integer decimals) {
		this.code = code;
		this.key = key;
		this.name = name;
		this.decimals = decimals;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return name;
	}

	/**
	 * Gets the code.
	 * 
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Gets the key.
	 * 
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Gets the decimals.
	 * 
	 * @return the decimals
	 */
	public Integer getDecimals() {
		return decimals;
	}

	/**
	 * Search the key by code.
	 * 
	 * @param code
	 *            the code
	 * @return the code
	 */
	public static String searchKeyByCode(String code) {
		for (CurrencyType currencyType : values()) {
			if (StringUtils.equalsIgnoreCase(currencyType.getCode(), code)) {
				return currencyType.getKey();
			}
		}
		return code;
	}

	/**
	 * Gets number of decimals by given code (2 decimals by default).
	 * 
	 * @param code
	 *            the code
	 * @return the code
	 */
	public static int getNumberOfDecimals(String code) {
		for (CurrencyType currencyType : values()) {
			if (StringUtils.equalsIgnoreCase(currencyType.getCode(), code)) {
				return currencyType.decimals;
			}
		}
		return DEFAULT_NUMBER_DECIMALS;
	}
}
