package com.foner.commons.zip;

import com.google.common.io.Files;
import com.foner.commons.http.HttpMethodType;
import com.foner.commons.http.OkHttpClientUtils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.log4j.Logger;

/**
 * The class ZipUtil.
 *
 * @author <a href="mailto:josepdcs@gmail.com">Josep Carbonell</a>
 */
public final class ZipUtils {

	/** The logger. */
	private static final Logger	logger	= Logger.getLogger(ZipUtils.class);

	/** The constant BUFFER. */
	private static final int	BUFFER	= 2048;

	/**
	 * Hides default constructor.
	 */
	private ZipUtils() {}

	/**
	 * Downloads the file from each given URLs and returns all them packed into a zip byte array
	 * 
	 * @param urls
	 *            the Map of urls (key = fileName, value = url to download this file)
	 * @return gets zip from list of urls
	 * @throws java.lang.Exception
	 */
	@SuppressWarnings("NestedAssignment")
	public static byte[] getZipFromUrls(Map<String, String> urls) throws Exception {
		byte[] result = null;

		FileOutputStream dest = null;
		ByteArrayInputStream bais = null;
		ZipOutputStream out = null;
		BufferedInputStream origin = null;
		File file = File.createTempFile("ZipUtil", ".zip");

		try {
			for (Map.Entry<String, String> entrySet : urls.entrySet()) {
				String fileName = entrySet.getKey();
				String url = entrySet.getValue();
				// download from resource
				byte[] download = OkHttpClientUtils.getResponseAsBytes(url, HttpMethodType.GET, null, null, null);
				bais = new ByteArrayInputStream(download);
				origin = new BufferedInputStream(bais, BUFFER);

				// temporal buffer
				byte buffer[] = new byte[BUFFER];

				// output
				if (dest == null) {
					dest = new FileOutputStream(file);
				}
				if (out == null) {
					out = new ZipOutputStream(new BufferedOutputStream(dest));
				}

				ZipEntry entry = new ZipEntry(fileName); // each filename
				out.putNextEntry(entry);

				int count;
				while ((count = origin.read(buffer, 0, BUFFER)) != -1) {
					out.write(buffer, 0, count);
				}
			}
		} catch (Exception ex) {
			logger.error("Error zipping file", ex);
			throw new Exception("Error zipping file", ex);
		} finally {
			if (origin != null) {
				try {
					origin.close();
				} catch (IOException ex) {
					logger.error("Error closing descriptor", ex);
				}
			}
			if (bais != null) {
				try {
					bais.close();
				} catch (IOException ex) {
					logger.error("Error closing descriptor", ex);
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException ex) {
					logger.error("Error closing descriptor", ex);
				}
			}
			if (dest != null) {
				try {
					dest.close();
				} catch (IOException ex) {
					logger.error("Error closing descriptor", ex);
				}
			}
		}

		try {
			result = Files.toByteArray(file);
			file.delete();
		} catch (IOException ex) {
			logger.error("Error reading temporally built zipfile", ex);
		}

		return result;
	}

}
