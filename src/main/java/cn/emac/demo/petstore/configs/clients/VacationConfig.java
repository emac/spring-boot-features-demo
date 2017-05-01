package cn.emac.demo.petstore.configs.clients;

import cn.emac.demo.petstore.clients.VacationClient;
import cn.emac.demo.petstore.clients.VacationClientConfig;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Emac
 * @since 2017-04-23
 */
@Configuration
@EnableConfigurationProperties(VacationProperties.class)
public class VacationConfig {

    @Autowired
    private VacationProperties vacationProperties;

    @Bean
    public VacationClient vacationClient() {
        VacationClientConfig clientConfig = new VacationClientConfig(vacationProperties.getHost());
        return new VacationClient(clientConfig);
    }
}

@ConfigurationProperties("vacation")
@Data
class VacationProperties {

    private String host;
}
