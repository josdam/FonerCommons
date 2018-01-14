package com.foner.commons.concurrent.callable.generic.impl;

import com.foner.commons.MethodParameter;
import com.foner.commons.concurrent.callable.generic.IGenericCallable;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * The class GenericCallable.
 * 
 * @param <T>
 *            the generic type
 * @author <a href="mailto:josepdcs@gmail.com">Josep Carbonell</a>
 */
public class GenericCallable<T> implements IGenericCallable<Object>, Serializable {

	/** The Constant serialVersionUID. */
	private static final long		serialVersionUID	= -1796233873145607876L;

	/** The logger. */
	private static final Logger		logger				= Logger.getLogger(GenericCallable.class);

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
	public GenericCallable(T instance, String methodName) {
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
				if (methodParameters.isEmpty()) {
					Method method = instance.getClass().getMethod(methodName);
					method.invoke(instance);
				} else {
					Class<?> types[] = new Class<?>[methodParameters.size()];
					Object values[] = new Object[methodParameters.size()];
					int i = 0;
					for (MethodParameter methodParameter : methodParameters) {
						types[i] = methodParameter.getType();
						values[i] = methodParameter.getValue();
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
