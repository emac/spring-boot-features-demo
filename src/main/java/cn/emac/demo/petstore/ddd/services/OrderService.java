package cn.emac.demo.petstore.ddd.services;

import cn.emac.demo.petstore.ddd.converters.OrderConverter;
import cn.emac.demo.petstore.ddd.entities.Order;
import cn.emac.demo.petstore.ddd.entities.OrderFactory;
import cn.emac.demo.petstore.ddd.forms.OrderCreateForm;
import cn.emac.demo.petstore.ddd.info.OrderInfo;
import cn.emac.demo.petstore.ddd.query.OrderQuery;
import cn.emac.demo.petstore.ddd.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Emac
 * @since 2018-05-26
 */
@Service
@Validated
public class OrderService {

    @Autowired
    private OrderFactory orderFactory;

    @Autowired
    private OrderConverter orderConverter;

    @Autowired
    private OrderRepository orderRepository;

    public List<OrderInfo> find(@Valid OrderQuery query) {
        return orderConverter.toInfo(orderRepository.find(query));
    }

    public OrderInfo create(@Valid OrderCreateForm form) {
        Order order = orderFactory.create(form);
        Integer orderid = orderRepository.store(order);
        return orderConverter.toInfo(orderRepository.findById(orderid));
    }

    public OrderInfo updateCourier(@NotNull Integer id, @NotNull String courier) {
        Order order = orderRepository.findById(id);
        order.changeCourier(courier);
        orderRepository.store(order);
        return orderConverter.toInfo(orderRepository.findById(id));
    }

    public void delete(@NotNull Integer id) {
        orderRepository.deleteById(id);
    }
}
