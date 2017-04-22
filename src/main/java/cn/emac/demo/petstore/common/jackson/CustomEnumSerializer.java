package cn.emac.demo.petstore.common.jackson;

import cn.emac.demo.petstore.common.exceptions.CommonException;
import cn.emac.demo.petstore.common.utils.Asserts;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.apache.commons.beanutils.PropertyUtils.getPropertyDescriptor;

/**
 * 自定义枚举序列化器，查找特定属性并进行序列化，如果无法找到，则序列化为枚举名。
 *
 * @author Emac
 * @since 2016-09-29
 */
@Slf4j
public class CustomEnumSerializer extends JsonSerializer<Enum> {

    private String prop;

    /**
     * @param prop 属性名
     */
    public CustomEnumSerializer(@NotNull String prop) {
        Asserts.notBlank(prop);

        this.prop = prop;
    }

    @Override
    public void serialize(Enum value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }
        try {
            PropertyDescriptor pd = getPropertyDescriptor(value, prop);
            if (pd == null || pd.getReadMethod() == null) {
                gen.writeString(value.name());
                return;
            }
            Method m = pd.getReadMethod();
            m.setAccessible(true);
            gen.writeObject(m.invoke(value));
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new CommonException(e);
        }
    }
}
