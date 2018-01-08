package com.jumbotours.commons.concurrent.executor;

import java.io.Serializable;
import java.util.concurrent.ExecutorService;

/**
 * The Interface IExecutorServiceManager.
 * 
 * @author Josep Carbonell
 */
public interface IExecutorServiceManager extends Serializable {

    /**
     * Gets the executor service.
     * 
     * @return the executor service
     */
    ExecutorService getExecutorService();
}
