package cn.emac.demo.petstore.clients;

import cn.emac.demo.petstore.PetstoreConstants;
import cn.emac.demo.petstore.common.retrofit.ClientConfig;

/**
 * @author Emac
 * @since 2017-04-23
 */
public class VacationClientConfig extends ClientConfig {

    public VacationClientConfig(String baseUrl) {
        super(baseUrl);
    }

    @Override
    public String getEnumPropName() {
        return PetstoreConstants.ENUM_PROP_NAME;
    }
}
