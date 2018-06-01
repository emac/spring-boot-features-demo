package cn.emac.demo.petstore.ddd.forms;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import java.math.BigDecimal;

/**
 * @author Emac
 * @since 2018-05-26
 */
@Data
public class LineItemCreateForm {

    @NotEmpty
    private String itemid;
    @Min(1)
    private Integer quantity;
    @Min(0)
    private BigDecimal unitprice;
}
