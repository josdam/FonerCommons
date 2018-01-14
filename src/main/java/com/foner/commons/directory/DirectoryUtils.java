package com.foner.commons.directory;

import com.foner.commons.slugify.SlugifyUtils;
import java.io.File;
import java.io.UnsupportedEncodingException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * The DirsUtil Class.
 * 
 * @author Manuel Vidal, Josep Carbonell
 */
public final class DirectoryUtils {

	/** The constant POINT_SEPARATOR. */
	private static final String	POINT_SEPARATOR	= ".";

	/** The constant UNDERSCORE. */
	private static final String	UNDERSCORE		= "_";

	/**
	 * Hide default constructor.
	 */
	private DirectoryUtils() {}

	/**
	 * Number to dirs.
	 * 
	 * @param path
	 *            the path
	 * @param number
	 *            the number
	 * @return the generated directory
	 */
	public static String number2Dirs(String path, String number) {
		String generatedPath;
		int div = Integer.parseInt(number) / 10;
		int mod = Integer.parseInt(number) % 10;
		StringBuilder builder = new StringBuilder();
		while (div >= 10) {
			builder.append(mod);
			builder.append(System.getProperty("file.separator"));
			mod = div % 10;
			div /= 10;
		}
		builder.append(mod);
		if (div > 0) {
			builder.append(System.getProperty("file.separator"));
			builder.append(div);
		}

		StringBuilder builder2 = new StringBuilder();
		builder2.append(path);
		builder2.append(System.getProperty("file.separator"));
		builder2.append(StringUtils.reverse(builder.toString()));

		generatedPath = builder2.toString();

		File file = new File(generatedPath);
		file.mkdirs();

		return generatedPath;
	}

	/**
	 * Move file from directory source to destination.
	 * 
	 * @param sourceDir
	 *            source directory
	 * @param destinationDir
	 *            destination directory
	 * @return
	 * 		the moved file names
	 */
	public static String[] moveFiles(String sourceDir, String destinationDir) {

		String[] fileNames;

		// Create destination directory
		File desDir = new File(destinationDir);
		desDir.mkdirs();

		// Getting source files
		File sourDir = new File(sourceDir);
		fileNames = sourDir.list();

		// Move file to destination directory
		for (String fileName : fileNames) {
			File file = new File(sourDir, fileName);
			file.renameTo(new File(desDir, fileName));
		}

		return fileNames;
	}

	/**
	 * Clean directory excluding the passed files.
	 * 
	 * @param directory
	 *            the directory
	 * @param filesToExclude
	 *            the files to exclude
	 */
	public static void cleanDirectory(String directory, String[] filesToExclude) {
		// Getting source files
		File sourDir = new File(directory);
		String[] fileNames = sourDir.list();

		// Remove files not exclude
		if (fileNames != null) {
			for (String fileName : fileNames) {
				if (!ArrayUtils.contains(filesToExclude, fileName)) {
					File file = new File(directory, fileName);
					file.delete();
				}
			}
		}
	}

	/**
	 * Deletes directory
	 * 
	 * @param path
	 *            the path to delete
	 * @return true, if successful
	 */
	public static boolean deleteDirectory(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					deleteDirectory(file);
				} else {
					file.delete();
				}
			}
		}
		return (path.delete());
	}

	/**
	 * Normalize the file names for scp.
	 * 
	 * @param directory
	 *            the directory
	 * @throws UnsupportedEncodingException
	 *             the unsupported encoding exception
	 */
	public static void normalizaFileNames(String directory) throws UnsupportedEncodingException {
		normalizaFileNames(directory, null);
	}

	/**
	 * Normalize the file names for scp.
	 * 
	 * @param directory
	 *            the directory
	 * @param languageCode
	 *            the language code
	 * @throws UnsupportedEncodingException
	 *             the unsupported encoding exception
	 */
	public static void normalizaFileNames(String directory, String languageCode) throws UnsupportedEncodingException {
		// Gets the source files
		File sourDir = new File(directory);
		String[] fileNames = sourDir.list();

		// Normaliza the files name
		if (fileNames != null) {
			File file;
			File newFile;
			String newFileName;
			for (String fileName : fileNames) {
				newFileName = normalizaFileName(fileName, languageCode);
				file = new File(directory, fileName);
				newFile = new File(directory, newFileName);
				file.renameTo(newFile);
			}
		}
	}

	/**
	 * Normalize the file name for scp.
	 * 
	 * @param name
	 *            the name
	 * @return the normalized name
	 * @throws UnsupportedEncodingException
	 *             the unsupported encoding exception
	 */
	public static String normalizaFileName(String name) throws UnsupportedEncodingException {
		return normalizaFileName(name, null);
	}

	/**
	 * Normalize the file name for scp.
	 * 
	 * @param name
	 *            the name
	 * @param languageCode
	 *            the language code
	 * @return the normalized name
	 * @throws UnsupportedEncodingException
	 *             the unsupported encoding exception
	 */
	public static String normalizaFileName(String name, String languageCode) throws UnsupportedEncodingException {
		StringBuilder builder = new StringBuilder();
		builder.append(SlugifyUtils.slugify(StringUtils.substringBeforeLast(name, POINT_SEPARATOR)));
		if (StringUtils.isNotEmpty(languageCode)) {
			builder.append(UNDERSCORE).append(StringUtils.lowerCase(languageCode));
		}
		builder.append(POINT_SEPARATOR);
		builder.append(StringUtils.substringAfterLast(name, POINT_SEPARATOR));
		return builder.toString();
	}

	/**
	 * Checks if the directory is not empty
	 * 
	 * @param directory
	 *            the directory
	 * @return true, if the directory is not empty
	 */
	public static boolean isNotEmptyDirectory(String directory) {
		File sourDir = new File(directory);
		String[] fileNames = sourDir.list();
		return (fileNames != null) && (fileNames.length > 0);
	}

	/**
	 * Builds path from given folders.
	 * 
	 * @param folders
	 *            the list of folders
	 * @return the built path
	 */
	public static String buildPathFromFolders(String... folders) {
		StringBuilder builder = new StringBuilder();
		for (String folder : folders) {
			if (StringUtils.isNotBlank(folder)) {
				if (!StringUtils.startsWith(folder, File.separator) && !StringUtils.endsWith(builder.toString(), File.separator)) {
					builder.append(File.separator);
				}
				builder.append(folder);
			}
		}
		return builder.toString();
	}
}
