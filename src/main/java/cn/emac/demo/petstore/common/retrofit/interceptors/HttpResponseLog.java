package cn.emac.demo.petstore.common.retrofit.interceptors;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.List;

import static com.google.common.base.Joiner.on;
import static com.google.common.collect.Lists.newArrayList;

/**
 * 响应日志
 *
 * @author Emac
 * @since 2016-09-27
 */
@Getter
@Builder
public class HttpResponseLog {

    // 响应码
    private Integer code;

    // 响应信息
    private String msg;

    // 响应地址
    private String url;

    // 响应头
    @Singular("header")
    private List<String> headers = newArrayList();

    // 响应体
    private String body;

    // 耗时(ms)
    private long tookMs;

    // 异常
    private String errorMsg;

    @Override
    public String toString() {
        // <--表示接收
        if (errorMsg == null) {
            String headers = "[\n\t\t" + on(",\n\t\t").join(this.headers) + "\n\t]";
            return "\n<--response start\n{" +
                    "\n\tcode: " + code + "," +
                    "\n\tmsg: " + msg + "," +
                    "\n\turl: " + url + "," +
                    "\n\theaders: " + headers + "," +
                    (body != null ? "\n\tbody: " + body.replaceAll("\\n", "") + "," : "") +
                    "\n\ttookMs: " + tookMs + "ms" +
                    "\n}\n<--response end";
        } else {
            return "\n<--response start\n{" +
                    "\n\turl: " + url + "," +
                    "\n\terrorMsg: " + errorMsg +
                    "\n}\n<--response end";
        }
    }
}
