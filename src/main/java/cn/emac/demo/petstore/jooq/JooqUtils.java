package cn.emac.demo.petstore.jooq;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Field;
import org.jooq.SortField;
import org.jooq.SortOrder;
import org.jooq.Table;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cn.emac.demo.petstore.common.utils.Asserts.isTrue;
import static cn.emac.demo.petstore.common.utils.Asserts.notNull;


/**
 * @author Emac
 * @since 2018-05-26
 */
public class JooqUtils {

    public static String DEFAULT_DELIMITER = ".";

    /**
     * 获取一张或多张表的全字段别名。全字段定义：表名.字段名。
     */
    public static List<Field<?>> getFullFields(Table<?>... tables) {
        List<Field<?>> fields = Lists.newArrayList();
        Stream.of(tables).forEach(table -> {
            fields.addAll(Stream.of(table.fields())
                    .map(field -> getFullField(table, field))
                    .collect(Collectors.toList())
            );
        });
        return fields;
    }

    /**
     * 获取单表的全字段别名。全字段定义：表名.字段名。
     */
    public static Field<?> getFullField(Table<?> table, Field<?> field) {
        notNull(table, "table cannot be null");
        notNull(field, "field cannot be null");

        return field.as(table.getName() + DEFAULT_DELIMITER + field.getName());
    }

    /**
     * 转换成排序字段（单表）。Sort Property可以带表名，也可以不带表名。
     */
    public static List<SortField<?>> getSortFields(Table<?> table, Sort sort) {
        notNull(table, "table cannot be null");
        notNull(sort, "sort cannot be null");

        List<SortField<?>> sortFields = Lists.newArrayList();
        sort.forEach(order -> {
            String property = order.getProperty();
            if (property.contains(DEFAULT_DELIMITER)) {
                // 校验表名
                String[] parts = StringUtils.splitByWholeSeparator(property, DEFAULT_DELIMITER);
                isTrue(parts.length == 2, "Sort property in wrong format: " + property);
                isTrue(parts[0].toLowerCase().equals(table.getName().toLowerCase()), String.format("Unmatched table name: %s, %s", property, table.getName()));
                // 去除表名
                property = parts[1];
            }
            Field<?> field = table.field(property);
            if (field != null) {
                SortOrder sortOrder = order.getDirection() == Sort.Direction.ASC ? SortOrder.ASC : SortOrder.DESC;
                SortField<?> sortField = field.sort(sortOrder);
                sortFields.add(sortField);
            }
        });
        return sortFields;
    }

    /**
     * 转换成排序字段（多表）。Sort Property必须带表名。
     */
    public static List<SortField<?>> getSortFieldsAccrossMutiTables(Sort sort, Table<?>... tables) {
        notNull(sort, "sort cannot be null");
        isTrue(ArrayUtils.isNotEmpty(tables), "tables cannot be empty");

        Map<String, Table<?>> tableMap = Stream.of(tables).collect(Collectors.toMap(t -> t.getName().toLowerCase(), Function.identity()));
        List<SortField<?>> sortFields = Lists.newArrayList();
        sort.forEach(order -> {
            String property = order.getProperty();
            isTrue(property.contains(DEFAULT_DELIMITER), "Sort property must contain table name in case of multiple tables: " + property);

            // 校验表名
            String[] parts = StringUtils.splitByWholeSeparator(property, DEFAULT_DELIMITER);
            isTrue(parts.length == 2, "Sort property in wrong format: " + property);

            Table<?> table = tableMap.get(parts[0].toLowerCase());
            if (table != null) {
                Field<?> field = table.field(parts[1]);
                if (field != null) {
                    SortOrder sortOrder = order.getDirection() == Sort.Direction.ASC ? SortOrder.ASC : SortOrder.DESC;
                    SortField<?> sortField = field.sort(sortOrder);
                    sortFields.add(sortField);
                }
            }
        });
        return sortFields;
    }

    /**
     * 转换成排序字段
     */
    public static List<SortField<?>> toSortFields(Sort sort, Function<Order, Optional<SortField<?>>> sortMapping) {
        notNull(sort, "sort cannot be null");
        notNull(sortMapping, "sortMapping cannot be null");

        List<SortField<?>> sortFields = Lists.newArrayList();
        sort.forEach(order -> sortMapping.apply(order).ifPresent(sortFields::add));
        return sortFields;
    }
}
