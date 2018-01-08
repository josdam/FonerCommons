package com.jumbotours.commons.price;

import com.jumbotours.commons.currency.type.CurrencyType;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * The class PriceUtil.
 * 
 * @author Josep Carbonell
 */
public final class PriceUtils {

	/** Hides default constructor. */
	private PriceUtils() {}

	/**
	 * Apply markup on purchase amount.
	 * 
	 * @param markupPercentage
	 *            the markup percentage
	 * @param markupAmount
	 *            the markup amount
	 * @param purchaseAmount
	 *            the purchase amount
	 * @return the new amount with markup applied
	 */
	public static double applyMarkup(Double markupPercentage, Double markupAmount, double purchaseAmount) {
		double amount = purchaseAmount;

		if (markupPercentage != null) {
			amount = purchaseAmount * (1 + markupPercentage / 100.0); // selling price
		}
		if (markupAmount != null) {
			amount = purchaseAmount + markupAmount; // selling price
		}

		return amount;
	}

	/**
	 * Apply tax percentage on amount.
	 * 
	 * @param amount
	 *            the amount
	 * @param percentage
	 *            the percentage
	 * @return the new amount with percentage applied
	 */
	public static double applyPercentage(Double amount, Double percentage) {
		double result = amount;

		if (percentage != null) {
			result = amount * (1 + percentage / 100.00);
		}

		return result;
	}

	/**
	 * Gets percentage on amount.
	 * 
	 * @param amount
	 *            the amount
	 * @param percentage
	 *            the pax percentage
	 * @return the percentage on amount
	 */
	public static double getPercentage(Double amount, Double percentage) {
		double result = 0;

		if (percentage != null) {
			result = amount * (percentage / 100.00);
		}

		return result;
	}

	/**
	 * Gets the commission.
	 * 
	 * 
	 * @param commissionPercentage
	 *            the commission percentage
	 * @param taxPercentage
	 *            the tax percentage
	 * @param amount
	 *            the amount
	 * @return the commission
	 */
	public static double getCommission(Double commissionPercentage, Double taxPercentage, double amount) {
		double result = 0.0;

		if (commissionPercentage != null) {
			result = applyPercentage(amount * (commissionPercentage / 100), taxPercentage);
		}

		return result;
	}

	/**
	 * Gets the scaled up price from given parameters
	 * 
	 * @param price
	 *            the price.
	 * @param currencyCode
	 *            the currency code.
	 * @return the scaled up price.
	 */
	public static double getScaledUpPrice(double price, String currencyCode) {
		BigDecimal big = new BigDecimal(price);
		big = big.setScale(CurrencyType.getNumberOfDecimals(currencyCode), RoundingMode.HALF_UP);
		return big.doubleValue();
	}
}
