package cn.emac.demo.petstore.common.jackson;

import cn.emac.demo.petstore.common.utils.Asserts;
import com.fasterxml.jackson.databind.module.SimpleModule;

import javax.validation.constraints.NotNull;

/**
 * @author Emac
 * @since 2016-10-01
 */
public class CustomEnumModule extends SimpleModule {

    /**
     * @param prop 属性名
     */
    public CustomEnumModule(@NotNull String prop){
        Asserts.notBlank(prop);

        addDeserializer(Enum.class, new CustomEnumDeserializer(prop));
        addSerializer(Enum.class, new CustomEnumSerializer(prop));
    }
}
