package cn.emac.demo.petstore.ddd.info;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

/**
 * @author Emac
 * @since 2018-05-26
 */
@Data
public class OrderInfo {

    private Integer orderid;
    private String userid;
    private Date orderdate;
    private String shipaddr1;
    private String shipaddr2;
    private String shipcity;
    private String shipstate;
    private String shipzip;
    private String shipcountry;
    private String billaddr1;
    private String billaddr2;
    private String billcity;
    private String billstate;
    private String billzip;
    private String billcountry;
    private String courier;
    private BigDecimal totalprice;
    private String billtofirstname;
    private String billtolastname;
    private String shiptofirstname;
    private String shiptolastname;
    private String creditcard;
    private String exprdate;
    private String cardtype;
    private String locale;

    private List<LineItemInfo> itemInfos;
}
