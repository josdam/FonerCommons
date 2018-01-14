package com.foner.commons.cipher;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

/**
 * The Class CipherUtil.
 */
public final class CipherUtil {

	/** The logger. */
	private static final Logger	logger		= Logger.getLogger(CipherUtil.class);

	/** The constant HMAC_SHA256. */
	public final static String	HMAC_SHA256	= "HmacSHA256";

	/**
	 * Hide default constructor.
	 */
	private CipherUtil() {}

	/**
	 * AES encode.
	 * 
	 * @param keyValue
	 *            the key value
	 * @param valueToEncode
	 *            the value to encode
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public static String AESEncode(byte[] keyValue, String valueToEncode) throws Exception {
		Key key = generateKey(keyValue, "AES");
		Cipher c = Cipher.getInstance("AES");
		c.init(Cipher.ENCRYPT_MODE, key);
		byte[] encValue = c.doFinal(valueToEncode.getBytes());
		String encryptedValue = URLEncoder.encode(new String(Base64.encodeBase64(encValue)), "UTF8");

		return encryptedValue;
	}

	/**
	 * AES decode.
	 * 
	 * @param keyValue
	 *            the key value
	 * @param valueToDecode
	 *            the value to decode
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public static String AESDecode(byte[] keyValue, String valueToDecode) throws Exception {
		Key key = generateKey(keyValue, "AES");
		Cipher c = Cipher.getInstance("AES");
		c.init(Cipher.DECRYPT_MODE, key);
		String urlDecodedValue = URLDecoder.decode(valueToDecode, "UTF8");
		byte[] decodedValue = Base64.decodeBase64(urlDecodedValue.getBytes());
		byte[] decValue = c.doFinal(decodedValue);
		String decryptedValue = new String(decValue);

		return decryptedValue;
	}

	/**
	 * AES decode.
	 * 
	 * @param key
	 *            the key value
	 * @param data
	 *            the data
	 * @param algorithm
	 *            the algorithm
	 * @return the array of bytes
	 * @throws Exception
	 *             the exception
	 */
	public static byte[] doHMAC(byte[] key, String data, String algorithm) throws Exception {
		Mac mac = Mac.getInstance(algorithm);
		SecretKeySpec secretKeySpec = new SecretKeySpec(key, algorithm);
		mac.init(secretKeySpec);
		return mac.doFinal(data.getBytes());
	}

	/**
	 * Generate key.
	 * 
	 * @param keyValue
	 *            the key value
	 * @param algorithm
	 *            the algorithm
	 * @return the key
	 * @throws Exception
	 *             the exception
	 */
	private static Key generateKey(byte[] keyValue, String algorithm) throws Exception {
		Key key = new SecretKeySpec(keyValue, algorithm);
		return key;
	}
}
