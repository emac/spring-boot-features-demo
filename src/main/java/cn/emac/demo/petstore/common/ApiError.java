package cn.emac.demo.petstore.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiError implements Error {

    // 系统错误
    SERVER_EXCEPTION(0, "服务异常");

    private int code;
    private String message;
}
