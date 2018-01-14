package com.foner.commons.springframework;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.web.context.WebApplicationContext;

/**
 * The class SpringBeanLookupUtil.
 *
 * @author Josep Carbonell
 */
public final class SpringBeanLookupManager {

	/** The logger. */
	private static final Logger						logger		= Logger.getLogger(SpringBeanLookupManager.class);

	/** The INSTANCE. */
	private static final SpringBeanLookupManager	instance	= new SpringBeanLookupManager();

	/** The web application context. */
	private WebApplicationContext					webApplicationContext;

	/**
	 * Hides default constructor.
	 */
	private SpringBeanLookupManager() {}

	/**
	 * Gets the single instance of SpringBeanLookupUtil.
	 * 
	 * @return single instance of SpringBeanLookupUtil
	 */
	public static SpringBeanLookupManager getInstance() {
		return instance;
	}

	/**
	 * Gets the web application context.
	 * 
	 * @return the web application context
	 */
	public synchronized WebApplicationContext getWebApplicationContext() {
		return webApplicationContext;
	}

	/**
	 * Sets the web application context.
	 * 
	 * @param webApplicationContext
	 *            the new web application context
	 */
	public synchronized void setWebApplicationContext(WebApplicationContext webApplicationContext) {
		this.webApplicationContext = webApplicationContext;
	}

	/**
	 * Look up service.
	 * 
	 * @param qualifierName
	 *            the qualifier name
	 * @return the i handler
	 */
	public synchronized ISpringBean lookUpService(String qualifierName) {
		ISpringBean springBeanInstance = null;

		try {
			springBeanInstance = (ISpringBean) getWebApplicationContext().getBean(qualifierName);
		} catch (BeansException e) {
			logger.warn("Spring bean not found: " + e, e);
		}

		return springBeanInstance;
	}

	/**
	 * Look up service.
	 * 
	 * @param springBean
	 *            the handler
	 * @return the i handler
	 */
	public synchronized ISpringBean lookUpService(Class<? extends ISpringBean> springBean) {

		ISpringBean springBeanInstance = null;

		try {
			springBeanInstance = getWebApplicationContext().getBean(springBean);
		} catch (BeansException e) {
			logger.warn("Spring bean not found: " + e, e);
		}

		return springBeanInstance;
	}
}
