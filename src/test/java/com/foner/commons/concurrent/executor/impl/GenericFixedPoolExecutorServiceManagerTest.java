package com.foner.commons.concurrent.executor.impl;

import com.foner.commons.AbstractTest;
import com.foner.commons.concurrent.executor.ExecutorServiceManager;
import com.foner.commons.concurrent.supplier.generic.impl.DefaultGenericSupplier;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * The class GenericFixedPoolExecutorServiceManagerTest.
 *
 * @author <a href="mailto:josepdcs@gmail.com">Josep Carbonell</a>
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({GenericFixedPoolExecutorServiceManager.class, Logger.class})
public class GenericFixedPoolExecutorServiceManagerTest extends AbstractTest {

	/**
	 * Test of getInstance method, of class ExecutorServiceManager.
	 */
	@Test
	public void testGetInstance() {
		ExecutorServiceManager executorServiceManager = GenericFixedPoolExecutorServiceManager.getInstance();
		MatcherAssert.assertThat(executorServiceManager, Matchers.notNullValue());
		MatcherAssert.assertThat(executorServiceManager, IsInstanceOf.instanceOf(ExecutorServiceManager.class));
	}

	/**
	 * Test of getExecutorService method, of class ExecutorServiceManager.
	 */
	@Test
	public void testGetExecutorService() {
		ExecutorServiceManager executorServiceManager = GenericFixedPoolExecutorServiceManager.getInstance();
		ExecutorService executorService = executorServiceManager.getExecutorService();

		MatcherAssert.assertThat(executorService, Matchers.notNullValue());
		MatcherAssert.assertThat(executorService, IsInstanceOf.instanceOf(ExecutorService.class));
		MatcherAssert.assertThat(((GenericFixedPoolExecutorServiceManager) executorServiceManager).getPoolSize(), Matchers.equalTo(10));
	}

	/**
	 * Test of getExecutorService method, of class ExecutorServiceManager with given pool size property.
	 */
	@Test
	public void testGetExecutorServiceWithGivenPoolSizeProperty() {
		System.setProperty(GenericFixedPoolExecutorServiceManager.GENERIC_FIXED_POOL_SIXE, "20");
		ExecutorServiceManager executorServiceManager = GenericFixedPoolExecutorServiceManager.getInstance();
		int previousPoolSixe = ((GenericFixedPoolExecutorServiceManager) executorServiceManager).getPoolSize();
		ExecutorService executorService = executorServiceManager.getExecutorService();

		MatcherAssert.assertThat(executorService, Matchers.notNullValue());
		MatcherAssert.assertThat(executorService, IsInstanceOf.instanceOf(ExecutorService.class));
		MatcherAssert.assertThat(((GenericFixedPoolExecutorServiceManager) executorServiceManager).getPoolSize(), Matchers.equalTo(20));
		System.setProperty(GenericFixedPoolExecutorServiceManager.GENERIC_FIXED_POOL_SIXE, String.valueOf(previousPoolSixe));
	}

	/**
	 * Test simple completable future
	 * 
	 * @throws java.lang.InterruptedException
	 * @throws java.util.concurrent.ExecutionException
	 */
	@Test
	public void testSimpleCompletableFuture() throws InterruptedException, ExecutionException {
		ExecutorService executorService = GenericFixedPoolExecutorServiceManager.getInstance().getExecutorService();

		CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
			try {
				TimeUnit.SECONDS.sleep(1);
				logger.info("Running in async method...");
			} catch (InterruptedException e) {
				throw new IllegalStateException(e);
			}
			return "Result of the asynchronous computation";
		}, executorService);

		String result = completableFuture.get();
		MatcherAssert.assertThat(result, Matchers.equalTo("Result of the asynchronous computation"));

	}

	/**
	 * Test simple completable future with default generic supplier
	 * 
	 * @throws java.lang.InterruptedException
	 * @throws java.util.concurrent.ExecutionException
	 */
	@Test
	public void testSimpleCompletableFutureWithDefaultGenericSupplier() throws InterruptedException, ExecutionException {
		ExecutorService executorService = GenericFixedPoolExecutorServiceManager.getInstance().getExecutorService();
		DefaultGenericSupplier<SimpleLogicClass, String> defaultGenericSupplier = new DefaultGenericSupplier<>(new SimpleLogicClass(), "getResult");
		CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(defaultGenericSupplier, executorService);

		String result = completableFuture.get();
		MatcherAssert.assertThat(result, Matchers.equalTo("Result of the asynchronous computation"));

	}

	/**
	 * Test simple completable future with default generic supplier
	 * 
	 * @throws java.lang.InterruptedException
	 * @throws java.util.concurrent.ExecutionException
	 */
	@Test
	public void testSimpleCompletableFutureWithDefaultGenericSupplierAndThenApply() throws InterruptedException, ExecutionException {
		ExecutorService executorService = GenericFixedPoolExecutorServiceManager.getInstance().getExecutorService();
		DefaultGenericSupplier<SimpleLogicClass, String> defaultGenericSupplier = new DefaultGenericSupplier<>(new SimpleLogicClass(), "getResult");
		CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(defaultGenericSupplier, executorService);

		completableFuture = completableFuture.thenApply(name -> {
			return "START " + name;
		});

		String result = completableFuture.get();
		MatcherAssert.assertThat(result, Matchers.equalTo("START Result of the asynchronous computation"));

	}

}
