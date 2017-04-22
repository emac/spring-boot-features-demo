package cn.emac.demo.petstore.configs;

import cn.emac.demo.petstore.common.jackson.CustomEnumModule;
import cn.emac.demo.petstore.common.jackson.Java8Mapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

import static cn.emac.demo.petstore.PetstoreConstants.ENUM_PROP_NAME;

/**
 * @author Emac
 * @since 2016-12-01
 */
@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        Java8Mapper java8Mapper = new Java8Mapper();
        // 确保反序列化时自动加上TimeZone信息
        java8Mapper.setTimeZone(TimeZone.getDefault());
        java8Mapper.registerModule(new CustomEnumModule(ENUM_PROP_NAME));

        return java8Mapper;
    }
}
