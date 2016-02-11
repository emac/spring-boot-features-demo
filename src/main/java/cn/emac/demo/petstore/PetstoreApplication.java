package cn.emac.demo.petstore;

import cn.emac.demo.petstore.configs.JpetstoreDbConfig;
import cn.emac.demo.petstore.configs.SecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({JpetstoreDbConfig.class, SecurityConfig.class})
public class PetstoreApplication extends WebMvcAutoConfigurationAdapter {

	public static void main(String[] args) {
		SpringApplication.run(PetstoreApplication.class, args);
	}
}
