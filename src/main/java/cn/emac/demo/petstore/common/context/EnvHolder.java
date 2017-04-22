package cn.emac.demo.petstore.common.context;

import lombok.Getter;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * 将Spring Environment实例暴露为包可见的静态对象.
 *
 * @author Emac
 * @since 2016-02-26
 */
@Component
public class EnvHolder implements EnvironmentAware {

    @Getter
    private static Environment env;

    @Override
    public void setEnvironment(Environment environment) {
        if (env == null) {
            env = environment;
        }
    }
}
