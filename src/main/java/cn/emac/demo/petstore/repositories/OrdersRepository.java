package cn.emac.demo.petstore.repositories;

import cn.emac.demo.petstore.ddd.query.OrderQuery;
import cn.emac.demo.petstore.domain.tables.pojos.Orders;
import cn.emac.demo.petstore.jooq.AbstractSingleRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.jooq.Condition;
import org.jooq.Table;
import org.jooq.UpdatableRecord;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.List;

import static cn.emac.demo.petstore.domain.Tables.ORDERS;

/**
 * @author Emac
 * @since 2018-05-26
 */
@Repository
public class OrdersRepository extends AbstractSingleRepository<Orders> {

    @Override
    protected Pair<Class<Orders>, Table<? extends UpdatableRecord>> mapping() {
        return Pair.of(Orders.class, ORDERS);
    }

    public List<Orders> find(OrderQuery query) {
        Condition cond = DSL.trueCondition();
        if (CollectionUtils.isNotEmpty(query.getOrderids())) {
            cond = cond.and(ORDERS.ORDERID.in(query.getOrderids()));
        }
        if (query.getUserid().isPresent()) {
            cond = cond.and(ORDERS.USERID.eq(query.getUserid().get()));
        }
        return find(cond);
    }

    public Integer insertOrUpdateAndReturnId(Orders order) {
        if (order.getOrderid() == null) {
            return insertAndReturnId(order);
        } else {
            update(order);
            return order.getOrderid();
        }
    }
}
