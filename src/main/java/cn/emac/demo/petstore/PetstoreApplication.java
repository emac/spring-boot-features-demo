package cn.emac.demo.petstore;

import cn.emac.demo.petstore.configs.CacheConfig;
import cn.emac.demo.petstore.configs.JpetstoreDbConfig;
import cn.emac.demo.petstore.configs.SecurityConfig;
import cn.emac.demo.petstore.configs.SessionConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({JpetstoreDbConfig.class, SecurityConfig.class, SessionConfig.class, CacheConfig.class})
public class PetstoreApplication extends WebMvcAutoConfigurationAdapter {

	public static void main(String[] args) {
		SpringApplication.run(PetstoreApplication.class, args);
	}
}
