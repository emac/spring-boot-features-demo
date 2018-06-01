package cn.emac.demo.petstore.jooq;

import cn.emac.demo.petstore.common.data.Pagination;
import org.apache.commons.lang3.tuple.Pair;
import org.jooq.*;
import org.jooq.exception.DataAccessException;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.jooq.impl.DSL.trueCondition;

/**
 * @author Emac
 * @since 2018-05-26
 */
public abstract class AbstractSingleDslRepository<E extends Serializable> extends AbstractDslRepository {

    /**
     * OR-Mapping的基础映射
     *
     * @return (Object, Relationship)
     */
    protected abstract Pair<Class<E>, Table<? extends UpdatableRecord>> mapping();

    protected Class<E> type() {
        return mapping().getLeft();
    }

    protected Table<? extends UpdatableRecord> table() {
        return mapping().getRight();
    }

    /*------------------------------------查------------------------------------*/

    public List<E> findAll() {
        return getDsl().selectFrom(table()).fetchInto(type());
    }

    public Optional<E> findOptionalById(Integer id) {
        return findOptional(pk(), id);
    }

    public E findById(Integer id) {
        return getDsl().selectFrom(table()).where(pk().eq(id)).fetchOneInto(type());
    }

    public <T> List<E> find(Field<T> field, T value) {
        return getDsl().selectFrom(table()).where(field.eq(value)).fetchInto(type());
    }

    public List<E> find(Condition... condition) {
        return select(condition).fetchInto(type());
    }

    public <T extends E> List<T> find(Class<T> subType, Condition... condition) {
        return select(condition).fetchInto(subType);
    }

    public <T> Optional<E> findOptional(Field<T> field, T value) {
        return getDsl().selectFrom(table()).where(field.eq(value)).fetchOptionalInto(type());
    }

    public Optional<E> findOptional(Condition... condition) {
        return select(condition).fetchOptionalInto(type());
    }

    public Pagination<E> findPage(Pageable pageable, Condition... condition) {
        return findPage(type(), pageable, condition);
    }

    public <T extends E> Pagination<T> findPage(Class<T> subType, Pageable pageable, Condition... condition) {
        List<T> content = select(condition)
                .orderBy(JooqUtils.getSortFields(table(), pageable.getSort()))
                .limit(pageable.getOffset(), pageable.getPageSize())
                .fetchInto(subType);
        Long total = count(condition);

        Pagination<T> page = new Pagination<>();
        page.setPage(pageable.getPageNumber());
        page.setSize(pageable.getPageSize());
        page.setContent(content);
        page.setTotal(Optional.of(total.intValue()));

        return page;
    }

    public long countAll() {
        return getDsl().selectCount().from(table()).fetchOneInto(long.class);
    }

    public <T> long count(Field<T> field, T value) {
        return getDsl().selectCount().from(table()).where(field.eq(value)).fetchOneInto(long.class);
    }

    public long count(Condition... condition) {
        return getDsl().selectCount().from(table()).where(condition).fetchOneInto(long.class);
    }

    public boolean existsById(Integer id) {
        return exists(pk(), id);
    }

    public <T> boolean exists(Field<T> field, T value) {
        return count(field, value) > 0;
    }

    public boolean exists(Condition... condition) {
        return count(condition) > 0;
    }

    public boolean notExists(Condition... condition) {
        return count(condition) == 0;
    }

    /*------------------------------------增------------------------------------*/

    public <T extends E> boolean insert(T entity) {
        return record(entity, false).insert() == 1;
    }

    /**
     * 新增记录并返回新增后的记录
     * <p>
     * 先执行insert, 再执行select
     */
    public E insertAndReturn(E entity) {
        return getDsl().insertInto(table()).set(record(entity, false)).returning().fetchOne().into(type());
    }

    /**
     * 新增记录并返回主键ID
     * <p>
     * 只执行insert
     */
    public <T extends E> Integer insertAndReturnId(T entity) {
        UpdatableRecord record = record(entity, false);
        record.insert();
        return record.getValue(pk());
    }

