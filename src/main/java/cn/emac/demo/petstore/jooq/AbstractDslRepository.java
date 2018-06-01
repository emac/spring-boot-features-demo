package cn.emac.demo.petstore.jooq;

import org.jooq.DSLContext;

/**
 * @author Emac
 * @since 2018-05-26
 */
public abstract class AbstractDslRepository {

    protected abstract DSLContext getDsl();
}
