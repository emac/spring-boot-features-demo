package cn.emac.demo.petstore;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration;

/**
 * @author Emac
 * @since 2017-05-01
 */
// 满足@Service和@Repository的依赖
@ImportAutoConfiguration(JooqAutoConfiguration.class)
public abstract class BaseMvcTests extends BaseTests {
}
