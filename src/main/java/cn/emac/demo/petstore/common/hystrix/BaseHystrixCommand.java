package cn.emac.demo.petstore.common.hystrix;

import cn.emac.demo.petstore.common.exceptions.ClientException;
import cn.emac.demo.petstore.common.utils.ClientExceptionUtils;
import com.netflix.hystrix.*;
import retrofit2.Response;
import rx.Observable;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * Hystrix Command基类
 *
 * @author Emac
 * @since 2016-09-27
 */
public abstract class BaseHystrixCommand<R> extends HystrixCommand<R> {

    /**
     * @param commandConfig
     * @param threadConfig
     */
    public BaseHystrixCommand(HystrixCommandConfig commandConfig, HystrixThreadConfig threadConfig) {
        this(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(commandConfig.getGroupKey()))
                .andCommandKey(HystrixCommandKey.Factory.asKey(commandConfig.getCommandKey()))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withCircuitBreakerEnabled(commandConfig.getCircuitBreakerEnabled())
                        .withExecutionTimeoutEnabled(commandConfig.getExecutionTimeoutEnabled())
                        .withExecutionTimeoutInMilliseconds(commandConfig.getExecutionTimeoutInMilliseconds())
                        .withMetricsRollingStatisticalWindowInMilliseconds(commandConfig.getMetricsTimeInMilliseconds())
                        .withMetricsRollingPercentileWindowInMilliseconds(commandConfig.getMetricsTimeInMilliseconds())
                        .withMetricsRollingStatisticalWindowBuckets(commandConfig.getMetricsNumBuckets())
                        .withMetricsRollingPercentileBucketSize(commandConfig.getMetricsNumBuckets())
                        .withCircuitBreakerRequestVolumeThreshold(commandConfig.getCircuitBreakerRequestVolumeThreshold())
                        .withCircuitBreakerSleepWindowInMilliseconds(commandConfig.getCircuitBreakerSleepWindowInMilliseconds())
                        .withCircuitBreakerErrorThresholdPercentage(commandConfig.getCircuitBreakerErrorThresholdPercentage()))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey(threadConfig.getGroupKey()))
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
                        .withCoreSize(threadConfig.getCoreSize())
                        .withMaximumSize(threadConfig.getMaximumSize())
                        .withKeepAliveTimeMinutes(threadConfig.getKeepAliveTimeMinutes())
                        .withAllowMaximumSizeToDivergeFromCoreSize(threadConfig.getAllowMaximumSizeToDivergeFromCoreSize())
                        .withMaxQueueSize(threadConfig.getMaxQueueSize())
                        .withQueueSizeRejectionThreshold(threadConfig.getQueueSizeRejectionThreshold())));
    }

    /**
     * @param setter
     */
    public BaseHystrixCommand(Setter setter) {
        super(setter);
    }

    @Override
    protected R run() throws IOException {
        Response<R> response = doApiCall();
        if (response.isSuccessful()) {
            return response.body();
        }

        throw new ClientException("Server responds with " + response.code() + ": " + response.errorBody());
    }

    /**
     * 调用API
     *
     * @return
     * @throws IOException
     */
    protected abstract Response<R> doApiCall() throws IOException;

    /**
     * 异步执行，返回Future对象
     *
     * @return
     */
    public CompletableFuture<R> asyncExecute() {
        Observable<R> observable = super.toObservable();
        return fromObservable(observable);
    }

    private CompletableFuture<R> fromObservable(Observable<R> observable) {
        final CompletableFuture<R> future = new CompletableFuture<>();
        observable.single().doOnError(e -> {
            // 把异常统一封装到CompletableFuture里
            ClientException clientException = ClientExceptionUtils.toClientException(e);
            future.completeExceptionally(clientException);
        }).forEach(future::complete);
        return future;
    }
}
