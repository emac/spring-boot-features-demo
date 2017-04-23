package cn.emac.demo.petstore.common.retrofit;

import cn.emac.demo.petstore.common.jackson.CustomEnumModule;
import cn.emac.demo.petstore.common.jackson.Java8Mapper;
import cn.emac.demo.petstore.common.utils.Asserts;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * ObjectMapper工厂类，根据枚举属性名缓存ObjectMapper对象
 *
 * @author Emac
 * @since 2016-11-24
 */
public class ObjectMapperFactory {

    private static Map<String, ObjectMapper> mappers = Maps.newHashMap();

    /**
     * @param enumPropName 用于序列化的枚举属性名称
     * @return
     */
    public static ObjectMapper getObjectMapper(String enumPropName) {
        Asserts.notBlank(enumPropName);

        ObjectMapper mapper = mappers.get(enumPropName);
        if (mapper == null) {
            synchronized (ObjectMapperFactory.class) {
                mapper = mappers.get(enumPropName);
                if (mapper == null) {
                    mapper = _createObjectMapper(enumPropName);
                }
            }
        }

        return mapper;
    }

    private static ObjectMapper _createObjectMapper(String enumPropName) {
        Java8Mapper java8Mapper = new Java8Mapper();
        java8Mapper.registerModule(new CustomEnumModule(enumPropName));

        return java8Mapper;
    }
}
