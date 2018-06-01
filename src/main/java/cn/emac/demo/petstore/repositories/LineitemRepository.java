package cn.emac.demo.petstore.repositories;

import cn.emac.demo.petstore.domain.tables.pojos.Lineitem;
import cn.emac.demo.petstore.jooq.AbstractSingleRepository;
import org.apache.commons.lang3.tuple.Pair;
import org.jooq.Table;
import org.jooq.UpdatableRecord;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static cn.emac.demo.petstore.domain.Tables.LINEITEM;

/**
 * @author Emac
 * @since 2018-05-26
 */
@Repository
public class LineitemRepository extends AbstractSingleRepository<Lineitem> {

    @Override
    protected Pair<Class<Lineitem>, Table<? extends UpdatableRecord>> mapping() {
        return Pair.of(Lineitem.class, LINEITEM);
    }

    public List<Lineitem> findByOrderId(Integer orderId) {
        return find(LINEITEM.ORDERID.eq(orderId));
    }

    public void batchInsertOrUpdate(List<Lineitem> items) {
        List<Lineitem> itemsToInsert = items.stream().filter(i -> i.getOrderid() != null && i.getLinenum() == null).collect(Collectors.toList());
        for (int i = 0; i < itemsToInsert.size(); i++) {
            Lineitem item = itemsToInsert.get(i);
            item.setLinenum(++i);
        }

        List<Lineitem> itemsToUpdate = items.stream().filter(i -> i.getOrderid() != null && i.getLinenum() != null).collect(Collectors.toList());

        batchInsert(itemsToInsert);
        batchUpdate(itemsToUpdate);
    }

    public void deleteByOrderId(Integer id) {
        deleteBy(LINEITEM.ORDERID, id);
    }
}
