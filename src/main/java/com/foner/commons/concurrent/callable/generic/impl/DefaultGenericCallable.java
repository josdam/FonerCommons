package com.foner.commons.concurrent.callable.generic.impl;

import com.foner.commons.Parameter;
import com.foner.commons.concurrent.callable.generic.GenericCallable;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * The class DefaultGenericCallable.
 * 
 * @param <T>
 *            the generic type
 * @author <a href="mailto:josepdcs@gmail.com">Josep Carbonell</a>
 */
public class DefaultGenericCallable<T> implements GenericCallable<Object>, Serializable {

	/** The Constant serialVersionUID. */
	private static final long	serialVersionUID	= -1796233873145607876L;

	/** The logger. */
	private static final Logger	logger				= Logger.getLogger(GenericCallable.class);

	/** The instance. */
	private T					instance;

	/** The method name. */
	private String				methodName;

	/** The parameters. */
	private List<Parameter>		parameters			= new ArrayList<>(0);

	/**
	 * Instantiates a new launcher.
	 * 
	 * @param instance
	 *            the instance
	 * @param methodName
	 *            the method name
	 */
	public DefaultGenericCallable(T instance, String methodName) {
		this.instance = instance;
		this.methodName = methodName;
	}

	/**
	 * Gets the single instance of Launcher.
	 * 
	 * @return single instance of Launcher
	 */
	public T getInstance() {
		return instance;
	}

	/**
	 * Sets the instance.
	 * 
	 * @param instance
	 *            the new instance
	 */
	public void setInstance(T instance) {
		this.instance = instance;
	}

	/**
	 * Gets the method name.
	 * 
	 * @return the method name
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * Sets the method name.
	 * 
	 * @param methodName
	 *            the new method name
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	/**
	 * Gets the parameters.
	 * 
	 * @return the parameters
	 */
	public List<Parameter> getParameters() {
		return parameters;
	}

	/**
	 * Sets the parameters.
	 * 
	 * @param parameters
	 *            the new parameters
	 */
	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public Object call() throws Exception {
		Object o = null;
		if ((instance != null) && StringUtils.isNotEmpty(methodName)) {
			String taskName = instance.getClass().getName() + "." + methodName;
			logger.debug("Running generic callable task: " + taskName);
			long time = System.currentTimeMillis();
			try {
				if (parameters.isEmpty()) {
					Method method = instance.getClass().getMethod(methodName);
					method.invoke(instance);
				} else {
					Class<?> types[] = new Class<?>[parameters.size()];
					Object values[] = new Object[parameters.size()];
					int i = 0;
					for (Parameter parameter : parameters) {
						types[i] = parameter.getType();
						values[i] = parameter.getValue();
						i++;
					}
					Method method = instance.getClass().getMethod(methodName, types);
					o = method.invoke(instance, values);
				}
			} catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException ex) {
				logger.error(ex, ex);
			}
			long elapsed = System.currentTimeMillis() - time;
			logger.debug("Executed generic callable task " + taskName + " in " + elapsed + " ms.");
		}

		return o;
	}

}
