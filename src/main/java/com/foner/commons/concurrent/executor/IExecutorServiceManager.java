package com.foner.commons.concurrent.executor;

import java.io.Serializable;
import java.util.concurrent.ExecutorService;

/**
 * The Interface IExecutorServiceManager.
 * 
 * @author <a href="mailto:josepdcs@gmail.com">Josep Carbonell</a>
 */
public interface IExecutorServiceManager extends Serializable {

    /**
     * Gets the executor service.
     * 
     * @return the executor service
     */
    ExecutorService getExecutorService();
}
