package cn.emac.demo.petstore.common;

/**
 * 内部服务错误接口
 *
 * @author Emac
 * @since 2016-09-27
 */
public interface Error {

    int GENERAL_ERROR_CODE = 0;

    String GENERAL_ERROR_MESSAGE = "服务异常";

    /**
     * 获取错误码
     *
     * @return
     */
    int getCode();

    /**
     * 获取错误消息内容
     *
     * @return
     */
    String getMessage();
}
