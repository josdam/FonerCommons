package com.foner.commons.execute;

import com.foner.commons.exception.FonerCommonException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 * The class ExecuteNativeCommandUtil.
 *
 * @author <a href="mailto:josepdcs@gmail.com">Josep Carbonell</a>
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
	 * @throws FonerCommonException
	 *             the jumbo common exception
	 */
	public static String execute(String command) throws FonerCommonException {
		String output = null;

		Process process;
		InputStream inputStream = null;
		try {
			logger.info("Command to be executed: " + command);
			process = Runtime.getRuntime().exec(command);
			process.waitFor();
			inputStream = process.getInputStream();
			output = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
			logger.info("Result of executed command: " + output);
		} catch (IOException | InterruptedException e) {
			throw new FonerCommonException(e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					throw new FonerCommonException(e);
				}
			}
		}

		return output;
	}

}
