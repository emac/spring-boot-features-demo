package cn.emac.demo.petstore.ddd.controllers;

import cn.emac.demo.petstore.common.JsonResult;
import cn.emac.demo.petstore.ddd.forms.OrderCreateForm;
import cn.emac.demo.petstore.ddd.info.OrderInfo;
import cn.emac.demo.petstore.ddd.query.OrderQuery;
import cn.emac.demo.petstore.ddd.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

/**
 * @author Emac
 * @since 2018-05-26
 */
@RestController
@RequestMapping(value = "/order", produces = APPLICATION_JSON_UTF8_VALUE)
@Validated
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public JsonResult<OrderInfo> create(@RequestBody @Valid OrderCreateForm form) {
        OrderInfo order = orderService.create(form);
        return JsonResult.ok(order);
    }

    @GetMapping(value = "/find")
    public JsonResult<List<OrderInfo>> find(@Valid OrderQuery query) {
        List<OrderInfo> orders = orderService.find(query);
        return JsonResult.ok(orders);
    }

    @PutMapping(value = "/{id}")
    public JsonResult<OrderInfo> updateCourier(@PathVariable Integer id, @NotNull String courier) {
        OrderInfo order = orderService.updateCourier(id, courier);
        return JsonResult.ok(order);
    }

    @DeleteMapping(value = "/{id}")
    public JsonResult delete(@PathVariable Integer id) {
        orderService.delete(id);
        return JsonResult.ok();
    }
}
