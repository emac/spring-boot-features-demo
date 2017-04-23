package cn.emac.demo.petstore.common.retrofit.requests;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * API请求参数
 *
 * @author Emac
 * @since 2016-09-29
 */
public interface IRequest {

    /**
     * 请求参数自检
     *
     * @return
     */
    boolean isValid();

    /**
     * 返回请求头
     *
     * @return
     */
    Map<String, String> getHeaders();

    /**
     * 添加请求头
     *
     * @param header
     * @param value
     * @return
     */
    String addHeader(String header, String value);

    /**
     * 设置请求头
     *
     * @param headers
     */
    void setHeaders(@NotNull Map<String, String> headers);
}
