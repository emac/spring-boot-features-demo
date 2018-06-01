package cn.emac.demo.petstore.ddd.entities;

import cn.emac.demo.petstore.ddd.forms.LineItemCreateForm;
import cn.emac.demo.petstore.ddd.forms.OrderCreateForm;
import cn.emac.demo.petstore.domain.tables.pojos.Lineitem;
import cn.emac.demo.petstore.domain.tables.pojos.Orders;
import cn.emac.demo.petstore.repositories.LineitemRepository;
import cn.emac.demo.petstore.spring.Beans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Emac
 * @since 2018-05-26
 */
@Component
@Validated
public class OrderFactory {

    @Autowired
    private LineitemRepository lineitemRepository;

    public Order create(@Valid OrderCreateForm form) {
        Order order = new Order();
        Beans.copyProperties(form, order);
        order.setOrderdate(new Date(System.currentTimeMillis()));

        List<LineItem> items = form.getItemForms().stream().map(this::create).collect(Collectors.toList());
        order.setItems(items);
        return order;
    }

    public LineItem create(@NotNull LineItemCreateForm form) {
        LineItem item = new LineItem();
        Beans.copyProperties(form, item);
        return item;
    }

    public Order create(@NotNull Orders model) {
        Order order = new Order();
        Beans.copyProperties(model, order);

        List<LineItem> items = lineitemRepository.findByOrderId(model.getOrderid()).stream().map(this::create).collect(Collectors.toList());
        order.setItems(items);
        return order;
    }

    private LineItem create(Lineitem model) {
        LineItem item = new LineItem();
        Beans.copyProperties(model, item);
        return item;
    }

    public List<Order> create(List<Orders> orders) {
        return orders.stream().map(this::create).collect(Collectors.toList());
    }
}
