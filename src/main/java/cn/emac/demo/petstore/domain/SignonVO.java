package cn.emac.demo.petstore.domain;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Emac
 * @since 2016-05-16
 */
@Data
public class SignonVO {

    @NotNull
    private String username;
}
