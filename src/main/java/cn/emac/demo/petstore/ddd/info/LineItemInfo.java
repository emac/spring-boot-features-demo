package cn.emac.demo.petstore.ddd.info;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Emac
 * @since 2018-05-26
 */
@Data
public class LineItemInfo {

    private Integer orderid;
    private Integer linenum;
    private String itemid;
    private Integer quantity;
    private BigDecimal unitprice;
}
