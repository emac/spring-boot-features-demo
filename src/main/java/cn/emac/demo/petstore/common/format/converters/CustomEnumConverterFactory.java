package cn.emac.demo.petstore.common.format.converters;

import cn.emac.demo.petstore.common.utils.Asserts;
import cn.emac.demo.petstore.common.utils.Enums;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import javax.validation.constraints.NotNull;
import java.util.stream.Stream;

/**
 * 基于特定属性的枚举数据类型转换器工厂
 *
 * @author Emac
 * @since 2016-10-06
 */
public class CustomEnumConverterFactory implements ConverterFactory<String, Enum> {

    private String prop;

    /**
     * @param prop 属性名
     */
    public CustomEnumConverterFactory(@NotNull String prop) {
        Asserts.notBlank(prop);

        this.prop = prop;
    }

    @Override
    public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {
        return new CustomEnumConverter(targetType, prop);
    }

    /**
     * 基于特定属性的枚举数据类型转换器，如果无法找到，再尝试用枚举名进行转换。
     */
    public static class CustomEnumConverter<T extends Enum<T>> implements Converter<String, T> {

        private Class<T> enumCls;
        private String prop;

        /**
         * @param enumCls 枚举类型
         * @param prop 属性名
         */
        public CustomEnumConverter(Class<T> enumCls, String prop) {
            this.enumCls = enumCls;
            this.prop = prop;
        }

        @Override
        public T convert(String source) {
            if (StringUtils.isEmpty(source)) {
                return null;
            }
            return Enums.getEnum(enumCls, prop, source).orElseGet(() ->
                    Stream.of(enumCls.getEnumConstants())
                            .filter(e -> e.name().equals(source))
                            .findFirst().orElse(null)
            );
        }
    }
}
