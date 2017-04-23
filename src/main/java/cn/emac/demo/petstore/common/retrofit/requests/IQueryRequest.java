package cn.emac.demo.petstore.common.retrofit.requests;

import java.util.Map;

/**
 * 查询类API请求参数
 *
 * @author Emac
 * @since 2016-09-29
 */
public interface IQueryRequest extends IRequest {

    /**
     * 转换成参数Map
     *
     * @return
     */
    Map<String, String> toMap();
}
