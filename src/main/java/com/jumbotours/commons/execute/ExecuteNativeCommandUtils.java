package com.jumbotours.commons.execute;

import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 * The class ExecuteNativeCommandUtil.
 *
 * @author Josep Carbonell
 */
public final class ExecuteNativeCommandUtils {

	/** The logger. */
	private static final Logger logger = Logger.getLogger(ExecuteNativeCommandUtils.class);

	/**
	 * Hides default constructor.
	 */
	private ExecuteNativeCommandUtils() {}

	/**
	 * Instance new ExecuteNativeCommandUtil.
	 * 
	 * @return new ExecuteNativeCommandUtil instance
	 */
	public static ExecuteNativeCommandUtils newInstance() {
		return new ExecuteNativeCommandUtils();
	}

	/**
	 * Executes the given command and return the result of its execution.
	 * 
	 * @param command
	 *            the command to be execute
	 * @return the result of executing the given command
	 * @throws java.lang.Exception
	 */
	public static String execute(String command) throws Exception {
		String output = null;

		Process process;
		InputStream inputStream = null;
		try {
			logger.info("Command to be executed: " + command);
			process = Runtime.getRuntime().exec(command);
			process.waitFor();
			inputStream = process.getInputStream();
			output = IOUtils.toString(inputStream, "UTF-8");
			logger.info("Result of executed command: " + output);
		} finally {
			IOUtils.closeQuietly(inputStream);
		}

		return output;
	}

}