    /**
     * 新增记录，若有唯一键冲突，则忽略
     */
    public <T extends E> void insertOrIgnore(T entity) {
        getDsl().insertInto(table()).set(record(entity, false)).onDuplicateKeyIgnore().execute();
    }

    /**
     * 新增记录，若有唯一键冲突，则更新
     *
     * @return 更新条数
     */
    public <T extends E> Integer insertOrUpdate(T entity) {
        UpdatableRecord record = record(entity, false);
        return getDsl().insertInto(table()).set(record).onDuplicateKeyUpdate().set(record).execute();
    }

    /**
     * 批量新增
     */
    public <T extends E> boolean batchInsert(Collection<T> entities) {
        return getDsl().batchInsert(records(entities, false)).execute().length == entities.size();
    }

    /*------------------------------------改------------------------------------*/

    public <T extends E> boolean update(T entity) {
        return record(entity, true).update() == 1;
    }

    public <T extends E> E updateAndReturn(T entity) {
        UpdatableRecord<?> record = record(entity, true);
        Integer id = record.getValue(pk());
        if (id == null) {
            throw new IllegalArgumentException("id can not be null for update");
        }
        record.update();
        return select(pk().eq(id)).fetchOneInto(type());
    }

    /**
     * @param record    待更新的数据
     * @param condition 更新条件
     */
    public int update(UpdatableRecord record, Condition... condition) {
        return getDsl().update(table()).set(record).where(condition).execute();
    }

    /**
     * @param map       待更新的数据
     * @param condition 更新条件
     * @apiNote 如果多个更新项之间有严格的先后顺序, 务必使用有序Map
     */
    public int update(Map<? extends Field<?>, ?> map, Condition... condition) {
        return getDsl().update(table()).set(map).where(condition).execute();
    }

    /**
     * 更新指定的字段
     *
     * @param condition 更新条件
     */
    public <T> int update(Field<T> field, T value, Condition... condition) {
        return getDsl().update(table()).set(field, value).where(condition).execute();
    }

    /**
     * 批量更新
     */
    public boolean batchUpdate(Collection<E> entities) {
        return getDsl().batchUpdate(records(entities, true)).execute().length == entities.size();
    }

    /*------------------------------------删------------------------------------*/

    public boolean deleteById(Integer... ids) {
        if (ids.length == 1) {
            return getDsl().delete(table()).where(pk().eq(ids[0])).execute() == 1;
        }
        return getDsl().delete(table()).where(pk().in(ids)).execute() == ids.length;
    }

    public <T> int deleteBy(Field<T> field, T value) {
        return getDsl().delete(table()).where(field.eq(value)).execute();
    }

    /*------------------------------------内部------------------------------------*/

    protected UpdatableRecord<?> record(E entity, boolean forUpdate) {
        Field<Integer> field = pk();
        UpdatableRecord<?> record = getDsl().newRecord(table(), entity);
        if (forUpdate && field != null) {
            record.changed(field, false);
        }
        // 如果实体属性为NULL,但数据库列为NOT NULL, 则允许数据库应用列的默认值
        int size = record.size();
        for (int i = 0; i < size; i++) {
            if (record.getValue(i) == null && !record.field(i).getDataType().nullable()) {
                record.changed(i, false);
            }
        }
        return record;
    }

    protected <T extends E> List<UpdatableRecord<?>> records(Collection<T> entities, boolean forUpdate) {
        return entities.stream().map(entity -> record(entity, forUpdate)).collect(toList());
    }

    protected Field<Integer> pk() {
        UniqueKey<?> key = table().getPrimaryKey();
        if (key == null) {
            throw new DataAccessException("no primary key");
        }
        Field<?>[] fields = key.getFieldsArray();
        if (fields.length == 0) {
            throw new DataAccessException("no primary key");
        }
        return fields[0].cast(Integer.class);
    }

    private SelectConditionStep<? extends Record> select(Condition... condition) {
        if (condition == null) {
            return getDsl().selectFrom(table()).where(trueCondition());
        }
        return getDsl().selectFrom(table()).where(condition);
    }
}
