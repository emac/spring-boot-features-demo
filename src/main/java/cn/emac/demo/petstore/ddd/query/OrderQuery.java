package cn.emac.demo.petstore.ddd.query;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;
import java.util.Optional;

/**
 * @author Emac
 * @since 2018-05-26
 */
@Data
public class OrderQuery {

    private List<Integer> orderids = Lists.newArrayList();

    private Optional<String> userid = Optional.empty();
}
