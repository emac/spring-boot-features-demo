package cn.emac.demo.petstore.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Emac
 * @since 2017-04-22
 */
@Getter
@AllArgsConstructor
public enum ApiError implements Error {

    // 系统错误
    SERVER_EXCEPTION(0, "服务异常");

    private int code;
    private String message;
}
