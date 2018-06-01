package cn.emac.demo.petstore.ddd.forms;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * @author Emac
 * @since 2018-05-26
 */
@Data
public class OrderCreateForm {

    @NotNull
    private String userid;
    @NotEmpty
    private String shipaddr1;
    private Optional<String> shipaddr2 = Optional.empty();
    @NotEmpty
    private String shipcity;
    @NotEmpty
    private String shipstate;
    @NotEmpty
    private String shipzip;
    @NotEmpty
    private String shipcountry;
    @NotEmpty
    private String billaddr1;
    private Optional<String> billaddr2 = Optional.empty();
    @NotEmpty
    private String billcity;
    @NotEmpty
    private String billstate;
    @NotEmpty
    private String billzip;
    @NotEmpty
    private String billcountry;
    @NotEmpty
    private String courier;
    @Min(0)
    private BigDecimal totalprice;
    @NotEmpty
    private String billtofirstname;
    @NotEmpty
    private String billtolastname;
    @NotEmpty
    private String shiptofirstname;
    @NotEmpty
    private String shiptolastname;
    @NotEmpty
    private String creditcard;
    @NotEmpty
    private String cardtype;
    @NotEmpty
    private String exprdate;
    @NotEmpty
    private String locale;

    @NotEmpty
    @Valid
    private List<LineItemCreateForm> itemForms;
}
