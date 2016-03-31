package cn.emac.demo.petstore.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

@Slf4j
public class CustomEnumSerializer extends JsonSerializer<Enum<?>> {

    @Override
    public void serialize(Enum<?> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            return;
        }
        try {
            List<Object> list = Lists.newArrayList();
            list.add(value.name());
            PropertyDescriptor[] pds = Introspector.getBeanInfo(value.getClass(), Enum.class).getPropertyDescriptors();
            for (PropertyDescriptor pd : pds) {
                Method method = pd.getReadMethod();
                if (method == null) {
                    continue;
                }
                list.add(method.invoke(value));
            }
            gen.writeObject(list);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
