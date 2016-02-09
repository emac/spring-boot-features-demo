package cn.emac.demo.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * @author Emac
 * @since 2016-02-08
 */
@Configuration
public class JpetstoreDbConfig {

    @Bean(initMethod = "init", destroyMethod = "close")
    @ConfigurationProperties(prefix = "db.jpetstore")
    @Primary
    public DataSource dataSource() {
        return new DruidDataSource();
    }
}
