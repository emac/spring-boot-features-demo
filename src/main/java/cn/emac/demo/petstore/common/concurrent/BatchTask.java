package cn.emac.demo.petstore.common.concurrent;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.google.common.collect.ImmutableMap.of;
import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.stream.Collectors.toList;

/**
 * 批处理任务执行器
 *
 * @author Emac
 * @since 2016-12-09
 */
@Slf4j
public class BatchTask {

    @Getter
    private final ExecutorService pool;

    private final String batchName;

    private final boolean autoShutdown;

    private final long timeout;

    private final TimeUnit unit;

    // 当任务出错的数量 > 总任务数 * 该百分比，则不再处理后续任务
    private final Integer errorThresholdPercentage;

    public BatchTask(String batchName) {
        this(1, batchName);
    }

    /**
     * @param nThreads  建议设置为CPU核心数的1~4倍，可获得最佳性能
     * @param batchName 批处理名称
     */
    public BatchTask(int nThreads, String batchName) {
        this(nThreads, batchName, false);
    }

    public BatchTask(int nThreads, String batchName, boolean autoShutdown) {
        this(nThreads, batchName, autoShutdown, 10);
    }

    public BatchTask(int nThreads, String batchName, boolean autoShutdown, Integer errorThresholdPercentage) {
        this(nThreads, batchName, autoShutdown, 1, MINUTES, errorThresholdPercentage);
    }

    private BatchTask(int nThreads, String batchName,
                      boolean autoShutdown, long timeout, TimeUnit unit,
                      Integer errorThresholdPercentage) {
        if (nThreads < 1) {
            throw new IllegalArgumentException("线程数必须大于等于1");
        }
        if (nThreads == 1) {
            this.pool = Executors.newSingleThreadExecutor();
        } else {
            this.pool = Executors.newFixedThreadPool(nThreads);
        }
        this.batchName = batchName;
        this.autoShutdown = autoShutdown;
        this.timeout = timeout;
        this.unit = unit;
        if (errorThresholdPercentage < 0 || errorThresholdPercentage > 100) {
            throw new IllegalArgumentException("错误百分比阈值超出取值范围[0-100]");
        }
        this.errorThresholdPercentage = errorThresholdPercentage;
    }

    public <T> void process(List<T> list, Consumer<T> handler) {
        process(list, handler, (t, e) -> {
        });
    }

    /**
     * 执行批处理(同步方法)
     * <p>
     * 注: 该方法会阻塞当前线程, 直到批处理完成
     *
     * @param list    待处理记录
     * @param handler 单条处理方法
     * @param onError 自定义单条处理异常后的行为
     */
    public <T> void process(List<T> list, Consumer<T> handler, BiConsumer<T, Throwable> onError) {
        if (pool.isShutdown()) {
            throw new RejectedExecutionException("线程池不可用");
        }

        long startTime = currentTimeMillis();
        log.info(format("%s querySize = %d", batchName, list.size()));

        AtomicInteger success = new AtomicInteger(0);
        AtomicInteger error = new AtomicInteger(0);
        Integer errorLimitInclude = list.size() * errorThresholdPercentage / 100;

        Collection<Callable<Object>> callables = list.stream().map(t -> (Callable<Object>) () -> {
            if (error.get() > errorLimitInclude) {
                return null;
            }
            try {
                handler.accept(t);
                success.getAndIncrement();
            } catch (Throwable e) {
                log.error(format("%s error", batchName), of("data", t), e);
                error.getAndIncrement();
                onError.accept(t, e);
            }
            return null;
        }).collect(toList());

        try {
            pool.invokeAll(callables);
        } catch (InterruptedException ignore) {
            log.error(format("%s error", batchName), ignore);
        }

        long executionMills = currentTimeMillis() - startTime;
        log.info(format("%s tookMs = %d", batchName, executionMills));
        log.info(format("%s successSize = %d", batchName, success.get()));
        log.info(format("%s errorSize = %d", batchName, error.get()));

        if (autoShutdown) {
            shutdownAndAwaitTermination();
        }
    }

    private void shutdownAndAwaitTermination() {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(timeout, unit)) {
                pool.shutdownNow();
                if (!pool.awaitTermination(timeout, unit)) {
                    log.error("timeout to await termination");
                }
            }
        } catch (InterruptedException e) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
