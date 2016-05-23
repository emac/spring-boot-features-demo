package cn.emac.demo.petstore.services;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

/**
 * @author Emac
 * @since 2016-05-23
 */
@Service
public class RetryService {

    @Retryable(value = RuntimeException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public String retry() {
        if (Math.random() < 1d / 3) {
            return "success";
        } else {
            throw new RuntimeException("fail");
        }
    }

    @Recover
    public String recover(RuntimeException ex) {
        return "fail";
    }
}
