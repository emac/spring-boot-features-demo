package cn.emac.demo.petstore.common.retrofit;

import cn.emac.demo.petstore.common.JsonResult;
import cn.emac.demo.petstore.common.hystrix.BaseJsonHystrixCommand;
import cn.emac.demo.petstore.common.hystrix.HystrixCommandConfig;
import cn.emac.demo.petstore.common.hystrix.HystrixThreadConfig;
import cn.emac.demo.petstore.common.utils.ClientExceptionUtils;

import java.util.concurrent.CompletableFuture;

/**
 * @author Emac
 * @since 2016-09-27
 */
public class BaseClient<T> {

    protected final T api;
    protected final HystrixCommandConfig commandConfig;
    protected final HystrixThreadConfig threadConfig;

    public BaseClient(T api,
                      HystrixCommandConfig commandConfig,
                      HystrixThreadConfig threadConfig){
        this.api = api;
        this.commandConfig = commandConfig;
        this.threadConfig = threadConfig;
    }

    /**
     * 同步执行命令
     */
    protected static <R> JsonResult<R> executeCommand(BaseJsonHystrixCommand<R> command) {
        try {
            return command.execute();
        } catch (Exception e) {
            throw ClientExceptionUtils.toClientException(e);
        }
    }

    /**
     * 异步执行命令
     */
    protected static <R> CompletableFuture<JsonResult<R>> asyncExecuteCommand(BaseJsonHystrixCommand<R> command) {
        try {
            return command.asyncExecute();
        } catch (Exception e) {
            return ClientExceptionUtils.toCompleteableFuture(e);
        }
    }
}
