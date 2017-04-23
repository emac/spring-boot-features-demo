package cn.emac.demo.petstore.common.retrofit.interceptors;

import okhttp3.*;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;

import java.io.EOFException;
import java.nio.charset.Charset;
import java.util.Optional;

import static java.lang.Character.isISOControl;
import static java.lang.Long.MAX_VALUE;
import static java.util.Optional.empty;
import static java.util.Optional.of;

/**
 * OkHttp工具类
 *
 * @author Emac
 * @since 2016-09-27
 */
public class OkHttpUtils {

    private static Charset CHARSET_UTF_8 = Charset.forName("UTF-8");

    /**
     * 获取请求体内容
     *
     * @param request 请求对象
     * @apiNote 如果出现异常, 则忽略, 并返回empty
     */
    public static Optional<String> getBody(Request request) {
        if (request == null || _bodyEncoded(request.headers())) {
            return empty();
        }
        RequestBody body = request.body();
        if (body == null) {
            return empty();
        }
        try {
            // 1.写入buffer
            Buffer buffer = new Buffer();
            body.writeTo(buffer);
            // 2.指定字符集
            MediaType contentType = body.contentType();
            Charset charset = contentType == null ? CHARSET_UTF_8 : contentType.charset(CHARSET_UTF_8);
            // 3.从buffer中读取字符串
            if (!_isPlaintext(buffer)) {
                return empty();
            }
            return of(buffer.clone().readString(charset));
        } catch (Exception e) {
            // 忽略并返回empty
            return empty();
        }
    }

    /**
     * 获取响应体内容
     *
     * @param response 响应对象
     */
    public static Optional<String> getBody(Response response) {
        if (response == null || !HttpHeaders.hasBody(response) || _bodyEncoded(response.headers())) {
            return empty();
        }
        ResponseBody body = response.body();
        if (body == null) {
            return empty();
        }
        try {
            // 1.获取buffer
            BufferedSource source = body.source();
            source.request(MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();
            // 2.指定字符集
            MediaType contentType = body.contentType();
            Charset charset = contentType == null ? CHARSET_UTF_8 : contentType.charset(CHARSET_UTF_8);
            // 3.从buffer中读取字符串
            if (body.contentLength() == 0) {
                return empty();
            }
            return of(buffer.clone().readString(charset));
        } catch (Exception e) {
            return empty();
        }
    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    private static boolean _isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                if (isISOControl(prefix.readUtf8CodePoint())) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            // 忽略这个异常
            return false; // Truncated UTF-8 sequence.
        }
    }

    private static boolean _bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }
}
