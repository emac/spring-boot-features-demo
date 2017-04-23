package cn.emac.demo.petstore.common.hystrix;

import lombok.Getter;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

/**
 * Hystrix控制熔断和统计的参数
 *
 * @author Emac
 * @since 2016-09-27
 */
@Getter
public class HystrixCommandConfig {

    /**
     * GroupKey, 用于设置Command的分组
     */
    @NonNull
    private String groupKey;

    /**
     * CommandKey, 控制熔断和统计的最小单位
     */
    @NonNull
    private String commandKey;

    /**
     * 是否检测超时
     */
    private Boolean executionTimeoutEnabled = true;

    /**
     * 请求超时时间
     */
    private Integer executionTimeoutInMilliseconds = 1000;

    /**
     * 是否开启熔断
     */
    private Boolean circuitBreakerEnabled = true;

    /**
     * 时间窗口内至少多少个请求, 才会开始判断熔断
     */
    private Integer circuitBreakerRequestVolumeThreshold = 5;

    /**
     * 熔断后间隔多长时间, 会开始再次尝试决定是否继续熔断
     */
    private Integer circuitBreakerSleepWindowInMilliseconds = 5000;

    /**
     * 错误百分比大于多少, 进行熔断
     */
    private Integer circuitBreakerErrorThresholdPercentage = 50;

    /**
     * 时间窗口大小
     */
    private Integer metricsTimeInMilliseconds = 20000;

    /**
     * 每个时间窗口的Bucket数, 必须能整除statsTimeInMilliseconds
     */
    private Integer metricsNumBuckets = 10;

    public HystrixCommandConfig(String groupKey, String commandKey) {
        if (StringUtils.isBlank(groupKey) || StringUtils.isBlank(commandKey)) {
            throw new IllegalArgumentException("Must specify valid group key and command key.");
        }
        this.groupKey = groupKey;
        this.commandKey = commandKey;
    }

    public HystrixCommandConfig executionTimeoutEnabled(Boolean value) {
        this.executionTimeoutEnabled = value;
        return this;
    }

    public HystrixCommandConfig executionTimeoutInMilliseconds(Integer value) {
        this.executionTimeoutInMilliseconds = value;
        return this;
    }

    public HystrixCommandConfig circuitBreakerEnabled(Boolean value) {
        this.circuitBreakerEnabled = value;
        return this;
    }

    public HystrixCommandConfig circuitBreakerRequestVolumeThreshold(Integer value) {
        this.circuitBreakerRequestVolumeThreshold = value;
        return this;
    }

    public HystrixCommandConfig circuitBreakerSleepWindowInMilliseconds(Integer value) {
        this.circuitBreakerSleepWindowInMilliseconds = value;
        return this;
    }

    public HystrixCommandConfig circuitBreakerErrorThresholdPercentage(Integer value) {
        this.circuitBreakerErrorThresholdPercentage = value;
        return this;
    }

    public HystrixCommandConfig metricsTimeInMilliseconds(Integer value) {
        this.metricsTimeInMilliseconds = value;
        return this;
    }

    public HystrixCommandConfig metricsNumBuckets(Integer value) {
        this.metricsNumBuckets = value;
        return this;
    }
}
