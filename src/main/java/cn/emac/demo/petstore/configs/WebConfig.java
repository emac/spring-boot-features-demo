package cn.emac.demo.petstore.configs;

import cn.emac.demo.petstore.common.concurrent.SpringExecutorFactory;
import cn.emac.demo.petstore.common.format.CustomFormatterRegistrar;
import cn.emac.demo.petstore.controllers.MyErrorController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import static cn.emac.demo.petstore.PetstoreConstants.ENUM_PROP_NAME;

/**
 * @author Emac
 * @since 2016-03-20
 */
@Configuration
@ServletComponentScan("cn.emac")
@EnableSpringDataWebSupport
@EnableRetry
public class WebConfig extends WebMvcConfigurerAdapter implements EmbeddedServletContainerCustomizer {

    @Value("${server.error.path:${error.path:/error}}")
    private String errorPath = "/error";

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        // 启用方法层面(参数,返回值)的校验,跟@Validated注解配合使用
        MethodValidationPostProcessor processor = new MethodValidationPostProcessor();
        processor.setProxyTargetClass(true);
        return processor;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        new CustomFormatterRegistrar(ENUM_PROP_NAME).registerFormatters(registry);
    }

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        configurer.setDefaultTimeout(5 * 60 * 1000L); //5分钟
        // used by controller
        configurer.setTaskExecutor(SpringExecutorFactory.build("default-DeferredResult", 15, 30, 30));
    }

    @Bean
    public MyErrorController errorController() {
        // uniform error control
        ErrorAttributes errorAttributes = new DefaultErrorAttributes();
        ErrorProperties errorProperties = new ErrorProperties();
        return new MyErrorController(errorAttributes, errorProperties);
    }

    @Override
    public void customize(ConfigurableEmbeddedServletContainer factory) {
        // highest priority: fine-grained error control for each HTTP status
        factory.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, errorPath));
        factory.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, errorPath));
    }
}
