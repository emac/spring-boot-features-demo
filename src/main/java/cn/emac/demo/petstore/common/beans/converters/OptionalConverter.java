package cn.emac.demo.petstore.common.beans.converters;

import org.apache.commons.beanutils.converters.AbstractConverter;

import java.util.Optional;

/**
 * 添加{@code Optional}类型的属性值转换。转换规则：value -> Optional.ofNullable(value)
 *
 * @author Emac
 * @since 2017-05-24
 */
public class OptionalConverter extends AbstractConverter {

    public OptionalConverter() {
        super();
    }

    public OptionalConverter(Object defaultValue) {
        super(defaultValue);
    }

    @Override
    protected Class<?> getDefaultType() {
        return Optional.class;
    }

    @Override
    protected <T> T convertToType(Class<T> type, Object value) throws Throwable {
        if (Optional.class.equals(type)) {
            if (value instanceof Optional) {
                return type.cast(value);
            }
            return type.cast(Optional.ofNullable(value));
        }

        throw conversionException(type, value);
    }
}
