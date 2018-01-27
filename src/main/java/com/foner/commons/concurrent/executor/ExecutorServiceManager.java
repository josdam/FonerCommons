package com.foner.commons.concurrent.executor;

import java.io.Serializable;
import java.util.concurrent.ExecutorService;

/**
 * The Interface ExecutorServiceManager.
 * 
 * @author <a href="mailto:josepdcs@gmail.com">Josep Carbonell</a>
 */
public interface ExecutorServiceManager extends Serializable {

    /**
     * Gets the executor service.
     * 
     * @return the executor service
     */
    ExecutorService getExecutorService();
}
