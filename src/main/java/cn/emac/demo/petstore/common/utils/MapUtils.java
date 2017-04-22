package cn.emac.demo.petstore.common.utils;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.apache.commons.collections4.MapUtils.isEmpty;

/**
 * Map工具类
 *
 * @author Emac
 * @since 2016-09-21
 */
public class MapUtils {

    public static <T> Map<String, T> subMap(Map<String, T> map, Set<String> keys) {
        return subMap(map, keys, key -> key);
    }

    public static <T> Map<String, T> subMap(Map<String, T> map, Set<String> keys,
                                            Function<String, String> keyHandler) {
        return subMap(map, keys, keyHandler, value -> value);
    }

    /**
     * @param map          原map
     * @param keys         匹配的键
     * @param keyHandler   对key的处理
     * @param valueHandler 对value的处理
     * @return 子map
     */
    public static <T, R> Map<String, R> subMap(Map<String, T> map, Set<String> keys,
                                               Function<String, String> keyHandler,
                                               Function<T, R> valueHandler) {
        Map<String, R> subMap = Maps.newHashMapWithExpectedSize(keys.size());
        keys.stream()
                .filter(map::containsKey)
                .map(key -> Pair.of(keyHandler.apply(key), valueHandler.apply(map.get(key))))
                .forEach(pair -> subMap.put(pair.getKey(), pair.getValue()));
        return subMap;
    }

    /**
     * 判断map中的值是否全为空
     * <p>
     * 若map本身为空，也视为值全为空
     *
     * @param nullPredicate 可视为null的自定义条件
     */
    @SafeVarargs
    public static <T> boolean isAllNullValues(Map<String, T> map, Predicate<T>... nullPredicate) {
        return isEmpty(map) || map.values().stream().allMatch(
                t -> t == null || Stream.of(nullPredicate).anyMatch(predicate -> predicate.test(t))
        );
    }
}
