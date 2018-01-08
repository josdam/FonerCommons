package com.jumbotours.commons.concurrent.worker.generic.impl;

import com.jumbotours.commons.MethodParameter;
import com.jumbotours.commons.concurrent.worker.generic.IGenericWorker;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * The class GenericWorker.
 * 
 * @param <T>
 *            the generic type
 * @author Josep Carbonell
 */
public class GenericWorker<T> implements IGenericWorker<T>, Serializable {

	/** The Constant serialVersionUID. */
	private static final long		serialVersionUID	= -1796233873145607876L;

	/** The logger. */
	private static final Logger		logger				= Logger.getLogger(GenericWorker.class);

	/** The instance. */
	private T						instance;

	/** The method name. */
	private String					methodName;

	/** The method parameters. */
	private List<MethodParameter>	methodParameters	= new ArrayList<>(0);

	/**
	 * Instantiates a new launcher.
	 * 
	 * @param instance
	 *            the instance
	 * @param methodName
	 *            the method name
	 */
	public GenericWorker(T instance, String methodName) {
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
	 * Gets the method parameters.
	 * 
	 * @return the method parameters
	 */
	public List<MethodParameter> getMethodParameters() {
		return methodParameters;
	}

	/**
	 * Sets the method parameters.
	 * 
	 * @param methodParameters
	 *            the new method parameters
	 */
	public void setMethodParameters(List<MethodParameter> methodParameters) {
		this.methodParameters = methodParameters;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		if ((instance != null) && StringUtils.isNotEmpty(methodName)) {
			String taskName = instance.getClass().getName() + "." + methodName;
			logger.debug("Running generic task: " + taskName);
			long time = System.currentTimeMillis();
			try {
				if (methodParameters.isEmpty()) {
					Method method = instance.getClass().getMethod(methodName);
					method.invoke(instance);
				} else {
					Class<?> types[] = new Class<?>[methodParameters.size()];
					Object values[] = new Object[methodParameters.size()];
					int i = 0;
					for (MethodParameter methodParameter : methodParameters) {
						if (methodParameter.getType() != null) {
							types[i] = methodParameter.getType();
						}
						if (methodParameter.getValue() != null) {
							values[i] = methodParameter.getValue();
						}
						if (methodParameter.getValues() != null) {
							values[i] = methodParameter.getValues();
						}
						i++;
					}
					Method method = instance.getClass().getMethod(methodName, types);
					method.invoke(instance, values);
				}
			} catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException ex) {
				logger.error(ex, ex);
			}
			long elapsed = System.currentTimeMillis() - time;
			logger.debug("Executed generic task " + taskName + " in " + elapsed + " ms.");
		}
	}
}
