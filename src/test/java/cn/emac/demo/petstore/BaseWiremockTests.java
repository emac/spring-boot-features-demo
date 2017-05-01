package cn.emac.demo.petstore;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.springframework.test.context.TestPropertySource;

/**
 * @author Emac
 * @since 2017-05-01
 */
// 切换端口以避免和VacationClientTests冲突
@TestPropertySource(properties = "vacation.host=http://localhost:3001")
public abstract class BaseWiremockTests extends BaseMvcTests {

    // 必须和vacation.host属性里的端口保持一致
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(3001);
}
