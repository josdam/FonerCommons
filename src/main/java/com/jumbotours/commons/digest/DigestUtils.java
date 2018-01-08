package com.jumbotours.commons.digest;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

/**
 * The class DigestUtil.
 *
 * @author Josep Carbonell
 */
public final class DigestUtils {

	private static final Logger	logger	= Logger.getLogger(DigestUtils.class);

	/** The constant MD5. */
	public final static String	MD5		= "MD5";

	/** The constant SHA1. */
	public final static String	SHA1	= "SHA1";

	/** The constant SHA512. */
	public final static String	SHA512	= "SHA-512";

	/**
	 * Hide default constructor.
	 */
	private DigestUtils() {}

	/**
	 * Md5 encode.
	 * 
	 * @param valueToEncode
	 *            the value to encode
	 * @return the string
	 */
	public static String MD5Encode(byte[] valueToEncode) {
		String returnData = null;

		try {
			MessageDigest algorithm = MessageDigest.getInstance(MD5);
			algorithm.reset();
			algorithm.update(valueToEncode);
			byte messageDigest[] = algorithm.digest();
			returnData = bytes2String(messageDigest);
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage());
		}

		return returnData;
	}

	/**
	 * Gets the message digest.
	 * 
	 * @param data
	 *            the data
	 * @return the message digest
	 * @throws Exception
	 *             the jwlp util exception
	 */
	public static String getMessageDigest(String data) throws Exception {
		String returnData = null;
		try {
			MessageDigest md = MessageDigest.getInstance(SHA1);
			byte[] digest = md.digest(data.getBytes());
			returnData = bytes2String(digest);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception(e.getMessage());
		}
		return returnData;
	}

	/**
	 * Gets the message digest as a string in base 64 format.
	 * 
	 * @param data
	 *            the data
	 * @param algorithm
	 *            the algorithm
	 * @return the message digest
	 * @throws Exception
	 *             the jwlp util exception
	 */
	public static String getBase64MessageDigest(String data, String algorithm) throws Exception {
		String returnData = null;
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);
			byte[] digest = md.digest(data.getBytes());
			returnData = new String(Base64.encodeBase64(digest), "UTF-8");
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			throw new Exception(e.getMessage());
		}
		return returnData;
	}

	/**
	 * Bytes2 string.
	 * 
	 * @param bytes
	 *            the bytes
	 * @return the string
	 */
	private static String bytes2String(byte[] bytes) {
		StringBuilder string = new StringBuilder();
		for (byte b : bytes) {
			String hexString = Integer.toHexString(0x00FF & b);
			string.append(hexString.length() == 1 ? "0" + hexString : hexString);
		}
		return string.toString();
	}
}
