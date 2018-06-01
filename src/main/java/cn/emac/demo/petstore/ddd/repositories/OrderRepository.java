package cn.emac.demo.petstore.ddd.repositories;

import cn.emac.demo.petstore.ddd.converters.OrderConverter;
import cn.emac.demo.petstore.ddd.entities.Order;
import cn.emac.demo.petstore.ddd.entities.OrderFactory;
import cn.emac.demo.petstore.ddd.query.OrderQuery;
import cn.emac.demo.petstore.domain.tables.pojos.Lineitem;
import cn.emac.demo.petstore.domain.tables.pojos.Orders;
import cn.emac.demo.petstore.repositories.LineitemRepository;
import cn.emac.demo.petstore.repositories.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Emac
 * @since 2018-05-26
 */
@Repository
public class OrderRepository {

    @Autowired
    private OrderFactory orderFactory;

    @Autowired
    private OrderConverter orderConverter;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private LineitemRepository lineitemRepository;

    public Order findById(Integer id) {
        Orders order = ordersRepository.findById(id);
        return orderFactory.create(order);
    }

    public List<Order> find(OrderQuery query) {
        List<Orders> orders = ordersRepository.find(query);
        return orderFactory.create(orders);
    }

    @Transactional
    public Integer store(Order order) {
        Orders orderModel = orderConverter.toModel(order);
        Integer orderId = ordersRepository.insertOrUpdateAndReturnId(orderModel);

        List<Lineitem> itemsModel = orderConverter.toModel(order.getItems());
        itemsModel.forEach(i -> i.setOrderid(orderId));
        lineitemRepository.batchInsertOrUpdate(itemsModel);

        return orderId;
    }

    @Transactional
    public void deleteById(Integer id) {
        lineitemRepository.deleteByOrderId(id);
        ordersRepository.deleteById(id);
    }
}
