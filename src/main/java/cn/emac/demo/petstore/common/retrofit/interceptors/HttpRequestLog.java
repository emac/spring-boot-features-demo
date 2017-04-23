package cn.emac.demo.petstore.common.retrofit.interceptors;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.List;

import static com.google.common.base.Joiner.on;
import static com.google.common.collect.Lists.newArrayList;

/**
 * 请求日志
 *
 * @author Emac
 * @since 2016-09-27
 */
@Getter
@Builder
public class HttpRequestLog {

    // 协议
    private String protocol;

    // 请求地址
    private String url;

    // 请求方法
    private String method;

    // 请求头
    @Singular("header")
    private List<String> headers = newArrayList();

    // 请求体
    private String body;

    @Override
    public String toString() {
        // --> 表示发送
        String headers = "[\n\t\t" + on(",\n\t\t").join(this.headers) + "\n\t]";
        return "\n-->request start\n{" +
                "\n\turl: " + url + "," +
                "\n\tmethod: " + method + "," +
                "\n\theaders: " + headers + "," +
                (body != null ? "\n\tbody: " + body.replaceAll("\\n", "") + "," : "") +
                "\n\tprotocol: " + protocol +
                "\n}\n-->request end";
    }
}
