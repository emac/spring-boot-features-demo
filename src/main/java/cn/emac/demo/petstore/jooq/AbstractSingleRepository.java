package cn.emac.demo.petstore.jooq;

import lombok.Getter;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

/**
 * @author Emac
 * @since 2018-05-26
 */
public abstract class AbstractSingleRepository<E extends Serializable> extends AbstractSingleDslRepository<E> {

    @Getter
    @Autowired
    protected DSLContext dsl;
}
