package cn.emac.demo.petstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@ComponentScan("cn.emac")
@EnableRetry
public class PetstoreApplication extends WebMvcAutoConfigurationAdapter {

	public static void main(String[] args) {
		SpringApplication.run(PetstoreApplication.class, args);
	}
}
