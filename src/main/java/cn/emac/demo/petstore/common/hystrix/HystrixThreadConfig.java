package cn.emac.demo.petstore.common.hystrix;

import lombok.Getter;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

/**
 * Hystrix控制隔离线程池的参数
 *
 * @author Emac
 * @since 2016-09-27
 */
@Getter
public class HystrixThreadConfig {

    /**
     * 线程池名, 如果threadGroupKey一样, 则会使用同一个线程池。
     * 同一个线程池如果有多个配置, 则可能无法确保使用哪个配置, 所以请确保不要配置多个同名的线程池。
     */
    @NonNull
    private String groupKey;

    /**
     * 线程池Core大小
     */
    private Integer coreSize = 5;

    /**
     * 线程池Maximum大小, 必须设置allowMaximumSizeToDivergeFromCoreSize为true才生效, 否则等于coreSize
     */
    private Integer maximumSize = 10;

    /**
     * 线程池KeepAlive时间, 线程空闲超过该时间会被回收, 直到线程为coreSize
     */
    private Integer keepAliveTimeMinutes = 2;

    /**
     * 是否允许线程池coreSize和maximumSize不同
     */
    private Boolean allowMaximumSizeToDivergeFromCoreSize = true;

    /**
     * 线程池队列大小
     */
    private Integer maxQueueSize = 20;

    /**
     * 线程池队列限制, 大于此值即使队列还有空间也会被Reject
     */
    private Integer queueSizeRejectionThreshold = 20;

    public HystrixThreadConfig(String groupKey) {
        if (StringUtils.isBlank(groupKey)) {
            throw new IllegalArgumentException("Must specify a valid thread group key.");
        }
        this.groupKey = groupKey;
    }

    public HystrixThreadConfig coreSize(Integer value) {
        this.coreSize = value;
        return this;
    }

    public HystrixThreadConfig maximumSize(Integer value) {
        this.maximumSize = value;
        return this;
    }

    public HystrixThreadConfig keepAliveTimeMinutes(Integer value) {
        this.keepAliveTimeMinutes = value;
        return this;
    }

    public HystrixThreadConfig allowMaximumSizeToDivergeFromCoreSize(Boolean value) {
        this.allowMaximumSizeToDivergeFromCoreSize = value;
        return this;
    }

    public HystrixThreadConfig maxQueueSize(Integer value) {
        this.maxQueueSize = value;
        return this;
    }

    public HystrixThreadConfig queueSizeRejectionThreshold(Integer value) {
        this.queueSizeRejectionThreshold = value;
        return this;
    }
}
