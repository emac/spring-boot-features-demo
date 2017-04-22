package cn.emac.demo.petstore.components;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.Callable;

/**
 * @author Emac
 * @since 2016-04-15
 */
@Component
@Async
public class AsyncExecutor {

    @Async
    public ListenableFuture<String> delay(long delay, String result) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            // ignore
        }
        return AsyncResult.forValue(result);
    }

    /**
     * Invokes the given callable asynchronously.
     *
     * @param callable
     * @param <T>
     * @return
     */
    public <T> ListenableFuture<T> invoke(Callable<T> callable) {
        try {
            return AsyncResult.forValue(callable.call());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
