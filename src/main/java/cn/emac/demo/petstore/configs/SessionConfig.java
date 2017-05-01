package cn.emac.demo.petstore.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author Emac
 * @since 2016-02-17
 */
@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 86400)
// 去除测试用例对Redis的依赖
@Profile("!test")
public class SessionConfig {

    @Bean
    @ConfigurationProperties(prefix = "redis")
    public JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setUsePool(true);
        return jedisConnectionFactory;
    }
}
