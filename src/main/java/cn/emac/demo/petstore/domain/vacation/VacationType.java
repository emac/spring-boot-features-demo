package cn.emac.demo.petstore.domain.vacation;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Emac
 * @since 2017-04-23
 */
@AllArgsConstructor
@Getter
public enum VacationType {
    PERSONAL(0), SICK(1);

    private Integer value;
}
