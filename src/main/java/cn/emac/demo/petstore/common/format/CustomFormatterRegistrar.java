package cn.emac.demo.petstore.common.format;

import cn.emac.demo.petstore.common.utils.Asserts;
import cn.emac.demo.petstore.common.format.converters.CustomEnumConverterFactory;
import cn.emac.demo.petstore.common.format.converters.OffsetDateTimeConverter;
import cn.emac.demo.petstore.common.format.converters.StringTrimmerConverter;
import org.springframework.format.FormatterRegistrar;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.Jsr310DateTimeFormatAnnotationFormatterFactory;

import javax.validation.constraints.NotNull;

/**
 * 统一注册自定义的值转换器
 *
 * @author Emac
 * @since 2016-09-20
 */
public class CustomFormatterRegistrar implements FormatterRegistrar {

    private String enumProp;

    /**
     * @param enumProp 枚举属性名
     */
    public CustomFormatterRegistrar(@NotNull String enumProp) {
        Asserts.notBlank(enumProp);

        this.enumProp = enumProp;
    }

    @Override
    public void registerFormatters(FormatterRegistry registry) {
        addConverters(registry);
        addFormattersForFieldAnnotation(registry);
    }

    private void addConverters(FormatterRegistry registry) {
        registry.addConverter(new StringTrimmerConverter(false));
        registry.addConverter(new OffsetDateTimeConverter());
        registry.addConverterFactory(new CustomEnumConverterFactory(enumProp));
    }

    private void addFormattersForFieldAnnotation(FormatterRegistry registry) {
        registry.addFormatterForFieldAnnotation(new Jsr310DateTimeFormatAnnotationFormatterFactory());
    }
}
