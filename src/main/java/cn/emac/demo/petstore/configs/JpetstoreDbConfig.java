package cn.emac.demo.petstore.configs;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author Emac
 * @since 2016-02-08
 */
@Configuration
public class JpetstoreDbConfig {

    @Value("${druid.stat.login.username}")
    private String loginUsername;

    @Value("${druid.stat.login.password}")
    private String loginPassword;

    @Bean(initMethod = "init", destroyMethod = "close")
    @ConfigurationProperties(prefix = "db.jpetstore")
    @Primary
    public DataSource dataSource() {
        return new DruidDataSource();
    }

    @Bean
    @Primary
    public DataSourceTransactionManager transactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource());
        return transactionManager;
    }

    @Bean
    public ServletRegistrationBean statViewServlet() {
        ServletRegistrationBean reg = new ServletRegistrationBean();
        reg.setServlet(new StatViewServlet());
        reg.addUrlMappings("/druid/*");
        reg.addInitParameter("loginUsername", loginUsername);
        reg.addInitParameter("loginPassword", loginPassword);

        return reg;
    }
}
