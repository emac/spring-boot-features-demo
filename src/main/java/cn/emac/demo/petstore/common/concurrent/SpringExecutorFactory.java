package cn.emac.demo.petstore.common.concurrent;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Spring线程池工厂
 *
 * @author Emac
 * @since 2016-12-09
 */
public class SpringExecutorFactory {

    public static final Integer DEFAULT_CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors();

    /**
     * 创建适用于处理实时任务（CPU密集）的线程池：
     * <ul>
     * <li>corePoolSize: #CPU + 1</li>
     * <li>queueCapacity: #CPU * 2</li>
     * <li>maxPoolSize: Integer.MAX_VALUE</li>
     * </ul>
     *
     * @param threadNamePrefix
     * @return
     */
    public static ThreadPoolTaskExecutor buildRealtime(String threadNamePrefix) {
        return build(threadNamePrefix, DEFAULT_CORE_POOL_SIZE + 1, DEFAULT_CORE_POOL_SIZE * 2, Integer.MAX_VALUE);
    }

    /**
     * 创建适用于处理批处理任务（I/O密集）的线程池：
     * <ul>
     * <li>corePoolSize: #CPU * 2</li>
     * <li>queueCapacity: Integer.MAX_VALUE</li>
     * <li>maxPoolSize: #CPU * 2</li>
     * </ul>
     *
     * @param threadNamePrefix
     * @return
     */
    public static ThreadPoolTaskExecutor buildBatch(String threadNamePrefix) {
        return build(threadNamePrefix, DEFAULT_CORE_POOL_SIZE * 2, Integer.MAX_VALUE, DEFAULT_CORE_POOL_SIZE * 2);
    }

    /**
     * 创建通用线程池
     *
     * @param threadNamePrefix
     * @param corePoolSize
     * @param queueCapacity
     * @param maxPoolSize
     * @return
     */
    public static ThreadPoolTaskExecutor build(String threadNamePrefix, int corePoolSize, int queueCapacity, int maxPoolSize) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix(threadNamePrefix);
        executor.setCorePoolSize(corePoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setMaxPoolSize(maxPoolSize);
        return executor;
    }
}
