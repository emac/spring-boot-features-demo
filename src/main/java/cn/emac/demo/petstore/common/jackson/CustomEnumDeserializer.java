package cn.emac.demo.petstore.common.jackson;

import cn.emac.demo.petstore.common.utils.Asserts;
import cn.emac.demo.petstore.common.utils.Enums;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.stream.Stream;

/**
 * 自定义枚举反序列化器，根据特定属性进行反序列化，如果无法找到，再尝试用枚举名进行反序列化。
 *
 * @author Emac
 * @since 2016-09-29
 */
public class CustomEnumDeserializer extends JsonDeserializer<Enum> implements ContextualDeserializer {

    @Setter
    private Class<Enum> enumCls;

    private String prop;

    /**
     * @param prop 属性名
     */
    public CustomEnumDeserializer(@NotNull String prop) {
        Asserts.notBlank(prop);

        this.prop = prop;
    }

    @Override
    public Enum deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
        String text = parser.getText();
        return Enums.getEnum(enumCls, prop, text).orElseGet(() ->
                Stream.of(enumCls.getEnumConstants())
                        .filter(e -> e.name().equals(text))
                        .findFirst().orElse(null)
        );
    }

    @Override
    public JsonDeserializer createContextual(DeserializationContext ctx, BeanProperty property) throws JsonMappingException {
        Class rawCls = ctx.getContextualType().getRawClass();
        Asserts.isTrue(rawCls.isEnum());

        Class<Enum> enumCls = (Class<Enum>) rawCls;
        CustomEnumDeserializer clone = new CustomEnumDeserializer(prop);
        clone.setEnumCls(enumCls);
        return clone;
    }
}
