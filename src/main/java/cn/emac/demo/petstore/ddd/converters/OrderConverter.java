package cn.emac.demo.petstore.ddd.converters;

import cn.emac.demo.petstore.ddd.entities.LineItem;
import cn.emac.demo.petstore.ddd.entities.Order;
import cn.emac.demo.petstore.ddd.info.LineItemInfo;
import cn.emac.demo.petstore.ddd.info.OrderInfo;
import cn.emac.demo.petstore.domain.tables.pojos.Lineitem;
import cn.emac.demo.petstore.domain.tables.pojos.Orders;
import cn.emac.demo.petstore.spring.Beans;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Emac
 * @since 2018-05-26
 */
@Component
public class OrderConverter {

    public OrderInfo toInfo(Order order) {
        OrderInfo orderInfo = new OrderInfo();
        Beans.copyProperties(order, orderInfo);

        List<LineItemInfo> itemInfos = order.getItems().stream().map(this::toInfo).collect(Collectors.toList());
        orderInfo.setItemInfos(itemInfos);
        return orderInfo;
    }

    public LineItemInfo toInfo(@NotNull LineItem item) {
        LineItemInfo info = new LineItemInfo();
        Beans.copyProperties(item, info);
        return info;
    }

    public List<OrderInfo> toInfo(List<Order> orders) {
        return orders.stream().map(this::toInfo).collect(Collectors.toList());
    }

    public Orders toModel(@NotNull Order order) {
        Orders model = new Orders();
        Beans.copyProperties(order, model);
        return model;
    }

    public List<Lineitem> toModel(List<LineItem> items) {
        return items.stream().map(this::toModel).collect(Collectors.toList());
    }

    public Lineitem toModel(LineItem item) {
        Lineitem model = new Lineitem();
        Beans.copyProperties(item, model);
        return model;
    }
}
