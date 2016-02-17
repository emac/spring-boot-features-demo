package cn.emac.demo.petstore.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author Emac
 * @since 2016-02-17
 */
@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 86400)
public class SessionConfig {

    @Bean
    @ConfigurationProperties(prefix = "redis")
    public JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setUsePool(true);
        return jedisConnectionFactory;
    }
}
