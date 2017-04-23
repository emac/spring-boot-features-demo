package cn.emac.demo.petstore.common.utils;

import cn.emac.demo.petstore.common.exceptions.ClientCallException;
import cn.emac.demo.petstore.common.exceptions.ClientException;

import java.util.concurrent.CompletableFuture;

/**
 * @author Emac
 * @since 2016-09-29
 */
public class ClientExceptionUtils {

    /**
     * 将指定异常转换为{@code ClientException}
     */
    public static ClientException toClientException(Throwable e) {
        if (e instanceof ClientCallException) {
            return (ClientException) e;
        }

        return new ClientException("Remote call failed.", e);
    }

    /**
     * 将指定异常转换为{@code ClientException}并封装为{@code CompletableFuture}
     */
    public static <T> CompletableFuture<T> toCompleteableFuture(Throwable e) {
        ClientException convertedException = toClientException(e);
        CompletableFuture<T> future = new CompletableFuture<>();
        future.completeExceptionally(convertedException);
        return future;
    }
}
