package cn.emac.demo.petstore.ddd.entities;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author Emac
 * @since 2018-05-26
 */
@Data
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class LineItem {

    /*
     * Self
     */

    private Integer orderid;
    private Integer linenum;
    private String itemid;
    private Integer quantity;
    private BigDecimal unitprice;
}
