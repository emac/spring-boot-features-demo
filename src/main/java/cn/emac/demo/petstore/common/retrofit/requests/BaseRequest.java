package cn.emac.demo.petstore.common.retrofit.requests;

import cn.emac.demo.petstore.common.utils.Asserts;
import com.google.common.collect.Maps;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * API请求参数基类
 *
 * @author Emac
 * @since 2016-10-14
 */
public abstract class BaseRequest implements IRequest {

    private Map<String, String> headers = Maps.newHashMap();

    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public String addHeader(String header, String value) {
        return headers.put(header, value);
    }

    @Override
    public void setHeaders(@NotNull Map<String, String> headers) {
        Asserts.notNull(headers);

        this.headers = headers;
    }
}
